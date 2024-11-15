package com.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

public class CtrlTables {

    @FXML
    private TableView<Table> tableView;

    @FXML
    private TableColumn<Table, String> tableColumn;
    @FXML
    private TableColumn<Table, String> idOrderColumn;
    @FXML
    private TableColumn<Table, String> waiterColumn;
    @FXML
    private TableColumn<Table, String> paidColumn;
    @FXML
    private TableColumn<Table, String> freeColumn;

    private ObservableList<Table> tableList = FXCollections.observableArrayList();

    public void initialize() {
        tableView.setItems(tableList);
        generateTables();
        setupTableColumns();
    }

    private void setupTableColumns() {
        // Configuración de columnas
        tableColumn.setCellValueFactory(new PropertyValueFactory<>("table"));
        idOrderColumn.setCellValueFactory(new PropertyValueFactory<>("idOrder"));
        waiterColumn.setCellValueFactory(new PropertyValueFactory<>("waiter"));
        paidColumn.setCellValueFactory(new PropertyValueFactory<>("paid"));
        freeColumn.setCellValueFactory(new PropertyValueFactory<>("free"));

        // Hacemos que las columnas editables
        idOrderColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        waiterColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        paidColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        freeColumn.setCellFactory(TextFieldTableCell.forTableColumn());

        // Configuración de edición
        idOrderColumn.setOnEditCommit(event -> event.getRowValue().setIdOrder(event.getNewValue()));
        waiterColumn.setOnEditCommit(event -> event.getRowValue().setWaiter(event.getNewValue()));
        paidColumn.setOnEditCommit(event -> event.getRowValue().setPaid(event.getNewValue()));
        freeColumn.setOnEditCommit(event -> event.getRowValue().setFree(event.getNewValue()));

        tableView.setEditable(true);
    }

    private void generateTables() {
        for (int i = 1; i <= 20; i++) {
            tableList.add(new Table("Table " + i, "", "", "", ""));
        }
    }

    @FXML
    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }
}
