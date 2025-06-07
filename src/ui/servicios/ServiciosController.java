package com.centroestetico.ui.servicios;

import com.centroestetico.servicios.ServicioBase;
import com.centroestetico.database.ServicioDAO;
import com.centroestetico.ui.MainController;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.Node;

import java.util.List;

public class ServiciosController {

    @FXML private TextField txtCategoria;
    @FXML private TextField txtDescripcion;
    @FXML private TextField txtPrecio;

    @FXML private TableView<ServicioBase> tableView;
    @FXML private TableColumn<ServicioBase,String>  colCategoria;
    @FXML private TableColumn<ServicioBase,String>  colDescripcion;
    @FXML private TableColumn<ServicioBase,Double> colPrecio;

    private final ObservableList<ServicioBase> servicios = FXCollections.observableArrayList();

    private MainController main; // referencia al controller principal

    public void setMainController(MainController main) {
        this.main = main;
    }

    @FXML
    private void initialize() {
        colCategoria.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getCategoria()));
        colDescripcion.setCellValueFactory(c -> new SimpleStringProperty(c.getValue().getDescripcion()));
        colPrecio.setCellValueFactory(c -> new SimpleDoubleProperty(c.getValue().getPrecio()).asObject());

        tableView.setItems(servicios);
        tableView.setOnMouseClicked(e -> llenarFormulario());

        cargarServicios();
    }

    @FXML private void onAgregar() {
        Double precio = parsePrecio(); if (precio == null) return;
        ServicioBase nuevo = new ServicioBase(0,
                txtCategoria.getText(),
                txtDescripcion.getText(),
                precio);
        ServicioDAO.agregarServicioBase(nuevo);
        servicios.add(nuevo);
        limpiar();
    }

    @FXML private void onActualizar() {
        ServicioBase sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un servicio."); return; }
        Double precio = parsePrecio(); if (precio == null) return;

        ServicioDAO.actualizarServicioBase(sel,
                txtCategoria.getText(),
                txtDescripcion.getText(),
                precio);

        sel.setCategoria(txtCategoria.getText());
        sel.setDescripcion(txtDescripcion.getText());
        sel.setPrecio(precio);
        tableView.refresh();
        limpiar();
    }

    @FXML private void onEliminar() {
        ServicioBase sel = tableView.getSelectionModel().getSelectedItem();
        if (sel == null) { alerta("Seleccioná un servicio."); return; }

        Alert conf = new Alert(Alert.AlertType.CONFIRMATION,"¿Eliminar?");
        conf.showAndWait().ifPresent(r -> {
            if (r == ButtonType.OK) {
                ServicioDAO.eliminarServicioBase(sel);
                servicios.remove(sel);
            }
        });
    }

    @FXML
    private void volverMenu() {
        if (main != null) {
            main.volverMenu();
        }
    }

    private void cargarServicios() {
        List<ServicioBase> lista = ServicioDAO.obtenerServiciosBase();
        servicios.setAll(lista);
    }

    private void llenarFormulario() {
        ServicioBase s = tableView.getSelectionModel().getSelectedItem();
        if (s != null) {
            txtCategoria.setText(s.getCategoria());
            txtDescripcion.setText(s.getDescripcion());
            txtPrecio.setText(String.valueOf(s.getPrecio()));
        }
    }

    private void limpiar() {
        txtCategoria.clear(); txtDescripcion.clear(); txtPrecio.clear();
    }

    private Double parsePrecio() {
        try {
            double p = Double.parseDouble(txtPrecio.getText());
            if (p < 0) throw new NumberFormatException();
            return p;
        } catch (NumberFormatException ex) {
            alerta("Precio inválido.");
            return null;
        }
    }

    private void alerta(String m) {
        new Alert(Alert.AlertType.INFORMATION,m).showAndWait();
    }
}
