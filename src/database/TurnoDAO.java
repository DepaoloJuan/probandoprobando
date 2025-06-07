package com.centroestetico.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

public class TurnoDAO {

    public static void agregarTurno(int idCliente, int idServicio, int idEmpleado,
                                    LocalDate fecha, LocalTime hora, double costo, String estado) {

        String sql = """
            INSERT INTO turnos (id_cliente, id_servicio, id_empleado, fecha, hora, costo, estado)
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """;

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            // 🔍 Log previo
            System.out.println("→ Insertando turno:");
            System.out.println("  Cliente: " + idCliente);
            System.out.println("  Servicio: " + idServicio);
            System.out.println("  Empleado: " + idEmpleado);
            System.out.println("  Fecha: " + fecha);
            System.out.println("  Hora: " + hora);
            System.out.println("  Costo: " + costo);
            System.out.println("  Estado: " + estado);

            pstmt.setInt(1, idCliente);
            pstmt.setInt(2, idServicio);
            pstmt.setInt(3, idEmpleado);
            pstmt.setDate(4, java.sql.Date.valueOf(fecha));
            pstmt.setTime(5, java.sql.Time.valueOf(hora));
            pstmt.setDouble(6, costo);
            pstmt.setString(7, estado);

            pstmt.executeUpdate();
            System.out.println("✅ Turno guardado con éxito.");

        } catch (SQLException e) {
            System.out.println("❌ Error al guardar turno: " + e.getMessage());
            e.printStackTrace(); // 💥 Esta línea es CLAVE
        }
    }
}

