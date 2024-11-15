package com.client;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Table {

    private final StringProperty table;
    private final StringProperty idOrder;
    private final StringProperty waiter;
    private final StringProperty paid;
    private final StringProperty free;

    public Table(String table, String idOrder, String waiter, String paid, String free) {
        this.table = new SimpleStringProperty(table != null ? table : "Unknown");
        this.idOrder = new SimpleStringProperty(idOrder != null ? idOrder : "N/A");
        this.waiter = new SimpleStringProperty(waiter != null ? waiter : "N/A");
        this.paid = new SimpleStringProperty(paid != null ? paid : "No");
        this.free = new SimpleStringProperty(free != null ? free : "Yes");
    }

    // Getters y Setters con propiedades JavaFX

    public String getTable() {
        return table.get();
    }

    public StringProperty tableProperty() {
        return table;
    }

    public String getIdOrder() {
        return idOrder.get();
    }

    public void setIdOrder(String idOrder) {
        this.idOrder.set(idOrder);
    }

    public StringProperty idOrderProperty() {
        return idOrder;
    }

    public String getWaiter() {
        return waiter.get();
    }

    public void setWaiter(String waiter) {
        this.waiter.set(waiter);
    }

    public StringProperty waiterProperty() {
        return waiter;
    }

    public String getPaid() {
        return paid.get();
    }

    public void setPaid(String paid) {
        this.paid.set(paid);
    }

    public StringProperty paidProperty() {
        return paid;
    }

    public String getFree() {
        return free.get();
    }

    public void setFree(String free) {
        this.free.set(free);
    }

    public StringProperty freeProperty() {
        return free;
    }
}
