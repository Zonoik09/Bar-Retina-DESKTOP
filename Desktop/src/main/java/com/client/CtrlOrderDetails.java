package com.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Objects;

import static com.client.DatabaseManager.changeOrderTag;
import static com.client.DatabaseManager.changeTag;

public class CtrlOrderDetails {

    @FXML
    public Text TimeText;
    @FXML
    public VBox vBoxProducts;
    @FXML
    public VBox vBoxPriceProducts;
    @FXML
    public VBox vBoxState;
    @FXML
    public Text priceSubtotal;
    @FXML
    public Text priceTotal;
    @FXML
    public Button completeOrderButton;
    @FXML
    public Button paybutton;
    @FXML
    public Button payallbutton;
    @FXML
    private Text tableText;
    @FXML
    private Text orderText;
    @FXML
    private Text waiterName;
    private static CtrlOrderDetails instance;
    private float sum2 = 0;
    private float sum = 0;
    private float sum1 = 0;


    @FXML
    public void initialize() {
        instance = this;
        completeOrderButton.setDisable(true);
        instance.paybutton.setDisable(true);
    }


    public static void setOrderDetails(Orders order) {
        if (order != null) {
            instance.tableText.setText(String.valueOf(order.getTableID()));
            instance.orderText.setText(order.getOrderID());
            instance.waiterName.setText(order.getWaiter());
            instance.TimeText.setText(order.getHour());
            List<String> products = order.getProducts();
            List<String> states = order.getState();
            List<Float> prices = order.getPrices();
            instance.vBoxProducts.getChildren().clear();
            instance.vBoxPriceProducts.getChildren().clear();
            instance.vBoxState.getChildren().clear();
            instance.sum = 0;
            instance.sum1 = 0;
            instance.sum2 = 0;
            DecimalFormat df = new DecimalFormat("0.00");

            for (int i = 0; i <= products.size() - 1; i++) {
                // Creacion de los comboBox
                ComboBox<String> stateComboBox = new ComboBox<>();
//                stateComboBox.getItems().addAll("Pending", "In Preparation", "Ready");
                if (!Objects.equals(states.get(i), "Paid")) {
                    stateComboBox.getItems().addAll("Pending", "In Preparation", "Ready");
                } else {
                    stateComboBox.getItems().addAll("Paid");
                }
                stateComboBox.setValue(states.get(i));
                stateComboBox.setPrefWidth(190);
                stateComboBox.setMinWidth(190);
                stateComboBox.setMaxWidth(190);
                stateComboBox.setPrefHeight(25);
                stateComboBox.setMaxHeight(25);
                stateComboBox.setStyle("-fx-font-size: 17px;");
                instance.vBoxState.getChildren().add(stateComboBox);

                // Creacion de los checkBox
                CheckBox checkBox = new CheckBox(products.get(i));
                checkBox.setStyle("-fx-font-size: 18px; -fx-padding: 5px;");
                instance.vBoxProducts.getChildren().add(checkBox);
                if (Objects.equals(states.get(i),"Paid")) {
                    checkBox.setDisable(true);
                }
                Label priceText = new Label(df.format(prices.get(i)) + "€");
                priceText.setStyle("-fx-font-size: 18px; -fx-padding: 5px; -fx-alignment: center-right;");
                instance.vBoxPriceProducts.getChildren().add(priceText);

                int productIndex = i;
                float productPrice = prices.get(i);
                instance.sum += productPrice;

                if (!"Paid".equals(states.get(i))) {
                    instance.sum1 += productPrice;
                }

                // Listener para el ComboBox
                stateComboBox.valueProperty().addListener((observable, oldValue, newValue) -> {
                if ("Ready".equals(newValue)) {
                    String productName = products.get(productIndex);
                    instance.sendRequest("ready", "Product: " + productName + " is ready.");
                }
                    String productName = products.get(productIndex);
                    changeTag(Integer.parseInt(order.getOrderID()),productIndex+1,stateComboBox.getValue());
                    if ("Paid".equals(newValue)) {
                        instance.sum1 -= productPrice;
                        checkBox.setDisable(true);
                    } else if ("Paid".equals(oldValue)) {
                        instance.sum1 += productPrice;
                        checkBox.setDisable(false);
                    }
                    instance.updateSubtotal();
                    instance.checkAllPaid();
                });

                // Listener para el CheckBox
                checkBox.selectedProperty().addListener((observable, oldValue, newValue) -> {
                    if (newValue) {
                        instance.paybutton.setDisable(false);
                        instance.sum2 += productPrice;
                    } else {
                        instance.paybutton.setDisable(true);
                        instance.sum2 -= productPrice;
                    }
                    instance.updateSubtotal();
                });
            }

            instance.priceTotal.setText(df.format(instance.sum) + "€");
            instance.updateSubtotal();
        }
        instance.checkAllPaid();
    }

    // Metodo para actualizar el subtotal
    private void updateSubtotal() {
        DecimalFormat df = new DecimalFormat("0.00");
        if (sum2 == 0) {
            priceSubtotal.setText(df.format(sum1) + "€");
        } else {
            priceSubtotal.setText(df.format(sum2) + "€");
        }
    }


    private void sendRequest(String type,String msg) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("message", msg);
        CtrlLogin.wsClient.safeSend(message.toString());
    }

    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("tablesView");
    }

    public void payall(ActionEvent actionEvent) {
        for (int i = 0; i < vBoxProducts.getChildren().size(); i++) {
            if (vBoxProducts.getChildren().get(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) vBoxProducts.getChildren().get(i);
                if (!checkBox.isDisable()) {
                    checkBox.setSelected(false);
                    checkBox.setDisable(true);
                    if (i < vBoxState.getChildren().size() &&
                            vBoxState.getChildren().get(i) instanceof ComboBox) {
                        ComboBox<String> stateComboBox = (ComboBox<String>) vBoxState.getChildren().get(i);
                        stateComboBox.getItems().clear();
                        stateComboBox.getItems().add("Paid");
                        stateComboBox.setValue("Paid");
                    }
                }
            }

        }

        priceSubtotal.setText(priceTotal.getText());
        System.out.println("All products have been marked as Paid.");
    }


    public void pay(ActionEvent actionEvent) {
        for (int i = 0; i < vBoxProducts.getChildren().size(); i++) {
            if (vBoxProducts.getChildren().get(i) instanceof CheckBox) {
                CheckBox checkBox = (CheckBox) vBoxProducts.getChildren().get(i);
                if (checkBox.isSelected()) {
                    if (i < vBoxState.getChildren().size() &&
                            vBoxState.getChildren().get(i) instanceof ComboBox) {
                        ComboBox<String> stateComboBox = (ComboBox<String>) vBoxState.getChildren().get(i);

                        stateComboBox.getItems().clear();
                        stateComboBox.getItems().add("Paid");
                        stateComboBox.setValue("Paid");

                        checkBox.setSelected(false);
                        checkBox.setDisable(true);
                    }
                }
            }
        }

        priceSubtotal.setText(priceTotal.getText());
    }

    private void checkAllPaid() {
        boolean allPaid = true;
        for (int i = 0; i < vBoxState.getChildren().size(); i++) {
            ComboBox<String> comboBox = (ComboBox<String>) vBoxState.getChildren().get(i);
            if (!"Paid".equals(comboBox.getValue())) {
                allPaid = false;
                break;
            }
        }

        if (allPaid) {
            completeOrderButton.setDisable(false);
            changeOrderTag(Integer.parseInt(orderText.getText()),"paid");
            payallbutton.setDisable(true);
        } else {
            changeOrderTag(Integer.parseInt(orderText.getText()),"pending");
            payallbutton.setDisable(false);
            completeOrderButton.setDisable(true);
        }
    }
    public void completeOrder(ActionEvent actionEvent) {
        changeOrderTag(Integer.parseInt(orderText.getText()),"complete");
    }
}
