package com.centroestetico.servicios;

import com.centroestetico.servicios.Servicio;
import com.centroestetico.database.ServicioDAO;
import com.centroestetico.ui.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.util.List;

/**
 * Controller para mostrar el historial de servicios de un cliente.
 */
public class HistorialServiciosController {

    @FXML private VBox root;
    @FXML private TableView<Servicio>   tableView;
    @FXML private TableColumn<Servicio,String> colFecha;
    @FXML private TableColumn<Servicio,String> colDescripcion;
    @FXML private TableColumn<Servicio,String> colMonto;
    @FXML private TableColumn<Servicio,String> colEstado;
    @FXML private TableColumn<Servicio,String> colEmpleado;

    private final ObservableList<Servicio> servicios = FXCollections.observableArrayList();
    private int clienteId;

    /**
     * Inicializa el controller y la tabla.
     * Este método se llama automáticamente tras cargar el FXML.
     */
    @FXML
    private void initialize() {
        colFecha.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getFecha().toString()));
        colDescripcion.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getDescripcion()));
        colMonto.setCellValueFactory(c ->
                new SimpleStringProperty("$" + String.format("%.2f", c.getValue().getMonto())));
        colEstado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getEstado()));
        colEmpleado.setCellValueFactory(c ->
                new SimpleStringProperty(c.getValue().getNombreEmpleado()));

        tableView.setItems(servicios);
    }

    /**
     * Llama este método tras instanciar el controller para fijar el cliente
     * y cargar sus servicios.
     */
    public void init(int clienteId) {
        this.clienteId = clienteId;
        List<Servicio> lista = ServicioDAO.obtenerServiciosPorCliente(clienteId);
        servicios.setAll(lista);
    }

    /**
     * Botón «⟵ Volver» — retorna al calendario/menu principal.
     */
    @FXML
    private void onVolverMenu() {
        MainController main =
                (MainController) root.getScene()
                        .getRoot()
                        .getProperties()
                        .get("fx:controller");
        if (main != null) main.volverMenu();
    }
}
