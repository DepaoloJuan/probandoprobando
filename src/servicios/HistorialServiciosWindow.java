package com.centroestetico.servicios;

import com.centroestetico.database.ServicioDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class HistorialServiciosWindow {

    private ObservableList<Servicio> serviciosRealizados = FXCollections.observableArrayList();

    public void start(Stage stage, int idCliente) {
        stage.setTitle("Historial de Servicios del Cliente");

        TableView<Servicio> tableView = new TableView<>(serviciosRealizados);

        TableColumn<Servicio, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getFecha().toString()));

        TableColumn<Servicio, String> colDescripcion = new TableColumn<>("Servicio");
        colDescripcion.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getDescripcion()));

        TableColumn<Servicio, String> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(cellData ->
                new SimpleStringProperty("$" + String.format("%.2f", cellData.getValue().getMonto())));

        TableColumn<Servicio, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getEstado()));

        TableColumn<Servicio, String> colEmpleado = new TableColumn<>("Empleado");
        colEmpleado.setCellValueFactory(cellData ->
                new SimpleStringProperty(cellData.getValue().getNombreEmpleado())); // ⚠️ Asegurate de tener este campo en Servicio.java

        tableView.getColumns().addAll(colFecha, colDescripcion, colMonto, colEstado, colEmpleado);

        VBox layout = new VBox(10);
        layout.getChildren().addAll(tableView);

        serviciosRealizados.setAll(ServicioDAO.obtenerServiciosPorCliente(idCliente));

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();
    }
}


