package com.client;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class CtrlOrders {

    @FXML
    public ListView<String> listViewOrders;
    @FXML
    public ListView<String> listviewOrder;
    @FXML
    public Text totalToPay;
    @FXML
    public ImageView back_arrow;
    @FXML
    public Button deleteButton;
    @FXML
    public Button payButton;

    public void initialize() {
        addListView("Table 23", "order 245",LocalTime.now());
        addListView("Table 13", "order 242",LocalTime.now());
        deleteButton.setDisable(true);
        payButton.setDisable(true);
        // Agregar un listener para detectar cambios en la selección
        listViewOrders.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue != null) {
                deleteButton.setDisable(false);
                payButton.setDisable(false);
            }
        });
    }

    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }

    public void DeleteButton(ActionEvent actionEvent) {
        // Obtener el elemento seleccionado
        String selectedItem = listViewOrders.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            // Eliminar el elemento seleccionado de la lista
            listViewOrders.getItems().remove(selectedItem);
            Main.showAlert("ALERT","Removed: "+selectedItem);

            // Deshabilitar los botones después de eliminar el elemento
            deleteButton.setDisable(true);
            payButton.setDisable(true);
        }
    }


    public void FinalizeAndPay(ActionEvent actionEvent) {
        String selectedItem = listViewOrders.getSelectionModel().getSelectedItem();
        if (selectedItem != null) {
            listViewOrders.getItems().remove(selectedItem);
            Main.showAlert("INFORMATION","Paid and removed: "+selectedItem);

            // Deshabilitar los botones después de procesar el pago y eliminar el elemento
            deleteButton.setDisable(true);
            payButton.setDisable(true);
        }
    }


    public void addListView(String table, String comanda, LocalTime hora) {
        // Formatear la hora actual
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String horas = hora.format(formatter);

        // Crear el texto a mostrar en el ListView
        String itemText = String.format("%s : %-30s %s", table, comanda, horas);

        // Obtener los elementos actuales del ListView y añadir el nuevo elemento
        ObservableList<String> items = listViewOrders.getItems();
        items.add(itemText);
        listViewOrders.setItems(items);
    }


}
