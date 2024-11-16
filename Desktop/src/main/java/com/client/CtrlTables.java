package com.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.MouseEvent;

import java.sql.SQLException;

public class CtrlTables {

    @FXML
    public TableView<Table> tableView;

    @FXML
    private TableColumn<Table, String> tableColumn;
    @FXML
    private TableColumn<Table, String> idOrderColumn;
    @FXML
    private TableColumn<Table, String> waiterColumn;
    @FXML
    private TableColumn<Table, Boolean> paidColumn;
    @FXML
    private TableColumn<Table, Boolean> freeColumn;

    public static ObservableList<Table> tableList = FXCollections.observableArrayList();

    public void initialize() {
        tableView.setItems(tableList);
        setupTableColumns();
        generateTables();

        DatabaseManager dbm = new DatabaseManager();
        try {
            dbm.getTables();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        tableView.setOnMouseClicked(event -> {
            detallOrderTable();
        });
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
        paidColumn.setCellFactory(CheckBoxTableCell.forTableColumn(paidColumn));
        freeColumn.setCellFactory(CheckBoxTableCell.forTableColumn(freeColumn));

        // Configuración de edición
        idOrderColumn.setOnEditCommit(event -> event.getRowValue().setIdOrder(event.getNewValue()));
        waiterColumn.setOnEditCommit(event -> event.getRowValue().setWaiter(event.getNewValue()));
        paidColumn.setCellFactory(CheckBoxTableCell.forTableColumn(paidColumn));
        freeColumn.setCellFactory(CheckBoxTableCell.forTableColumn(freeColumn));


        tableView.setEditable(true);
    }

    private void generateTables() {
        for (int i = 1; i <= 20; i++) {
            tableList.add(new Table("Table " + i, "", "", false, true));
        }
    }

    @FXML
    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }

    public void detallOrderTable() {
        Table selectedItem = tableView.getSelectionModel().getSelectedItem();
        if (selectedItem.getIdOrder().equals("")) {
            Main.showAlert("No order in progress","No order in progress");
        } else {
            try {
                UtilsViews.addView(CtrlLogin.class, "detailOrder", "/order_details.fxml");
                UtilsViews.setView("detailOrder");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }


        }
    }


}
