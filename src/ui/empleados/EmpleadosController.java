package com.centroestetico.ui.empleados;

import com.centroestetico.empleados.Empleado;
import com.centroestetico.database.EmpleadoDAO;
import com.centroestetico.empleados.ResumenEmpleadoWindow;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.util.List;

public class EmpleadosController {

    private com.centroestetico.ui.MainController main;

    public void setMainController(com.centroestetico.ui.MainController main) {
        this.main = main;
    }



    /* ---------- nodos inyectados desde FXML ---------- */
    @FXML private TextField txtNombre;
    @FXML private TextField txtApellido;
    @FXML private TextField txtTelefono;
    @FXML private TextField txtPorcentaje;          // lo tratamos como texto y validamos al guardar

    @FXML private TableView<Empleado> tableView;
    @FXML private TableColumn<Empleado,String>  colNombre;
    @FXML private TableColumn<Empleado,String>  colApellido;
    @FXML private TableColumn<Empleado,String>  colTelefono;
    @FXML private TableColumn<Empleado,Integer> colPorcentaje;

    /* ---------- datos ---------- */
    private final ObservableList<Empleado> empleados = FXCollections.observableArrayList();

    /* === ciclo de vida === */
    @FXML
    private void initialize() {
        // vincular columnas
        colNombre.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getNombre()));
        colApellido.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getApellido()));
        colTelefono.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getTelefono()));
        colPorcentaje.setCellValueFactory(c -> new SimpleIntegerProperty(c.getValue().getPorcentajeGanancia()).asObject());

        tableView.setItems(empleados);
        tableView.setOnMouseClicked(e -> llenarFormulario());

        cargarEmpleados();
    }

    /* === acciones === */
    @FXML private void onAgregar() {
        Integer porc = parsePorcentaje();
        if (porc == null) return;

        Empleado nuevo = new Empleado(
                0,
                txtNombre.getText(),
                txtApellido.getText(),
                txtTelefono.getText(),
                "",                      // email no está en el FXML (ajustalo si lo usás)
                porc
        );
        EmpleadoDAO.agregarEmpleado(nuevo);
        empleados.add(nuevo);
        limpiarCampos();
    }
    @FXML
    private void volverMenu() {
        if (main != null) {
            main.volverMenu();
        }
    }


    @FXML private void onActualizar() {
        Empleado sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un empleado para modificar."); return; }

        Integer porc = parsePorcentaje();
        if (porc == null) return;

        sel.setNombre(txtNombre.getText());
        sel.setApellido(txtApellido.getText());
        sel.setTelefono(txtTelefono.getText());
        sel.setPorcentajeGanancia(porc);

        EmpleadoDAO.actualizarEmpleado(sel);
        tableView.refresh();
        limpiarCampos();
    }

    @FXML private void onEliminar() {
        Empleado sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un empleado para eliminar."); return; }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION,"¿Eliminar?");
        confirm.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                EmpleadoDAO.eliminarEmpleado(sel.getId());
                empleados.remove(sel);
            }
        });
    }

    @FXML private void onVerResumen() {
        Empleado sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un empleado."); return; }

        // reutilizamos la ventana existente mientras no la migramos
        new ResumenEmpleadoWindow().start(new Stage(), sel);
    }

    /* === utilidades === */
    private void cargarEmpleados() {
        List<Empleado> lista = EmpleadoDAO.obtenerEmpleados();
        empleados.setAll(lista);
    }

    private void llenarFormulario() {
        Empleado e = tableView.getSelectionModel().getSelectedItem();
        if (e != null) {
            txtNombre.setText(e.getNombre());
            txtApellido.setText(e.getApellido());
            txtTelefono.setText(e.getTelefono());
            txtPorcentaje.setText(String.valueOf(e.getPorcentajeGanancia()));
        }
    }

    private void limpiarCampos() {
        txtNombre.clear(); txtApellido.clear(); txtTelefono.clear(); txtPorcentaje.clear();
    }

    private Integer parsePorcentaje() {
        try {
            int p = Integer.parseInt(txtPorcentaje.getText());
            if (p < 1 || p > 100) throw new NumberFormatException();
            return p;
        } catch (NumberFormatException ex) {
            alerta("Porcentaje debe ser un número entre 1 y 100.");
            return null;
        }
    }

    private void alerta(String msg) {
        Alert a = new Alert(Alert.AlertType.INFORMATION,msg); a.setHeaderText(null); a.showAndWait();
    }
}
