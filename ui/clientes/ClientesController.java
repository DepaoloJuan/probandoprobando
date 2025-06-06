package com.centroestetico.ui.clientes;

import com.centroestetico.clientes.Cliente;
import com.centroestetico.database.ClienteDAO;
import com.centroestetico.ui.MainController;
import com.centroestetico.servicios.HistorialServiciosController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;

public class ClientesController {

    // ----- Controles FXML -----
    @FXML private VBox root;

    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtEmail;

    @FXML private TableView<Cliente> tableView;
    @FXML private TableColumn<Cliente, String> colNombre;
    @FXML private TableColumn<Cliente, String> colApellido;
    @FXML private TableColumn<Cliente, String> colTelefono;
    @FXML private TableColumn<Cliente, String> colEmail;

    // ----- Datos -----
    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    // ===== Ciclo de vida =====
    @FXML
    private void initialize() {
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colApellido.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellido()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colEmail.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        tableView.setItems(clientes);
        tableView.setOnMouseClicked(e -> llenarFormulario());

        clientes.setAll(ClienteDAO.obtenerClientes());
    }

    // ===== Acciones botones =====
    @FXML private void onAgregar() {
        Cliente nuevo = new Cliente(
                0,
                txtNombre.getText(),
                txtApellido.getText(),
                txtTelefono.getText(),
                txtEmail.getText()
        );
        ClienteDAO.agregarCliente(nuevo);
        clientes.add(nuevo);
        limpiarCampos();
    }

    @FXML private void onActualizar() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un cliente para actualizar."); return; }

        sel.setNombre(txtNombre.getText());
        sel.setApellido(txtApellido.getText());
        sel.setTelefono(txtTelefono.getText());
        sel.setEmail(txtEmail.getText());

        ClienteDAO.actualizarCliente(sel);
        tableView.refresh();
        limpiarCampos();
    }

    @FXML private void onEliminar() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un cliente para eliminar."); return; }

        ClienteDAO.eliminarCliente(sel.getId());
        clientes.remove(sel);
    }

    @FXML private void onVerHistorial() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un cliente."); return; }

        try {
            FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/com/centroestetico/ui/servicios/HistorialServiciosView.fxml"));
            Node view = fx.load();

            HistorialServiciosController hc = fx.getController();
            hc.init(sel.getId());

            MainController main = (MainController)
                    root.getScene()
                            .getRoot()
                            .getProperties()
                            .get("fx:controller");

            main.setView(view);

        } catch (IOException ex) {
            ex.printStackTrace();
            alerta("Error al cargar historial.");
        }
    }

    // ===== Util =====
    private void llenarFormulario() {
        Cliente c = tableView.getSelectionModel().getSelectedItem();
        if (c != null) {
            txtNombre.setText(c.getNombre());
            txtApellido.setText(c.getApellido());
            txtTelefono.setText(c.getTelefono());
            txtEmail.setText(c.getEmail());
        }
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtEmail.clear();
    }

    private void alerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION, msg);
        a.setHeaderText(null);
        a.showAndWait();
    }
}
