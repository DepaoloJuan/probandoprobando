package com.centroestetico.database;

import com.centroestetico.servicios.Servicio;
import com.centroestetico.servicios.ServicioBase;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServicioDAO {

    public static void agregarServicio(Servicio servicio) {
        String sql = "INSERT INTO servicios (id_cliente, id_empleado, fecha, descripcion, monto, estado) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, servicio.getIdCliente());
            pstmt.setInt(2, servicio.getIdEmpleado());
            pstmt.setDate(3, Date.valueOf(servicio.getFecha()));
            pstmt.setString(4, servicio.getDescripcion());
            pstmt.setDouble(5, servicio.getMonto());
            pstmt.setString(6, servicio.getEstado());

            pstmt.executeUpdate();
            System.out.println("✅ Servicio agregado con éxito (tabla servicios - ya en desuso).");
        } catch (SQLException e) {
            System.out.println("❌ Error al agregar servicio: " + e.getMessage());
        }
    }

    // ✅ AHORA LEE DESDE 'turnos' y junta con empleados y servicios_base
    public static List<Servicio> obtenerServiciosPorCliente(int idCliente) {
        List<Servicio> servicios = new ArrayList<>();

        String sql = """
            SELECT t.id, t.id_cliente, t.id_empleado, t.fecha, sb.descripcion, t.costo, t.estado,
                   e.nombre AS empleado_nombre, e.apellido AS empleado_apellido
            FROM turnos t
            JOIN servicios_base sb ON t.id_servicio = sb.id
            JOIN empleados e ON t.id_empleado = e.id
            WHERE t.id_cliente = ?
            ORDER BY t.fecha, t.hora
        """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idCliente);
            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio();
                servicio.setFecha(rs.getDate("fecha").toLocalDate());
                servicio.setDescripcion(rs.getString("descripcion"));
                servicio.setMonto(rs.getDouble("costo")); // columna 'costo' en turnos
                servicio.setEstado(rs.getString("estado"));
                servicio.setNombreEmpleado(
                        rs.getString("empleado_nombre") + " " + rs.getString("empleado_apellido")
                );
                servicios.add(servicio);
            }

            System.out.println("✅ Historial cargado desde turnos para cliente ID " + idCliente);
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener servicios (turnos): " + e.getMessage());
        }

        return servicios;
    }

    public static void agregarServicioBase(ServicioBase servicio) {
        String sql = "INSERT INTO servicios_base (categoria, descripcion, precio) VALUES (?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, servicio.getCategoria());
            pstmt.setString(2, servicio.getDescripcion());
            pstmt.setDouble(3, servicio.getPrecio());

            pstmt.executeUpdate();
            System.out.println("✅ Servicio base agregado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al agregar servicio base: " + e.getMessage());
        }
    }

    public static void actualizarServicioBase(ServicioBase original, String nuevaCategoria, String nuevaDescripcion, double nuevoPrecio) {
        String sql = "UPDATE servicios_base SET categoria = ?, descripcion = ?, precio = ? WHERE descripcion = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, nuevaCategoria);
            pstmt.setString(2, nuevaDescripcion);
            pstmt.setDouble(3, nuevoPrecio);
            pstmt.setString(4, original.getDescripcion());

            pstmt.executeUpdate();
            System.out.println("✅ Servicio base actualizado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar servicio base: " + e.getMessage());
        }
    }

    public static void eliminarServicioBase(ServicioBase servicio) {
        String sql = "DELETE FROM servicios_base WHERE descripcion = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, servicio.getDescripcion());
            pstmt.executeUpdate();
            System.out.println("✅ Servicio base eliminado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar servicio base: " + e.getMessage());
        }
    }

    public static List<ServicioBase> obtenerServiciosBase() {
        List<ServicioBase> serviciosBase = new ArrayList<>();
        String sql = "SELECT * FROM servicios_base";

        try (Connection conn = MySQLConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                ServicioBase servicio = new ServicioBase(
                        rs.getInt("id"),
                        rs.getString("categoria"),
                        rs.getString("descripcion"),
                        rs.getDouble("precio")
                );

                serviciosBase.add(servicio);
            }
            System.out.println("✅ Servicios base obtenidos correctamente.");
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener servicios base: " + e.getMessage());
        }
        return serviciosBase;
    }
}

