package com.centroestetico.empleados;

import com.centroestetico.database.EmpleadoDAO;
import com.centroestetico.servicios.Servicio;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

import java.time.LocalDate;
import java.util.List;

public class ResumenEmpleadoWindow {

    private TableView<Servicio> tableView = new TableView<>();
    private ObservableList<Servicio> servicios = FXCollections.observableArrayList();

    public void start(Stage stage, Empleado empleado) {
        stage.setTitle("Resumen de Servicios - " + empleado.getNombre() + " " + empleado.getApellido());

        ComboBox<String> comboPeriodo = new ComboBox<>();
        comboPeriodo.getItems().addAll("Semana", "Quincena", "Mes");
        comboPeriodo.setPromptText("Seleccionar periodo");

        Label lblFacturacion = new Label("Total facturado: $0.00");
        Label lblGanancia = new Label("Ganancia del empleado: $0.00");

        // Columnas
        TableColumn<Servicio, String> colFecha = new TableColumn<>("Fecha");
        colFecha.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getFecha().toString()));

        TableColumn<Servicio, String> colDescripcion = new TableColumn<>("Servicio");
        colDescripcion.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getDescripcion()));

        TableColumn<Servicio, String> colMonto = new TableColumn<>("Monto");
        colMonto.setCellValueFactory(cell -> new SimpleStringProperty("$" + cell.getValue().getMonto()));

        TableColumn<Servicio, String> colEstado = new TableColumn<>("Estado");
        colEstado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().getEstado()));

        tableView.getColumns().addAll(colFecha, colDescripcion, colMonto, colEstado);
        tableView.setItems(servicios);

        // Acción al seleccionar periodo
        comboPeriodo.setOnAction(e -> {
            String periodo = comboPeriodo.getValue();
            LocalDate fin = LocalDate.now();
            LocalDate inicio;

            switch (periodo) {
                case "Semana" -> inicio = fin.minusDays(7);
                case "Quincena" -> inicio = fin.minusDays(15);
                case "Mes" -> inicio = fin.minusMonths(1);
                default -> inicio = fin.minusDays(7);
            }

            List<Servicio> lista = EmpleadoDAO.obtenerServiciosPorEmpleadoYFecha(
                    empleado.getId(),
                    inicio,
                    fin
            );


            servicios.setAll(lista);

            double total = lista.stream().mapToDouble(Servicio::getMonto).sum();
            double ganancia = total * empleado.getPorcentajeGanancia() / 100.0;

            lblFacturacion.setText(String.format("Total facturado: $%.2f", total));
            lblGanancia.setText(String.format("Ganancia del empleado: $%.2f", ganancia));
        });

        VBox layout = new VBox(10,
                new HBox(10, new Label("Periodo:"), comboPeriodo),
                tableView,
                lblFacturacion,
                lblGanancia
        );
        layout.setPadding(new javafx.geometry.Insets(15));

        Scene scene = new Scene(layout, 700, 500);
        stage.setScene(scene);
        stage.show();
    }
}
