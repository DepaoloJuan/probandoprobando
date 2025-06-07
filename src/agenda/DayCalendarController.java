package com.centroestetico.agenda;

import com.centroestetico.database.AgendaDAO;
import com.centroestetico.database.EmpleadoDAO;
import com.centroestetico.empleados.Empleado;                       // ②
import com.centroestetico.turnos.AsignarServicioWindow;            // ③
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.RowConstraints;
import javafx.stage.Stage;                                          // ④
import com.centroestetico.agenda.TurnoVista;

import java.time.LocalDate;                                        // ⑤
import java.time.LocalTime;                                        // ⑥
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DayCalendarController {

    @FXML private DatePicker datePicker;
    @FXML private GridPane   grid;

    private List<Empleado> empleados;
    private final LocalTime horaInicio = LocalTime.of(7,30);
    private final LocalTime horaFin    = LocalTime.of(22, 0);

    @FXML
    private void initialize() {
        datePicker.setValue(LocalDate.now());
        empleados = EmpleadoDAO.obtenerEmpleados();
        construirEjesFijos();
        cargarTurnos();
    }

    @FXML private void onFechaCambiada()   { cargarTurnos(); }
    @FXML private void onHoy()             { datePicker.setValue(LocalDate.now()); cargarTurnos(); }
    @FXML private void onSemanaAnterior()  { datePicker.setValue(datePicker.getValue().minusWeeks(1)); }
    @FXML private void onSemanaSiguiente() { datePicker.setValue(datePicker.getValue().plusWeeks(1)); }

    private void construirEjesFijos() {
        grid.getChildren().clear();
        grid.getColumnConstraints().clear();
        grid.getRowConstraints().clear();

        // Columna de horas
        grid.getColumnConstraints().add(new ColumnConstraints(70));

        // Columnas por empleado
        empleados.forEach(e -> grid.getColumnConstraints().add(new ColumnConstraints(140)));

        // Filas de media hora
        LocalTime t = horaInicio;
        int row = 1;
        while (!t.isAfter(horaFin)) {
            grid.getRowConstraints().add(new RowConstraints(40));
            Label lblHora = new Label(t.toString());
            lblHora.getStyleClass().add("hora-cell");
            grid.add(lblHora, 0, row);
            t = t.plusMinutes(30);
            row++;
        }

        // Cabecera con nombre completo del empleado
        for (int i = 0; i < empleados.size(); i++) {
            Empleado e = empleados.get(i);
            Label h = new Label(e.getNombre() + " " + e.getApellido());
            h.getStyleClass().add("empleado-head");
            grid.add(h, i + 1, 0);
        }
    }

    private void cargarTurnos() {
        // Limpia todas las celdas de contenido (salvo fila 0 y col 0)
        grid.getChildren().removeIf(n -> {
            Integer c = GridPane.getColumnIndex(n),
                    r = GridPane.getRowIndex(n);
            return c != null && r != null;
        });

        // Trae lista de turnos y construye un mapa clave->TurnoVista
        List<TurnoVista> turnos = (List<TurnoVista>) AgendaDAO.obtenerTurnosPorFecha(datePicker.getValue());
        Map<String, TurnoVista> mapa = turnos.stream()
                .collect(Collectors.toMap(
                        t -> t.empleado + "|" + t.hora,   // usa campo público empleado y hora
                        t -> t));

        // Recorre nuevamente cada fila y columna
        LocalTime t = horaInicio;
        int row = 1;
        while (!t.isAfter(horaFin)) {
            for (int col = 0; col < empleados.size(); col++) {
                Empleado emp = empleados.get(col);
                String key = emp.getNombre() + " " + emp.getApellido() + "|" + t.toString();
                if (mapa.containsKey(key)) {
                    // si hay turno, bloque rosa con cliente y servicio
                    grid.add(crearBloqueTurno(mapa.get(key)), col + 1, row);
                } else {
                    // si no, celda vacía clickeable
                    grid.add(crearCeldaLibre(emp, t), col + 1, row);
                }
            }
            t = t.plusMinutes(30);
            row++;
        }
    }

    private Label crearBloqueTurno(TurnoVista tv) {
        // usa tv.cliente y tv.servicio que son public String
        Label lbl = new Label(tv.cliente + "\n" + tv.servicio);
        lbl.setPadding(new Insets(4));
        lbl.getStyleClass().add("bloque-turno");
        return lbl;
    }

    /**  Una única definición de crearCeldaLibre  */
    private Pane crearCeldaLibre(Empleado e, LocalTime hora) {
        Pane pane = new Pane();
        pane.getStyleClass().add("celda-libre");
        pane.setOnMouseClicked(ev -> {
            // PASA SIEMPRE PRIMERO el Stage, luego (fecha, hora, empleado, callback)
            Stage dialog = new Stage();
            AsignarServicioWindow win = new AsignarServicioWindow();
            win.start(
                    dialog,                          // Stage
                    datePicker.getValue(),           // LocalDate
                    hora,                            // LocalTime
                    e,                               // Empleado
                    this::cargarTurnos               // Runnable callback
            );
        });
        return pane;
    }
}
