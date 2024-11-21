package com.client;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.client.CtrlOrderDetails.setOrderDetails;
import static com.client.CtrlOrders.setOrder;


public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3306/barretina2";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = ""; // borro la contra porque si es local cada uno tiene una

    public static void getDetailedOrder(int tableId, String ordersId) {
        String selectOrderQuery = "SELECT C.id AS order_id, C.tableid AS table_id, C.waiter, C.hour, Cd.product_name, Cd.price, Cd.state FROM orders C JOIN order_details Cd ON C.id = Cd.id_order WHERE C.tableid = ? AND C.id = ?;";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(selectOrderQuery)) {

            stmt.setInt(1, tableId);
            stmt.setString(2, ordersId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<String> listProducts = new ArrayList<>();
                List<Float> listPrices = new ArrayList<>();
                List<String> listStates = new ArrayList<>();
                Orders order = null;

                while (rs.next()) {
                    int tableID = rs.getInt("table_id");
                    String waiter = rs.getString("waiter");
                    String hour = rs.getString("hour");
                    String product = rs.getString("product_name");
                    float price = rs.getFloat("price");
                    String state = rs.getString("state");

                    listProducts.add(product);
                    listPrices.add(price);
                    listStates.add(state);
                    order = new Orders(tableID, ordersId, waiter, listProducts, listPrices, listStates, hour);
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

    public static void getOrder(int tableId, String ordersId) {
        String selectOrderQuery = "SELECT C.id AS order_id, C.tableid AS table_id, C.waiter, C.hour, C.day, Cd.product_name, Cd.price, C.state FROM orders C JOIN order_details Cd ON C.id = Cd.id_order WHERE C.tableid = ? AND C.id = ?;";

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(selectOrderQuery)) {

            stmt.setInt(1, tableId);
            stmt.setString(2, ordersId);

            try (ResultSet rs = stmt.executeQuery()) {
                List<String> listProducts = new ArrayList<>();
                List<Float> listPrices = new ArrayList<>();
                Orders order = null;

                while (rs.next()) {
                    int tableID = rs.getInt("table_id");
                    String waiter = rs.getString("waiter");
                    String hour = rs.getString("hour");
                    String date = rs.getString("day");
                    String product = rs.getString("product_name");
                    float price = rs.getFloat("price");
                    String state = rs.getString("state");

                    listProducts.add(product);
                    listPrices.add(price);
                    order = new Orders(tableID, ordersId, waiter, listProducts, listPrices, state, hour, date);
                }
                if (order != null) {
                    setOrder(order);
                }

            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order details: " + e.getMessage());
        }
    }

    public static ObservableList<String> getOrders() {
        String selectOrderQuery = "SELECT C.id, C.tableid FROM orders C ORDER BY C.id DESC;";
        ObservableList<String> ordersList = javafx.collections.FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(selectOrderQuery)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String orderId = rs.getString("id");
                    String tableID = rs.getString("tableid");
                    String formattedOrder = String.format("Table %s : Order %s", tableID, orderId);
                    ordersList.add(formattedOrder);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error processing ResultSet", e);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving order details: " + e.getMessage());
        }
        return ordersList;
    }

    public void getTables() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String selectQuery = "SELECT * FROM orders C JOIN order_details Cd ON C.id = Cd.id_order WHERE LOWER(C.state) != ?";

        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, "complete".toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String orderID = rs.getString("id");
                    int tableID = rs.getInt("tableid");
                    String waiter = rs.getString("waiter");
                    String state = rs.getString("state");
                    boolean paid = state.equalsIgnoreCase("paid");

                    boolean tableExists = false;
                    for (Table table : CtrlTables.tableList) {
                        if (table.getTable().equals(tableID+"")) {
                            table.setIdOrder(orderID);
                            table.setWaiter(waiter);
                            table.setPaid(paid);
                            table.setFree(false);
                            tableExists = true;
//                            break;
                        }
                    }
                    if (!tableExists) {
                        CtrlTables.tableList.add(new Table(tableID+"", orderID, waiter, paid, false));
                    }
                }
            }
        }
    }

    public static void changeTag(int orderID, int idProduct, String newState) {
        String query = "UPDATE order_details SET state = ? WHERE id_order = ? and id_products = ?";
        if (newState != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Establecer los parámetros de la consulta
                stmt.setString(1, newState);
                stmt.setInt(2, orderID);
                stmt.setInt(3, idProduct);

                // Ejecutar la consulta
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Product state updated successfully in the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error updating product state in database: " + e.getMessage());
            }
        }
    }

    public static void changeOrderTag(int orderID, String newState) {
        String query = "UPDATE orders SET state = ? WHERE id = ? ";
        if (newState != null) {
            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
                 PreparedStatement stmt = conn.prepareStatement(query)) {

                // Establecer los parámetros de la consulta
                stmt.setString(1, newState);
                stmt.setInt(2, orderID);

                // Ejecutar la consulta
                int rowsUpdated = stmt.executeUpdate();
                if (rowsUpdated > 0) {
                    System.out.println("Order state updated successfully in the database.");
                }
            } catch (SQLException e) {
                e.printStackTrace();
                System.err.println("Error updating Order state in database: " + e.getMessage());
            }
        }
    }

    public static ObservableList<String> getTopProducts() {
        String query = "SELECT product_name, COUNT(*) AS order_count " +
                "FROM order_details " +
                "GROUP BY product_name " +
                "ORDER BY order_count DESC " +
                "LIMIT 8;";
        ObservableList<String> topProductsList = FXCollections.observableArrayList();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement stmt = conn.prepareStatement(query)) {

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    String productName = rs.getString("product_name");
                    int orderCount = rs.getInt("order_count");
                    String formattedProduct = String.format("%s: %d orders", productName, orderCount);
                    topProductsList.add(formattedProduct);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error processing ResultSet", e);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving top products: " + e.getMessage());
        }
        return topProductsList;
    }

}
