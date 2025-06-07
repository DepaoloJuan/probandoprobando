package com.centroestetico.agenda;

import com.centroestetico.database.AgendaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import java.time.*;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

public class AgendaWindow {
    private final TableView<TurnoVista> tableView = new TableView<>();
    private final ObservableList<TurnoVista> turnos = FXCollections.observableArrayList();

    public void start(Stage stage) {
        stage.setTitle("Agenda de Turnos");

        DatePicker datePicker = new DatePicker(LocalDate.now());
        Button btnDia    = new Button("⏰ Día");
        Button btnSemana = new Button("🗓 Semana");
        btnDia   .setOnAction(e -> cargarDelDia(datePicker.getValue()));
        btnSemana.setOnAction(e -> cargarSemana(datePicker.getValue()));

        // Columnas
        TableColumn<TurnoVista,String> cHora     = new TableColumn<>("Hora");
        cHora    .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().hora));
        TableColumn<TurnoVista,String> cCliente  = new TableColumn<>("Cliente");
        cCliente .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().cliente));
        TableColumn<TurnoVista,String> cServicio = new TableColumn<>("Servicio");
        cServicio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().servicio));
        TableColumn<TurnoVista,String> cEmpleado = new TableColumn<>("Empleado");
        cEmpleado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().empleado));
        TableColumn<TurnoVista,String> cEstado   = new TableColumn<>("Estado");
        cEstado  .setCellValueFactory(c -> new SimpleStringProperty(c.getValue().estado));

        tableView.getColumns().addAll(cHora, cCliente, cServicio, cEmpleado, cEstado);
        tableView.setItems(turnos);

        HBox controls = new HBox(10, datePicker, btnDia, btnSemana);
        controls.setPadding(new Insets(8));
        VBox root = new VBox(10, controls, tableView);
        root.setPadding(new Insets(15));

        cargarDelDia(datePicker.getValue());

        stage.setScene(new Scene(root, 800, 500));
        stage.show();
    }

    private void cargarDelDia(LocalDate fecha) {
        List<TurnoVista> lista = AgendaDAO.obtenerTurnosPorFecha(fecha);
        turnos.setAll(lista);
    }

    private void cargarSemana(LocalDate fecha) {
        LocalDate lunes   = fecha.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = fecha.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));
        List<TurnoVista> lista = AgendaDAO.obtenerTurnosEntreFechas(lunes, domingo);
        turnos.setAll(lista);
    }
}
