package com.client;

import java.sql.*;

import static com.client.CtrlTables.tableList;

public class DatabaseManager {
    private static final String DB_URL = "jdbc:mysql://localhost:3307/barretina2"; // El puerto 3307 es por mi pc personal
    private static final String DB_USER = "Admin";
    private static final String DB_PASSWORD = "BarRetina2*";

    public void getOrder(int tableid, String orderid) throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String SelectOrder = "SELECT * FROM barretina2.BarRetina2 WHERE tableid = ? AND orderid = ?";

        try (PreparedStatement stmt = conn.prepareStatement(SelectOrder)) {
            stmt.setInt(1, tableid);
            stmt.setString(2, orderid);
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int tableID = rs.getInt("tableid");
                    String waiter = rs.getString("waiter");
                    String orderID = rs.getString("orderid");
                    String products = rs.getString("products");
                    String state = rs.getString("state");
                    Boolean paid = false;
                    if (state.equals("complete")) {
                        paid = true;
                    }

                }

            }
        }
    }

    public void getTables() throws SQLException {
        Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        String selectQuery = "SELECT * FROM barretina2.BarRetina2 WHERE LOWER(state) != ?";

        try (PreparedStatement stmt = conn.prepareStatement(selectQuery)) {
            stmt.setString(1, "complete".toLowerCase());
            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    int tableID = rs.getInt("tableid");
                    String waiter = rs.getString("waiter");
                    String orderID = rs.getString("orderid");
                    String state = rs.getString("state");
                    boolean paid = state.equalsIgnoreCase("complete");

                    boolean tableExists = false;
                    for (Table table : CtrlTables.tableList) {
                        if (table.getTable().equals("Table " + tableID)) {
                            table.setIdOrder(orderID);
                            table.setWaiter(waiter);
                            table.setPaid(paid);
                            table.setFree(false);
                            tableExists = true;
                            break;
                        }
                    }
                    if (!tableExists) {
                        CtrlTables.tableList.add(new Table("Table " + tableID, orderID, waiter, paid, false));
                    }
                }
            }
        }
    }



}
