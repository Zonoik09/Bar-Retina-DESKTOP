package com.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

import java.util.List;

import static com.client.DatabaseManager.getOrder;
import static com.client.DatabaseManager.getOrders;

public class CtrlOrders {

    @FXML
    public ListView<String> listViewOrders;
    @FXML
    public ListView<String> listviewOrder;
    @FXML
    public Text totalToPay;
    @FXML
    public ImageView back_arrow;
    public static CtrlOrders instance;
    @FXML
    public Text setWaiterText;
    @FXML
    public Text setDateText;
    @FXML
    public Text setStateText;

    public void initialize() {
        instance = this;
        addListView();
        listViewOrders.setOnMouseClicked(event -> {
            viewDetallOrder(event);
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


    public static void addListView() {
        // Obtener el texto de las órdenes
        ObservableList<String> items = getOrders();
        instance.listViewOrders.setItems(items);
    }


    public static void viewDetallOrder(MouseEvent mouseEvent) {
        try {
            String selectedItem = instance.listViewOrders.getSelectionModel().getSelectedItem();
            if (selectedItem != null) {
                String[] parts = selectedItem.split(":");
                parts[1] = parts[1].trim();
                String[] tableid = parts[0].split(" ");
                String[] orderid = parts[1].split(" ");
                getOrder(Integer.parseInt(tableid[1]), orderid[1]);
            } else {
                System.err.println("No item selected");
            }
        } catch (Exception e) {
            System.err.println("Error processing selected item: " + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void setOrder(Orders order) {
        instance.setWaiterText.setText(order.getWaiter());
        instance.setStateText.setText(order.getStateOrder());
        instance.setDateText.setText((order.getDate()+" "+order.getHour()));
        instance.listviewOrder.getItems().clear();
        ObservableList<String> items = instance.listviewOrder.getItems();
        List<String> products = order.getProducts();
        List<Float> prices = order.getPrices();
        for (int i = 0; i < products.size(); i++) {
            String product = products.get(i);
            Float price = prices.get(i);
            String itemText = String.format("%s : %.2f€", product, price); // Formato con dos decimales
            items.add(itemText);
        }

        instance.listviewOrder.setItems(items);
    }

}
