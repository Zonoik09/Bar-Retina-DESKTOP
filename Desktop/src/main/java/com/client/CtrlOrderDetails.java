package com.client;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

public class CtrlOrderDetails {

    @FXML
    private Label tableNameLabel;
    @FXML
    private Label orderIdLabel;
    @FXML
    private Label waiterLabel;
    @FXML
    private Label paidLabel;


    public void setOrderDetails(Orders order) {
        tableNameLabel.setText("Mesa: " + order.getTableID());
        orderIdLabel.setText("Orden ID: " + order.getOrderID());
        waiterLabel.setText("Mesero: " + order.getWaiter());
        boolean paid = false;
        if (order.getState().equals("complete")) {
            paid = true;
        }
        paidLabel.setText("Pagado: " + (paid ? "Sí" : "No"));
    }
}
