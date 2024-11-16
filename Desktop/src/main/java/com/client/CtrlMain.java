package com.client;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Accordion;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListView;
import javafx.scene.control.TitledPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.tableview2.filter.filtereditor.SouthFilter;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import java.io.StringReader;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class CtrlMain {

    @FXML
    public Accordion accordion = new Accordion();
    @FXML
    private ComboBox<String> itemBox;
    @FXML
    private ListView<String> listView;
    // Variable para rastrear el tipo de solicitud actual
    private String requestType = "";
    // Referencia estática de la instancia de CtrlMain para acceder desde métodos estáticos
    private static CtrlMain instance;
    private static ArrayList<Element> productes = new ArrayList<>();


    public void initialize() {
        instance = this;
        // Inicializar ComboBox con valores de Tags y Products
        itemBox.getItems().addAll("Tags", "Products");
        itemBox.setValue("Tags");
        // Configurar evento de cambio de selección en el ComboBox
        itemBox.setOnAction(event -> comboBoxValue());
        comboBoxValue();
    }

    private void comboBoxValue() {
        String selectedValue = itemBox.getValue();
        if (selectedValue.equals("Tags")) {
            requestType = "tags";
            sendRequest("tags");
        } else if (selectedValue.equals("Products")) {
            requestType = "products";
            sendRequest("products");
        }
    }

    // Metodo para enviar solicitud al servidor
    private void sendRequest(String type) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("message", type);

        CtrlLogin.wsClient.safeSend(message.toString());
    }

    // Metodo estático para actualizar la vista ListView
    public static void updateView(String stringItems, String type) {
        Platform.runLater(() -> {
            if (instance != null) {
                ObservableList<String> items = FXCollections.observableArrayList(stringItems.toUpperCase().split("\n"));
                if (type.equals("tags")) {
                    if (!items.isEmpty()) {
                        items.remove(0);
                    }
                    instance.listView.setItems(items);
                } else if (type.equals("products")) {
                    try {
                        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                        DocumentBuilder builder = factory.newDocumentBuilder();
                        InputSource is = new InputSource(new StringReader(stringItems));
                        Document document = builder.parse(is);

                        NodeList products = document.getElementsByTagName("product");
                        for (int i = 0; i < products.getLength(); i++) {
                            Element product = (Element) products.item(i);
                            productes.add(product);
                        }

                        String stringProd = productsToString();
                        ObservableList<String> productsObservableList = FXCollections.observableArrayList(stringProd.toUpperCase().split("\n"));
                        instance.listView.setItems(productsObservableList);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private static String productsToString() {

        StringBuilder output = new StringBuilder();

        for (Element producte : productes) {
            String id = producte.getAttribute("id");
            String tags = producte.getAttribute("tags");
            NodeList nodeList0 = producte.getElementsByTagName("name");
            String name = nodeList0.item(0).getTextContent();
            NodeList nodeList1 = producte.getElementsByTagName("description");
            String description = nodeList1.item(0).getTextContent();
            NodeList nodeList2 = producte.getElementsByTagName("price");
            String price = nodeList2.item(0).getTextContent();
            NodeList nodeList3 = producte.getElementsByTagName("image");
            String image = nodeList3.item(0).getTextContent();


            output.append(name + "\n" );
        }

        return output.toString();
    }

    public void goOrders(MouseEvent mouseEvent) throws Exception {
        UtilsViews.addView(CtrlLogin.class, "OrdersView", "/view_orders.fxml");
        UtilsViews.setView("OrdersView");
    }

    public void go_tables(MouseEvent mouseEvent) throws Exception {
        UtilsViews.addView(CtrlLogin.class, "tablesView", "/view_tables.fxml");
        UtilsViews.setView("tablesView");
    }

}
