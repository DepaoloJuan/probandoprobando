package com.centroestetico.database;

import com.centroestetico.clientes.Cliente;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ClienteDAO {

    public static void agregarCliente(Cliente cliente) {
        String sql = "INSERT INTO clientes (nombre, apellido, telefono, email) VALUES (?, ?, ?, ?)";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());

            pstmt.executeUpdate();
            System.out.println("✅ Cliente agregado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al agregar cliente: " + e.getMessage());
        }
    }

    public static List<Cliente> obtenerClientes() {
        List<Cliente> clientes = new ArrayList<>();
        String sql = "SELECT * FROM clientes";

        try (Connection conn = MySQLConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                Cliente cliente = new Cliente(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getString("apellido"),
                        rs.getString("telefono"),
                        rs.getString("email")
                );
                clientes.add(cliente);
            }
            System.out.println("✅ Clientes obtenidos correctamente.");
        } catch (SQLException e) {
            System.out.println("❌ Error al obtener clientes: " + e.getMessage());
        }
        return clientes;
    }

    public static void actualizarCliente(Cliente cliente) {
        String sql = "UPDATE clientes SET nombre = ?, apellido = ?, telefono = ?, email = ? WHERE id = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setString(1, cliente.getNombre());
            pstmt.setString(2, cliente.getApellido());
            pstmt.setString(3, cliente.getTelefono());
            pstmt.setString(4, cliente.getEmail());
            pstmt.setInt(5, cliente.getId());

            pstmt.executeUpdate();
            System.out.println("✅ Cliente actualizado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al actualizar cliente: " + e.getMessage());
        }
    }

    public static void eliminarCliente(int id) {
        String sql = "DELETE FROM clientes WHERE id = ?";

        try (Connection conn = MySQLConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);
            pstmt.executeUpdate();
            System.out.println("✅ Cliente eliminado con éxito.");
        } catch (SQLException e) {
            System.out.println("❌ Error al eliminar cliente: " + e.getMessage());
        }
    }
}

