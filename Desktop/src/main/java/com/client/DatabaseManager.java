package com.client;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;

import java.io.IOException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.client.CtrlOrderDetails.setOrderDetails;
import static com.client.CtrlTables.tableList;

public class DatabaseManager {
//    private static final String DB_URL = "jdbc:mysql://localhost:3306/barretina2";
    private static final String DB_URL = "jdbc:mysql://localhost:3307/barretina2";
    private static final String DB_USER = "Admin";
    private static final String DB_PASSWORD = "BarRetina2*";

    public static void getDetailedOrder(int tableId, String orderId) {
        String selectOrderQuery = "SELECT C.id AS command_id, C.tableid AS table_id, C.waiter, C.hour, Cd.product_name, Cd.price, Cd.state FROM Command C JOIN Command_details Cd ON C.id = Cd.id_command WHERE C.tableid = ? AND C.id = ?;";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(selectOrderQuery)) {

            stmt.setInt(1, tableId);
            stmt.setString(2, orderId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<String> listProducts = new ArrayList<>();
                List<Float> listPrices = new ArrayList<>();
                List<String> listStates = new ArrayList<>();
                Orders order = null;

                while (rs.next()) {
                    int commandId = rs.getInt("command_id");
                    int tableID = rs.getInt("table_id");
                    String waiter = rs.getString("waiter");
                    String hour = rs.getString("hour");
                    String product = rs.getString("product_name");
                    float price = rs.getFloat("price");
                    String state = rs.getString("state");

                    listProducts.add(product);
                    listPrices.add(price);
                    listStates.add(state);
                    order = new Orders(tableID, orderId, waiter, listProducts, listPrices, listStates, hour);
                }
                if (order != null) {
                    setOrderDetails(order);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order details: " + e.getMessage());
        }
    }



    // Esto sirve para que aparezcan las mesas con las ordenes etc...
    public void getTables() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String selectQuery = "SELECT * FROM Command C JOIN Command_details Cd ON C.id = Cd.id_command WHERE LOWER(C.state) != ?";

        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, "complete".toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String orderID = rs.getString("id");
                    int tableID = rs.getInt("tableid");
                    String waiter = rs.getString("waiter");
                    String state = rs.getString("state");
                    boolean paid = state.equalsIgnoreCase("complete");

                    boolean tableExists = false;
                    for (Table table : CtrlTables.tableList) {
                        if (table.getTable().equals(tableID+"")) {
                            table.setIdOrder(orderID);
                            table.setWaiter(waiter);
                            table.setPaid(paid);
                            table.setFree(false);
                            tableExists = true;
                            break;
                        }
                    }
                    if (!tableExists) {
                        CtrlTables.tableList.add(new Table(tableID+"", orderID, waiter, paid, false));
                    }
                }
            }
        }
    }



}
