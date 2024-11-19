package com.client;

import java.util.List;

public class Orders {

    private int tableID;
    private String orderID;
    private String waiter;
    private String state;
    private List<String> states;
    private List<String> listProducts;
    private List<Float> listPrices;
    private String hour;
    private String date;

    // Constructor de la clase Orders
    public Orders(int tableID, String orderID, String waiter, List<String> listProducts, List<Float> listPrices, List<String> states, String hour) {
        this.tableID = tableID;
        this.orderID = orderID;
        this.waiter = waiter;
        this.listProducts = listProducts;
        this.listPrices = listPrices;
        this.states = states;
        this.hour = hour;
    }

    public Orders(int tableID, String orderID, String waiter, List<String> listProducts, List<Float> listPrices, String state, String hour, String date) {
        this.tableID = tableID;
        this.orderID = orderID;
        this.waiter = waiter;
        this.listProducts = listProducts;
        this.listPrices = listPrices;
        this.state = state;
        this.hour = hour;
        this.date = date;
    }

    // Getters y Setters para acceder a las propiedades
    public int getTableID() {
        return tableID;
    }

    public void setTableID(int tableID) {
        this.tableID = tableID;
    }

    public String getOrderID() {
        return orderID;
    }

    public void setOrderID(String orderID) {
        this.orderID = orderID;
    }

    public String getWaiter() {
        return waiter;
    }

    public void setWaiter(String waiter) {
        this.waiter = waiter;
    }

    public List<String> getState() {
        return states;
    }

    public void setStates(List<String> state) {
        this.states = states;
    }

    public String getHour() {
        return hour;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public List<String> getProducts() {
        return listProducts;
    }
    public void setProducts(List<String> listProducts) {
        this.listProducts = listProducts;
    }
    public List<Float> getPrices() {
        return listPrices;
    }
    public void setPrices(List<Float> listPrices) {
        this.listPrices = listPrices;
    }

    public void setStateOrder(String state) {
        this.state = state;
    }

    public String getStateOrder() {
        return state;
    }

    public String getDate() {
        return date;
    }
}
