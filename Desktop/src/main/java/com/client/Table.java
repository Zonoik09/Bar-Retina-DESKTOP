package com.client;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Table {

    private final StringProperty table;
    private final StringProperty idOrder;
    private final StringProperty waiter;
    private final BooleanProperty paid;
    private final BooleanProperty free;
    private static int instanceCounter = 0;

    public Table(String table, String idOrder, String waiter, boolean paid, boolean free) {
        // Incrementar contador de instancias
        instanceCounter++;

        // Lógica para establecer valores predeterminados
        boolean defaultPaid = (instanceCounter == 1) ? false : paid; // false para el primero
        boolean defaultFree = (instanceCounter == 1) ? true : free;  // true para el primero

        this.table = new SimpleStringProperty(table != null ? table : "Unknown");
        this.idOrder = new SimpleStringProperty(idOrder != null ? idOrder : "N/A");
        this.waiter = new SimpleStringProperty(waiter != null ? waiter : "N/A");
        this.paid = new SimpleBooleanProperty(defaultPaid);
        this.free = new SimpleBooleanProperty(defaultFree);
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

    public Boolean getPaid() {
        return paid.get();
    }

    public void setPaid(Boolean paid) {
        this.paid.set(paid);
    }

    public BooleanProperty paidProperty() {
        return paid;
    }

    public Boolean getFree() {
        return free.get();
    }

    public void setFree(Boolean free) {
        this.free.set(free);
    }

    public BooleanProperty freeProperty() {
        return free;
    }
}
