package com.client;

import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;

import java.text.DecimalFormat;
import java.util.List;

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
    private Text tableText;
    @FXML
    private Text orderText;
    @FXML
    private Text waiterName;
    private static CtrlOrderDetails instance;


    @FXML
    public void initialize() {
        instance = this;
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
            float sum = 0;
            DecimalFormat df = new DecimalFormat("0.00");
            for (int i = 0;i <= products.size()-1;i++) {

                // Crear ComboBox para el estado
                ComboBox<String> stateComboBox = new ComboBox<>();
                stateComboBox.getItems().addAll("Pending", "In Preparation", "Ready", "Paid");
                stateComboBox.setValue(states.get(i));
                stateComboBox.setStyle("-fx-font-size: 18px;");

                instance.vBoxState.getChildren().add(stateComboBox);

                System.out.println(products.get(i) + " - " + prices.get(i));
                CheckBox checkBox = new CheckBox(products.get(i));
                checkBox.setStyle("-fx-font-size: 18px; -fx-padding: 5px;");
                instance.vBoxProducts.getChildren().add(checkBox);
                Label priceText = new Label(df.format(prices.get(i))+"€");
                priceText.setStyle("-fx-font-size: 18px; -fx-padding: 5px; -fx-alignment: center-right;");
                instance.vBoxPriceProducts.getChildren().add(priceText);

                sum += prices.get(i);
            }

            instance.priceSubtotal.setText(df.format(sum) + "€");
            instance.priceTotal.setText(df.format(sum)+"€");
        } else {
            System.err.println("ERROR!");
        }
    }


    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("tablesView");
    }

}
