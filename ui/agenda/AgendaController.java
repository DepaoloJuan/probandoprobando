package com.centroestetico.ui.agenda;

import com.centroestetico.database.AgendaDAO;
import com.centroestetico.turnos.AsignarServicioWindow;
import com.centroestetico.ui.MainController;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;
import com.centroestetico.agenda.TurnoVista;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

import javafx.fxml.FXMLLoader;   //  ←  nuevo
import javafx.scene.Parent;      //  ←  nuevo
import java.io.IOException;      //  ←  si aún no estaba

import com.centroestetico.export.ExcelExporter;
import javafx.stage.FileChooser;
import java.nio.file.Path;



/** Agenda diaria / semanal incrustada en la ventana principal */
public class AgendaController {

    /* ---------- FXML ---------- */
    @FXML private DatePicker datePicker;
    @FXML private Button     btnVista;

    @FXML private TableView<TurnoVista>           tableView;
    @FXML private TableColumn<TurnoVista,String>  colHora;
    @FXML private TableColumn<TurnoVista,String>  colCliente;
    @FXML private TableColumn<TurnoVista,String>  colServicio;
    @FXML private TableColumn<TurnoVista,String>  colEmpleado;
    @FXML private TableColumn<TurnoVista,String>  colEstado;

    // AgendaController.java  (solo muestro lo nuevo)
    private MainController main;          // referencia al padre

    public void setMainController(MainController mc) {   // lo llama MainController
        this.main = mc;
    }


    /* ---------- datos ---------- */
    private final ObservableList<TurnoVista> turnos = FXCollections.observableArrayList();
    private boolean vistaSemanal = false;

    /* ===== ciclo de vida ===== */
    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());

        colHora.setCellValueFactory    (c -> new SimpleStringProperty(c.getValue().hora));
        colCliente.setCellValueFactory (c -> new SimpleStringProperty(c.getValue().cliente));
        colServicio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().servicio));
        colEmpleado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().empleado));
        colEstado.setCellValueFactory  (c -> new SimpleStringProperty(c.getValue().estado));

        tableView.setItems(turnos);
        cargarTurnosDelDia();          // vista inicial


    }

    /* ===== handlers ===== */
    @FXML private void onFechaCambiada() { recargar(); }
    @FXML private void onHoy()          { datePicker.setValue(LocalDate.now()); recargar(); }

    @FXML
    private void onVerSemana() {
        try {
            FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/com/centroestetico/ui/agenda/SemanaView.fxml"));
            Parent root = fx.load();
            SemanaController sc = fx.getController();

            LocalDate lunes = datePicker.getValue()
                    .with(java.time.temporal.TemporalAdjusters.previousOrSame(java.time.DayOfWeek.MONDAY));

            // callback: volver al día
            sc.init(lunes, v -> main.setView("/com/centroestetico/ui/agenda/AgendaView.fxml"));

            // mostrar la vista semanal
            main.setView(root);     // <<<<  usa la referencia al MainController
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void onNuevoTurno() {
        Stage turnoStage = new Stage();
        new AsignarServicioWindow().start(
                turnoStage,
                LocalDate.now(),         // Fecha actual por defecto
                LocalTime.of(8, 0),      // Hora por defecto (8:00 AM)
                null,                    // Empleado sin seleccionar aún
                this::recargar           // Refresca la agenda al cerrar
        );
    }

    /* ===== carga de datos ===== */
    private void recargar() {
        if (vistaSemanal) {
            cargarTurnosSemana();
        } else {
            cargarTurnosDelDia();
        }
    }

    private void cargarTurnosDelDia() {
        List<TurnoVista> lista = AgendaDAO.obtenerTurnosPorFecha(datePicker.getValue());
        turnos.setAll(lista);
    }

    private void cargarTurnosSemana() {
        LocalDate seleccionado = datePicker.getValue();
        LocalDate lunes   = seleccionado.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
        LocalDate domingo = seleccionado.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY));

        List<TurnoVista> lista = AgendaDAO.obtenerTurnosEntreFechas(lunes, domingo);
        turnos.setAll(lista);
    }

    @FXML
    private void onExportarDia() {
        var turnosDia = AgendaDAO.obtenerTurnosPorFecha(datePicker.getValue());

        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar turnos del día");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Excel (*.xlsx)", "*.xlsx"));
        fc.setInitialFileName("turnos-" + datePicker.getValue() + ".xlsx");
        var file = fc.showSaveDialog(tableView.getScene().getWindow());

        if (file != null) {
            try {
                ExcelExporter.exportarTurnos(turnosDia, Path.of(file.toURI()));
                new Alert(Alert.AlertType.INFORMATION,
                        "Exportado en:\n" + file.getAbsolutePath()).showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Error al exportar:\n" + ex.getMessage()).showAndWait();
            }
        }
    }

    @FXML
    private void volverMenu() {
        if (main != null) {
            main.volverMenu();
        } else {
            new Alert(Alert.AlertType.ERROR, "Controlador principal no disponible").showAndWait();
        }
    }


}



