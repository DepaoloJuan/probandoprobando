package com.centroestetico.empleados;

import com.centroestetico.database.EmpleadoDAO;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.util.List;

public class EmpleadosWindow {

    private ObservableList<Empleado> empleados = FXCollections.observableArrayList();
    private TableView<Empleado> tableView = new TableView<>(empleados);

    public void start(Stage stage) {
        stage.setTitle("Gestión de Empleados");

        // Columnas
        TableColumn<Empleado, String> colNombre = new TableColumn<>("Nombre");
        colNombre.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getNombre()));

        TableColumn<Empleado, String> colApellido = new TableColumn<>("Apellido");
        colApellido.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getApellido()));

        TableColumn<Empleado, String> colTelefono = new TableColumn<>("Teléfono");
        colTelefono.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getTelefono()));

        TableColumn<Empleado, String> colEmail = new TableColumn<>("Email");
        colEmail.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().getEmail()));

        tableView.getColumns().addAll(colNombre, colApellido, colTelefono, colEmail);

        // Botones
        Button btnAgregar = new Button("Agregar Empleado");
        btnAgregar.setOnAction(e -> abrirFormularioAgregar());

        Button btnModificar = new Button("Modificar Empleado");
        btnModificar.setOnAction(e -> abrirFormularioModificar());

        Button btnEliminar = new Button("Eliminar Empleado");
        btnEliminar.setOnAction(e -> eliminarEmpleado());

        Button btnVerResumen = new Button("Ver Resumen");
        btnVerResumen.setOnAction(e -> {
            Empleado empleadoSeleccionado = tableView.getSelectionModel().getSelectedItem();
            if (empleadoSeleccionado != null) {
                ResumenEmpleadoWindow resumenWindow = new ResumenEmpleadoWindow();
                resumenWindow.start(new Stage(), empleadoSeleccionado);
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Seleccionar empleado");
                alert.setHeaderText(null);
                alert.setContentText("Por favor, selecciona un empleado para ver su resumen.");
                alert.showAndWait();
            }
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(tableView, btnAgregar, btnModificar, btnEliminar, btnVerResumen);

        Scene scene = new Scene(layout, 600, 400);
        stage.setScene(scene);
        stage.show();

        cargarEmpleados();
    }

    private void cargarEmpleados() {
        List<Empleado> lista = EmpleadoDAO.obtenerEmpleados();
        empleados.setAll(lista);
    }

    private void abrirFormularioAgregar() {
        Stage form = new Stage();
        form.setTitle("Nuevo Empleado");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtApellido = new TextField();
        txtApellido.setPromptText("Apellido");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        Spinner<Integer> spinnerPorcentaje = new Spinner<>(10, 100, 50);
        spinnerPorcentaje.setEditable(true);

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            Empleado nuevo = new Empleado(0,
                    txtNombre.getText(),
                    txtApellido.getText(),
                    txtTelefono.getText(),
                    txtEmail.getText(),
                    spinnerPorcentaje.getValue()
            );
            EmpleadoDAO.agregarEmpleado(nuevo);
            cargarEmpleados();
            form.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(txtNombre, txtApellido, txtTelefono, txtEmail, new Label("Porcentaje de Ganancia (%)"), spinnerPorcentaje, btnGuardar);

        Scene scene = new Scene(layout, 300, 300);
        form.setScene(scene);
        form.show();
    }

    private void abrirFormularioModificar() {
        Empleado seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccioná un empleado para modificar.");
            return;
        }

        Stage form = new Stage();
        form.setTitle("Modificar Empleado");

        TextField txtNombre = new TextField(seleccionado.getNombre());
        TextField txtApellido = new TextField(seleccionado.getApellido());
        TextField txtTelefono = new TextField(seleccionado.getTelefono());
        TextField txtEmail = new TextField(seleccionado.getEmail());

        Spinner<Integer> spinnerPorcentaje = new Spinner<>(10, 100, seleccionado.getPorcentajeGanancia());
        spinnerPorcentaje.setEditable(true);

        Button btnGuardar = new Button("Guardar Cambios");
        btnGuardar.setOnAction(e -> {
            seleccionado.setNombre(txtNombre.getText());
            seleccionado.setApellido(txtApellido.getText());
            seleccionado.setTelefono(txtTelefono.getText());
            seleccionado.setEmail(txtEmail.getText());
            seleccionado.setPorcentajeGanancia(spinnerPorcentaje.getValue());
            EmpleadoDAO.actualizarEmpleado(seleccionado);
            cargarEmpleados();
            form.close();
        });

        VBox layout = new VBox(10);
        layout.getChildren().addAll(txtNombre, txtApellido, txtTelefono, txtEmail, new Label("Porcentaje de Ganancia (%)"), spinnerPorcentaje, btnGuardar);

        Scene scene = new Scene(layout, 300, 300);
        form.setScene(scene);
        form.show();
    }

    private void eliminarEmpleado() {
        Empleado seleccionado = tableView.getSelectionModel().getSelectedItem();
        if (seleccionado == null) {
            mostrarAlerta("Seleccioná un empleado para eliminar.");
            return;
        }

        Alert confirm = new Alert(Alert.AlertType.CONFIRMATION);
        confirm.setContentText("¿Estás seguro de que querés eliminar este empleado?");
        confirm.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                EmpleadoDAO.eliminarEmpleado(seleccionado.getId());
                cargarEmpleados();
            }
        });
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setContentText(mensaje);
        alert.setHeaderText(null);
        alert.showAndWait();
    }
}
