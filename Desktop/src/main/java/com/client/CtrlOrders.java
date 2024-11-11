package com.client;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.ListView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;

public class CtrlOrders {

    @FXML
    public ListView listViewOrders;
    @FXML
    public ListView listviewOrder;
    @FXML
    public Text totalToPay;

    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }

    public void DeleteButton(ActionEvent actionEvent) {
    }

    public void FinalizeAndPay(ActionEvent actionEvent) {
    }
}
