package com.centroestetico.ui;

import com.centroestetico.ui.agenda.AgendaController;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;        // ← nuevo import
import javafx.scene.layout.StackPane;
import java.io.IOException;       // ← nuevo import

public class MainController {

    @FXML private StackPane contentArea;

    /* ===== Navegación ===== */
    @FXML
    public void showInicio() {
        try {
            FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/com/centroestetico/ui/agenda/AgendaInicioView.fxml"));
            Parent root = fx.load();

            com.centroestetico.agenda.AgendaInicioController aic = fx.getController();
            aic.setMainController(this);  // <-- pasamos referencia al padre

            setView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    @FXML public void showClientes()  { setView("/com/centroestetico/ui/clientes/ClientesView.fxml"); }
    @FXML public void showEmpleados() {
        try {
            FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/com/centroestetico/ui/empleados/EmpleadosView.fxml"));
            Parent root = fx.load();

            // pasamos la instancia del controlador padre al hijo
            com.centroestetico.ui.empleados.EmpleadosController ec = fx.getController();
            ec.setMainController(this);

            setView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML public void showServicios() {
        try {
            FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/com/centroestetico/ui/servicios/ServiciosView.fxml"));
            Parent root = fx.load();

            // pasa la referencia al controller hijo
            com.centroestetico.ui.servicios.ServiciosController sc = fx.getController();
            sc.setMainController(this);

            setView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /** Vista diaria de Agenda: carga FXML, pasa this al controller y lo incrusta */
    @FXML
    public void showAgenda() {
        try {
            FXMLLoader fx = new FXMLLoader(
                    getClass().getResource("/com/centroestetico/ui/agenda/AgendaView.fxml"));
            Parent root = fx.load();

            AgendaController ac = fx.getController();
            ac.setMainController(this); // PASA el controlador padre

            setView(root);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /* ===== Util ===== */
    public void setView(String fxmlPath) {
        try {
            Node view = FXMLLoader.load(getClass().getResource(fxmlPath));
            contentArea.getChildren().setAll(view);
        } catch (Exception e) { e.printStackTrace(); }
    }

    public void setView(Node node) {
        contentArea.getChildren().setAll(node);
    }

    public void volverMenu() {
        showInicio(); // o cualquier vista principal a la que querés volver
    }

}

