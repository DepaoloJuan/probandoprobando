package com.centroestetico.turnos;

import com.centroestetico.clientes.Cliente;
import com.centroestetico.database.ClienteDAO;
import com.centroestetico.database.EmpleadoDAO;
import com.centroestetico.database.ServicioDAO;
import com.centroestetico.database.TurnoDAO;
import com.centroestetico.empleados.Empleado;
import com.centroestetico.servicios.ServicioBase;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.*;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

public class AsignarServicioWindow {

    private final ObservableList<Cliente> clientes = FXCollections.observableArrayList();
    private final ObservableList<Empleado> empleados = FXCollections.observableArrayList();
    private final ObservableList<ServicioBase> serviciosBase = FXCollections.observableArrayList();
    private final ObservableList<ServicioAsignado> serviciosAsignados = FXCollections.observableArrayList();

    private LocalDate fechaSeleccionada;
    private LocalTime horaSeleccionada;
    private Empleado empleadoSeleccionado;
    private Runnable refrescarCallback;

    public void start(Stage stage,
                      LocalDate fecha,
                      LocalTime hora,
                      Empleado empleado,
                      Runnable refrescarCallback) {

        this.fechaSeleccionada = fecha;
        this.horaSeleccionada = hora;
        this.empleadoSeleccionado = empleado;
        this.refrescarCallback = refrescarCallback;

        stage.setTitle("Asignar Servicios al Cliente");

        // Cliente
        ComboBox<Cliente> comboCliente = new ComboBox<>();
        comboCliente.setPromptText("Seleccionar cliente existente");
        clientes.setAll(ClienteDAO.obtenerClientes());
        comboCliente.setItems(clientes);

        Button btnNuevoCliente = new Button("➕ Nuevo Cliente");
        btnNuevoCliente.setOnAction(e -> mostrarDialogoNuevoCliente(comboCliente));

        // Tabla
        TableView<ServicioAsignado> tableView = new TableView<>(serviciosAsignados);
        TableColumn<ServicioAsignado, String> colServicio = new TableColumn<>("Servicio");
        colServicio.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().servicio.getDescripcion()));
        TableColumn<ServicioAsignado, String> colEmpleado = new TableColumn<>("Empleado");
        colEmpleado.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().empleado.toString()));
        TableColumn<ServicioAsignado, String> colHora = new TableColumn<>("Hora");
        colHora.setCellValueFactory(cell -> new SimpleStringProperty(cell.getValue().hora.toString()));
        tableView.getColumns().addAll(colServicio, colEmpleado, colHora);

        // Agregar Servicio
        ComboBox<ServicioBase> comboServicio = new ComboBox<>();
        comboServicio.setPromptText("Servicio");
        serviciosBase.setAll(ServicioDAO.obtenerServiciosBase());
        comboServicio.setItems(serviciosBase);

        ComboBox<Empleado> comboEmpleado = new ComboBox<>();
        comboEmpleado.setPromptText("Empleado");
        empleados.setAll(EmpleadoDAO.obtenerEmpleados());
        comboEmpleado.setItems(empleados);

        Spinner<LocalTime> spinnerHora = new Spinner<>();
        ObservableList<LocalTime> horasDisponibles = FXCollections.observableArrayList(generarHorasTurno());
        SpinnerValueFactory<LocalTime> valueFactory = new SpinnerValueFactory.ListSpinnerValueFactory<>(horasDisponibles);
        spinnerHora.setValueFactory(valueFactory);
        valueFactory.setValue(horasDisponibles.get(0));
        spinnerHora.setEditable(true);

        valueFactory.setConverter(new StringConverter<>() {
            @Override public String toString(LocalTime time) {
                return (time != null) ? time.toString() : "";
            }
            @Override public LocalTime fromString(String text) {
                try {
                    return LocalTime.parse(text);
                } catch (Exception e) {
                    return valueFactory.getValue();
                }
            }
        });

        Button btnAgregarServicio = new Button("Agregar Servicio");
        btnAgregarServicio.setOnAction(e -> {
            ServicioBase servicio = comboServicio.getValue();
            Empleado empleadoSel = comboEmpleado.getValue();
            spinnerHora.increment(0);
            LocalTime horaSel = spinnerHora.getValue();

            if (servicio != null && empleadoSel != null && horaSel != null) {
                serviciosAsignados.add(new ServicioAsignado(servicio, empleadoSel, horaSel));
                comboServicio.getSelectionModel().clearSelection();
                comboEmpleado.getSelectionModel().clearSelection();
                spinnerHora.getValueFactory().setValue(horasDisponibles.get(0));
            } else {
                mostrarAlerta("Faltan datos: servicio, empleado u hora.");
            }
        });

        HBox filaCarga = new HBox(10, comboServicio, comboEmpleado, spinnerHora, btnAgregarServicio);

        // Fecha y estado
        DatePicker datePicker = new DatePicker(LocalDate.now());
        ComboBox<String> comboEstado = new ComboBox<>();
        comboEstado.getItems().addAll("Pagado", "Adeuda");
        comboEstado.setPromptText("Estado de pago");

        // Confirmar Turno
        Button btnConfirmar = new Button("✅ Confirmar Turno");
        btnConfirmar.setOnAction(e -> {
            Cliente clienteSeleccionado = comboCliente.getValue();
            LocalDate fechaTurno = datePicker.getValue();
            String estado = comboEstado.getValue();

            if (clienteSeleccionado == null || fechaTurno == null || estado == null || serviciosAsignados.isEmpty()) {
                mostrarAlerta("Completa todos los campos antes de confirmar.");
                return;
            }

            for (ServicioAsignado asignado : serviciosAsignados) {
                TurnoDAO.agregarTurno(
                        clienteSeleccionado.getId(),
                        asignado.servicio.getId(),
                        asignado.empleado.getId(),
                        fechaTurno,
                        asignado.hora,
                        asignado.servicio.getPrecio(),
                        estado
                );
            }

            if (refrescarCallback != null) refrescarCallback.run();

            mostrarAlerta("✅ Turno asignado de manera correcta.");
            stage.close();
        });

        VBox layout = new VBox(12,
                new HBox(10, comboCliente, btnNuevoCliente),
                new Label("Servicios a realizar:"),
                filaCarga,
                tableView,
                new HBox(10, new Label("Fecha:"), datePicker, comboEstado),
                btnConfirmar
        );
        layout.setPadding(new Insets(15));

        Scene scene = new Scene(layout, 800, 600);
        stage.setScene(scene);
        stage.show();
    }

    private List<LocalTime> generarHorasTurno() {
        List<LocalTime> horas = new ArrayList<>();
        for (int h = 8; h <= 20; h++) {
            horas.add(LocalTime.of(h, 0));
            horas.add(LocalTime.of(h, 30));
        }
        return horas;
    }

    private void mostrarDialogoNuevoCliente(ComboBox<Cliente> comboCliente) {
        Stage dialogo = new Stage();
        dialogo.setTitle("Nuevo Cliente");

        TextField txtNombre = new TextField();
        txtNombre.setPromptText("Nombre");

        TextField txtApellido = new TextField();
        txtApellido.setPromptText("Apellido");

        TextField txtTelefono = new TextField();
        txtTelefono.setPromptText("Teléfono");

        TextField txtEmail = new TextField();
        txtEmail.setPromptText("Email");

        Button btnGuardar = new Button("Guardar");
        btnGuardar.setOnAction(e -> {
            Cliente nuevo = new Cliente(0, txtNombre.getText(), txtApellido.getText(), txtTelefono.getText(), txtEmail.getText());
            ClienteDAO.agregarCliente(nuevo);
            ObservableList<Cliente> nuevosClientes = FXCollections.observableArrayList(ClienteDAO.obtenerClientes());
            comboCliente.setItems(nuevosClientes);
            comboCliente.getSelectionModel().selectLast();
            dialogo.close();
        });

        VBox layout = new VBox(10, txtNombre, txtApellido, txtTelefono, txtEmail, btnGuardar);
        layout.setPadding(new Insets(15));
        dialogo.setScene(new Scene(layout, 300, 250));
        dialogo.show();
    }

    private void mostrarAlerta(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(null);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    private static class ServicioAsignado {
        ServicioBase servicio;
        Empleado empleado;
        LocalTime hora;

        public ServicioAsignado(ServicioBase servicio, Empleado empleado, LocalTime hora) {
            this.servicio = servicio;
            this.empleado = empleado;
            this.hora = hora;
        }
    }
}
