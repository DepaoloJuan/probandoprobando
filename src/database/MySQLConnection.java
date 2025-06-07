package com.centroestetico.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MySQLConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/centro_estetico";
    private static final String USER = "root"; // ⚡ Cambiar si tu usuario es otro
    private static final String PASSWORD = "Manager1"; // ⚡ Cambiar si tu contraseña no está vacía

    public static Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("✅ Conexión a MySQL exitosa");
        } catch (SQLException e) {
            System.out.println("❌ Error al conectar a MySQL: " + e.getMessage());
        }
        return conn;
    }
}
