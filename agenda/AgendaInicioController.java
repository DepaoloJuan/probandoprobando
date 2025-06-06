package com.centroestetico.agenda;

import com.centroestetico.database.AgendaDAO;
import com.centroestetico.database.EmpleadoDAO;
import com.centroestetico.empleados.Empleado;
import com.centroestetico.turnos.AsignarServicioWindow;
import com.centroestetico.ui.MainController;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import com.centroestetico.MainWindowController;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class AgendaInicioController {

    @FXML private DatePicker datePicker;
    @FXML private GridPane grid;

    private List<Empleado> empleados;
    private final LocalTime horaInicio = LocalTime.of(7, 30);
    private final LocalTime horaFin    = LocalTime.of(22, 0);

    private MainController main;

    public void setMainController(MainController mc) {
        this.main = mc;
    }

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());
        empleados = EmpleadoDAO.obtenerEmpleados();

        construirGrilla();
        cargarTurnos();
        datePicker.valueProperty().addListener((obs, oldDate, newDate) -> cargarTurnos());
    }

    /* ==== Navegación de botones superiores ==== */
    @FXML private void onIrClientes()  { main.showClientes(); }
    @FXML private void onIrEmpleados() { main.showEmpleados(); }
    @FXML private void onIrServicios() { main.showServicios(); }
    @FXML private void onIrAgenda()    { main.showAgenda(); }
    @FXML private void onFechaCambiada() { cargarTurnos(); }

    private void construirGrilla() {
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        // primera columna (horarios)
        ColumnConstraints colHora = new ColumnConstraints(70);
        grid.getColumnConstraints().add(colHora);

        // columnas de empleados
        for (int i = 0; i < empleados.size(); i++) {
            ColumnConstraints cc = new ColumnConstraints(140);
            grid.getColumnConstraints().add(cc);
        }

        // fila 0: nombres de empleados
        for (int col = 0; col < empleados.size(); col++) {
            Empleado emp = empleados.get(col);
            Label h = new Label(emp.getNombre());
            h.getStyleClass().add("empleado-head");
            grid.add(h, col + 1, 0); // columna 1 en adelante
        }

        // filas de horarios
        LocalTime t = horaInicio;
        int row = 1;
        while (!t.isAfter(horaFin)) {
            RowConstraints rc = new RowConstraints(40);
            grid.getRowConstraints().add(rc);

            Label lblHora = new Label(t.toString());
            lblHora.getStyleClass().add("hora-cell");
            grid.add(lblHora, 0, row); // columna 0

            t = t.plusMinutes(30);
            row++;
        }
    }

    private void cargarTurnos() {
        grid.getChildren().removeIf(n -> {
            Integer col = GridPane.getColumnIndex(n);
            Integer row = GridPane.getRowIndex(n);
            return col != null && col > 0 && row != null && row > 0;
        });

        List<TurnoVista> turnos = AgendaDAO.obtenerTurnosPorFecha(datePicker.getValue());
        Map<String, TurnoVista> mapa = turnos.stream()
                .collect(Collectors.toMap(
                        t -> t.empleado + "|" + t.hora,
                        t -> t));

        LocalTime t = horaInicio;
        int row = 1;
        while (!t.isAfter(horaFin)) {
            for (int col = 0; col < empleados.size(); col++) {
                Empleado emp = empleados.get(col);
                String key = emp.getNombre() + " " + emp.getApellido() + "|" + t.toString();

                if (mapa.containsKey(key)) {
                    TurnoVista tv = mapa.get(key);
                    Label lbl = crearCeldaOcupada(tv);
                    grid.add(lbl, col + 1, row);
                } else {
                    Pane empty = crearCeldaVacia(emp, t);
                    grid.add(empty, col + 1, row);
                }
            }
            t = t.plusMinutes(30);
            row++;
        }
    }

    private Label crearCeldaOcupada(TurnoVista tv) {
        Label lbl = new Label(tv.cliente + "\n" + tv.servicio);
        lbl.setPadding(new Insets(4));
        lbl.getStyleClass().add("bloque-turno");
        return lbl;
    }

    private Pane crearCeldaVacia(Empleado e, LocalTime hora) {
        Pane p = new Pane();
        p.getStyleClass().add("celda-libre");
        p.setOnMouseClicked(ev -> {
            Stage stage = new Stage();
            new AsignarServicioWindow().start(stage, datePicker.getValue(), hora, e, this::cargarTurnos);
        });
        return p;
    }
}
