package com.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.layout.VBox;
import org.json.JSONObject;

public class CtrlMain {

    @FXML
    private ComboBox<String> itemBox;
    @FXML
    private ListView<String> listView;
    // Variable para rastrear el tipo de solicitud actual
    private String requestType = "";
    // Referencia estática de la instancia de CtrlMain para acceder desde métodos estáticos
    private static CtrlMain instance;

    public void initialize() {
        instance = this;
        // Inicializar ComboBox con valores de Tags y Products
        itemBox.getItems().addAll("Tags", "Products");
        itemBox.setValue("Tags");
        // Configurar evento de cambio de selección en el ComboBox
        itemBox.setOnAction(event -> comboBoxValue());
        comboBoxValue();
    }

    private void comboBoxValue() {
        String selectedValue = itemBox.getValue();
        if (selectedValue.equals("Tags")) {
            requestType = "tags";
            sendRequest("tags");
        } else if (selectedValue.equals("Products")) {
            requestType = "products";
            sendRequest("products");
        }
    }

    // Metodo para enviar solicitud al servidor
    private void sendRequest(String type) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("message", type);

        CtrlLogin.wsClient.safeSend(message.toString());
    }

    // Metodo estático para actualizar la vista ListView
    public static void updateView(String stringItems, String type) {
        Platform.runLater(() -> {
            if (instance != null) {
                ObservableList<String> items = FXCollections.observableArrayList(stringItems.toUpperCase().split("\n"));
                if (type.equals("tags")) {
                    if (!items.isEmpty()) {
                        items.remove(0);
                    }
                    instance.listView.setItems(items);
                } else if (type.equals("products")) {
                    instance.listView.setItems(items);
                }
            }
        });
    }
}
