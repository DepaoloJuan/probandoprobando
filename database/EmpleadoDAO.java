package com.centroestetico.database;

import com.centroestetico.empleados.Empleado;
import com.centroestetico.servicios.Servicio;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class EmpleadoDAO {

    // Obtener todos los empleados
    public static List<Empleado> obtenerEmpleados() {
        List<Empleado> empleados = new ArrayList<>();
        String sql = "SELECT * FROM empleados";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                Empleado empleado = new Empleado(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email"),
                        rs.getInt("porcentaje_ganancia")
                );
                empleados.add(empleado);
            }
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener empleados: " + e.getMessage());
        }

        return empleados;
    }

    // Agregar nuevo empleado
    public static void agregarEmpleado(Empleado empleado) {
        String sql = "INSERT INTO empleados (nombre, apellido, telefono, email, porcentaje_ganancia) VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getApellido());
            pstmt.setString(3, empleado.getTelefono());
            pstmt.setString(4, empleado.getEmail());
            pstmt.setInt(5, empleado.getPorcentajeGanancia());

            pstmt.executeUpdate();
            System.out.println("✅ Empleado agregado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al agregar empleado: " + e.getMessage());
        }
    }

    // Actualizar datos de un empleado
    public static void actualizarEmpleado(Empleado empleado) {
        String sql = "UPDATE empleados SET nombre = ?, apellido = ?, telefono = ?, email = ?, porcentaje_ganancia = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, empleado.getNombre());
            pstmt.setString(2, empleado.getApellido());
            pstmt.setString(3, empleado.getTelefono());
            pstmt.setString(4, empleado.getEmail());
            pstmt.setInt(5, empleado.getPorcentajeGanancia());
            pstmt.setInt(6, empleado.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Empleado actualizado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar empleado: " + e.getMessage());
        }
    }

    // Eliminar un empleado por ID
    public static void eliminarEmpleado(int id) {
        String sql = "DELETE FROM empleados WHERE id = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Empleado eliminado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar empleado: " + e.getMessage());
        }
    }

    // Obtener servicios realizados por un empleado en un rango de fechas
    public static List<Servicio> obtenerServiciosPorEmpleadoYFecha(int idEmpleado, LocalDate desde, LocalDate hasta) {
        List<Servicio> lista = new ArrayList<>();

        String sql = """
            SELECT t.id, t.id_cliente, t.id_empleado, t.fecha, t.hora, s.descripcion, t.costo, t.estado,
                   c.nombre AS cliente_nombre, c.apellido AS cliente_apellido
            FROM turnos t
            JOIN servicios_base s ON t.id_servicio = s.id
            JOIN clientes c ON t.id_cliente = c.id
            WHERE t.id_empleado = ? AND t.fecha BETWEEN ? AND ?
            ORDER BY t.fecha, t.hora
        """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, idEmpleado);
            pstmt.setDate(2, Date.valueOf(desde));
            pstmt.setDate(3, Date.valueOf(hasta));

            ResultSet rs = pstmt.executeQuery();

            while (rs.next()) {
                Servicio servicio = new Servicio(
                        rs.getInt("id"),
                        rs.getInt("id_cliente"),
                        rs.getInt("id_empleado"),
                        rs.getDate("fecha").toLocalDate(),
                        rs.getString("descripcion"),
                        rs.getDouble("costo"),
                        rs.getString("estado")
                );

                // Para evitar dependencia directa, se puede usar un setter opcional solo si existe
                try {
                    servicio.getClass().getMethod("setNombreCliente", String.class)
                            .invoke(servicio, rs.getString("cliente_nombre") + " " + rs.getString("cliente_apellido"));
                } catch (Exception ignored) {
                    // Ignoramos si no existe el método
                }

                lista.add(servicio);
            }

        } catch (SQLException e) {
            System.out.println("❌ Error al obtener servicios del empleado: " + e.getMessage());
        }

        return lista;
    }
}
