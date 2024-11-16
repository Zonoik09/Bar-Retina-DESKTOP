package com.client;

public class Orders {

    private int tableID;
    private String orderID;
    private String waiter;
    private String products;
    private String state;

    // Constructor de la clase Orders
    public Orders(int tableID, String orderID, String waiter, String products, String state) {
        this.tableID = tableID;
        this.orderID = orderID;
        this.waiter = waiter;
        this.products = products;
        this.state = state;
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

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }
}
