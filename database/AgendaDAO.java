package com.centroestetico.database;

import com.centroestetico.agenda.TurnoVista;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class AgendaDAO {

    /** Turnos de un día */
    public static List<TurnoVista> obtenerTurnosPorFecha(LocalDate fecha) {
        List<TurnoVista> lista = new ArrayList<>();
        String sql = """
            SELECT t.fecha, t.hora,
                   c.nombre AS cliente_nombre, c.apellido AS cliente_apellido,
                   s.descripcion AS servicio_desc,
                   e.nombre AS empleado_nombre, e.apellido AS empleado_apellido,
                   t.estado
            FROM turnos t
            JOIN clientes c ON t.id_cliente = c.id
            JOIN empleados e ON t.id_empleado = e.id
            JOIN servicios_base s ON t.id_servicio = s.id
            WHERE t.fecha = ?
            ORDER BY t.hora
        """;
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(fecha));
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                LocalDate f = rs.getDate("fecha").toLocalDate();
                String hora     = rs.getString("hora");
                String cliente  = rs.getString("cliente_nombre") + " " + rs.getString("cliente_apellido");
                String servicio = rs.getString("servicio_desc");
                String empleado = rs.getString("empleado_nombre") + " " + rs.getString("empleado_apellido");
                String estado   = rs.getString("estado");
                lista.add(new TurnoVista(f, hora, cliente, servicio, empleado, estado));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener turnos del día: " + e.getMessage());
        }
        return lista;
    }

    /** Turnos en un rango (p.ej. semana) */
    public static List<TurnoVista> obtenerTurnosEntreFechas(LocalDate inicio, LocalDate fin) {
        List<TurnoVista> lista = new ArrayList<>();
        String sql = """
            SELECT t.fecha, t.hora,
                   c.nombre AS cliente_nombre, c.apellido AS cliente_apellido,
                   s.descripcion AS servicio_desc,
                   e.nombre AS empleado_nombre, e.apellido AS empleado_apellido,
                   t.estado
            FROM turnos t
            JOIN clientes c ON t.id_cliente = c.id
            JOIN empleados e ON t.id_empleado = e.id
            JOIN servicios_base s ON t.id_servicio = s.id
            WHERE t.fecha BETWEEN ? AND ?
            ORDER BY t.fecha, t.hora
        """;
        try (Connection conn = MySQLConnection.connect();
             PreparedStatement p = conn.prepareStatement(sql)) {
            p.setDate(1, Date.valueOf(inicio));
            p.setDate(2, Date.valueOf(fin));
            ResultSet rs = p.executeQuery();
            while (rs.next()) {
                LocalDate f = rs.getDate("fecha").toLocalDate();
                String hora     = rs.getString("hora");
                String cliente  = rs.getString("cliente_nombre") + " " + rs.getString("cliente_apellido");
                String servicio = rs.getString("servicio_desc");
                String empleado = rs.getString("empleado_nombre") + " " + rs.getString("empleado_apellido");
                String estado   = rs.getString("estado");
                lista.add(new TurnoVista(f, hora, cliente, servicio, empleado, estado));
            }
        } catch (SQLException e) {
            System.err.println("❌ Error al obtener turnos semanales: " + e.getMessage());
        }
        return lista;
    }
}

