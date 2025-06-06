package com.centroestetico;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.layout.StackPane;

public class MainWindowController {

    @FXML
    private StackPane contentArea;

    @FXML
    private void initialize() {
        contentArea.getScene()
                .getRoot()
                .getProperties()
                .put("fx:controller", this);

        showInicio();
    }

    @FXML
    public void showInicio() {
        try {
            FXMLLoader fx = new FXMLLoader(getClass().getResource("/com/centroestetico/ui/agenda/AgendaInicioView.fxml"));
            Node root = fx.load();
            contentArea.getChildren().setAll(root);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void showClientes() {
        setView("/com/centroestetico/ui/clientes/ClientesView.fxml");
    }

    @FXML
    public void showEmpleados() {
        setView("/com/centroestetico/ui/empleados/EmpleadosView.fxml");
    }

    @FXML
    public void showServicios() {
        setView("/com/centroestetico/ui/servicios/ServiciosView.fxml");
    }

    @FXML
    public void showAgenda() {
        setView("/com/centroestetico/ui/agenda/DayCalendarView.fxml");
    }

    public void volverMenu() {
        showInicio();
    }

    private void setView(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Node view = loader.load();
            contentArea.getChildren().setAll(view);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
