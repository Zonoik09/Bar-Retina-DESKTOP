package com.client;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.sql.SQLException;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

public class CtrlOrders {

    @FXML
    public ListView<String> listViewOrders;
    @FXML
    public ListView<String> listviewOrder;
    @FXML
    public Text totalToPay;
    @FXML
    public ImageView back_arrow;

    public void initialize() {
        addListView("Table 20", "Order 005");
        addListView("Table 13", "Order 008");
        listViewOrders.getSelectionModel().selectedItemProperty().addListener((observable, oldValue, newValue) -> {
        });
    }

    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }

//    public void DeleteButton(ActionEvent actionEvent) {
//        // Obtener el elemento seleccionado
//        String selectedItem = listViewOrders.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            // Eliminar el elemento seleccionado de la lista
//            listViewOrders.getItems().remove(selectedItem);
//            Main.showAlert("ALERT","Removed: "+selectedItem);
//
//            // Deshabilitar los botones después de eliminar el elemento
//            deleteButton.setDisable(true);
//            payButton.setDisable(true);
//        }
//    }
//
//
//    public void FinalizeAndPay(ActionEvent actionEvent) {
//        String selectedItem = listViewOrders.getSelectionModel().getSelectedItem();
//        if (selectedItem != null) {
//            listViewOrders.getItems().remove(selectedItem);
//            Main.showAlert("INFORMATION","Paid and removed: "+selectedItem);
//
//            // Deshabilitar los botones después de procesar el pago y eliminar el elemento
//            deleteButton.setDisable(true);
//            payButton.setDisable(true);
//        }
//    }


    public void addListView(String table, String comanda) {

        // Formatear la hora actual
//        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
//        String horas = hora.format(formatter);

        // Crear el texto a mostrar en el ListView
        String itemText = String.format("%s : %s", table, comanda);

        ObservableList<String> items = listViewOrders.getItems();
        items.add(itemText);
        listViewOrders.setItems(items);
    }

    public void viewDetallOrder(MouseEvent mouseEvent) throws SQLException {
        DatabaseManager dbm = new DatabaseManager();
        CtrlTables ctb = new CtrlTables();
        String selectedItem = listViewOrders.getSelectionModel().getSelectedItem();
        String[] parts = selectedItem.split(":");
        parts[1] = parts[1].trim();
        String[] tableid = parts[0].split(" ");
        String[] orderid = parts[1].split(" ");
        dbm.getDetailedOrder(Integer.parseInt(tableid[1]),orderid[1]);
    }

}
