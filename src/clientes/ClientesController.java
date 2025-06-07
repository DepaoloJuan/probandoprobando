package com.centroestetico.clientes;

import com.centroestetico.database.ClienteDAO;
import com.centroestetico.ui.MainController;

import com.centroestetico.servicios.HistorialServiciosController;
import com.centroestetico.ui.MainController;

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

    @FXML private VBox root;

    @FXML private TextField txtNombre, txtApellido, txtTelefono, txtEmail;
    @FXML private TableView<Cliente> tableView;
    @FXML private TableColumn<Cliente, String> colNombre, colApellido, colTelefono, colEmail;

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        colNombre  .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colApellido.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellido()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colEmail   .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getEmail()));

        tableView.setItems(clientes);
        tableView.getSelectionModel().selectedItemProperty().addListener(
                (obs, oldV, sel) -> {
                    if (sel != null) cargarEnCampos(sel);
                }
        );

        clientes.setAll(ClienteDAO.obtenerClientes());
    }

    // ── CRUD ──
    @FXML private void onAgregar() {
        Cliente c = new Cliente(0,
                txtNombre.getText(),
                txtApellido.getText(),
                txtTelefono.getText(),
                txtEmail.getText());
        ClienteDAO.agregarCliente(c);
        clientes.add(c);
        limpiarCampos();
    }

    @FXML private void onActualizar() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccione un cliente"); return; }
        sel.setNombre(txtNombre.getText());
        sel.setApellido(txtApellido.getText());
        sel.setTelefono(txtTelefono.getText());
        sel.setEmail(txtEmail.getText());
        ClienteDAO.actualizarCliente(sel);
        tableView.refresh();
    }

    @FXML private void onEliminar() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccione un cliente"); return; }
        ClienteDAO.eliminarCliente(sel.getId());
        clientes.remove(sel);
    }

    @FXML
    private void onVerHistorial() {
        Cliente sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) {
            alerta("Seleccione un cliente");
            return;
        }

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

        } catch (IOException e) {
            e.printStackTrace();
            alerta("Error al cargar historial.");
        }
    }

    @FXML private void onVolverMenu() {
        MainController main =
                (MainController) root.getScene()
                        .getRoot()
                        .getProperties()
                        .get("fx:controller");
        if (main != null) main.volverMenu();
    }

    private void cargarEnCampos(Cliente c) {
        txtNombre.setText(c.getNombre());
        txtApellido.setText(c.getApellido());
        txtTelefono.setText(c.getTelefono());
        txtEmail.setText(c.getEmail());
    }

    private void limpiarCampos() {
        txtNombre.clear();
        txtApellido.clear();
        txtTelefono.clear();
        txtEmail.clear();
    }

    private void alerta(String msg) {
        new Alert(Alert.AlertType.INFORMATION, msg).showAndWait();
    }
}
