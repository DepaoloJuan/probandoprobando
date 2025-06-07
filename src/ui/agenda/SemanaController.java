package com.centroestetico.ui.agenda;

import com.centroestetico.agenda.TurnoVista;
import com.centroestetico.database.AgendaDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import java.time.LocalDate;
import java.util.List;
import java.util.function.Consumer;

import com.centroestetico.export.ExcelExporter;
import javafx.stage.FileChooser;
import javafx.stage.Stage;


public class SemanaController {

    @FXML private Label lblLunes;
    @FXML private TableView<TurnoVista> tableView;
    @FXML private TableColumn<TurnoVista,String> colFecha;
    @FXML private TableColumn<TurnoVista,String> colHora;
    @FXML private TableColumn<TurnoVista,String> colCliente;
    @FXML private TableColumn<TurnoVista,String> colServicio;
    @FXML private TableColumn<TurnoVista,String> colEmpleado;
    @FXML private TableColumn<TurnoVista,String> colEstado;

    private final ObservableList<TurnoVista> turnos = FXCollections.observableArrayList();
    private LocalDate lunesSeleccionado;
    private Consumer<Void> callbackVolverDia;

    /* ==== inicialización programática ==== */
    void init(LocalDate lunes, Consumer<Void> volverDia) {
        this.lunesSeleccionado = lunes;
        this.callbackVolverDia = volverDia;
        lblLunes.setText(lunes.toString());
        cargarTurnosSemana();
    }

    @FXML
    private void initialize() {
        colFecha.setCellValueFactory(c -> {
            LocalDate f = c.getValue().fecha;
            return new SimpleStringProperty(f != null ? f.toString() : "");
        });
        colHora.setCellValueFactory    (c -> new SimpleStringProperty(c.getValue().hora));
        colCliente.setCellValueFactory (c -> new SimpleStringProperty(c.getValue().cliente));
        colServicio.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().servicio));
        colEmpleado.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().empleado));
        colEstado.setCellValueFactory  (c -> new SimpleStringProperty(c.getValue().estado));

        tableView.setItems(turnos);
    }

    /* ---- botones ---- */
    @FXML private void onSemanaAnterior() {
        lunesSeleccionado = lunesSeleccionado.minusWeeks(1);
        lblLunes.setText(lunesSeleccionado.toString());
        cargarTurnosSemana();
    }

    @FXML private void onSemanaSiguiente() {
        lunesSeleccionado = lunesSeleccionado.plusWeeks(1);
        lblLunes.setText(lunesSeleccionado.toString());
        cargarTurnosSemana();
    }

    @FXML private void onVolverDia() { callbackVolverDia.accept(null); }

    /* ---- carga ---- */
    private void cargarTurnosSemana() {
        List<TurnoVista> lista = AgendaDAO.obtenerTurnosEntreFechas(
                lunesSeleccionado, lunesSeleccionado.plusDays(6));
        turnos.setAll(lista);
    }

    /* Exportar pendiente */
    @FXML
    private void onExportar() {
        FileChooser fc = new FileChooser();
        fc.setTitle("Guardar turnos semana");
        fc.getExtensionFilters().add(
                new FileChooser.ExtensionFilter("Archivo Excel (*.xlsx)", "*.xlsx"));
        fc.setInitialFileName("turnos-" + lunesSeleccionado + ".xlsx");
        Stage stage = (Stage) tableView.getScene().getWindow();
        var file = fc.showSaveDialog(stage);

        if (file != null) {
            try {
                ExcelExporter.exportarTurnos(turnos, file.toPath());
                new Alert(Alert.AlertType.INFORMATION,
                        "Exportación exitosa:\n" + file.getAbsolutePath()).showAndWait();
            } catch (Exception ex) {
                ex.printStackTrace();
                new Alert(Alert.AlertType.ERROR,
                        "Error al exportar:\n" + ex.getMessage()).showAndWait();
            }
        }
    }

}

