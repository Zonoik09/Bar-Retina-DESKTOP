package com.client;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.json.JSONObject;

import java.util.jar.Attributes;

import static com.client.DatabaseManager.getTopProducts;

public class CtrlProductsTop {
    // Imagenes de los productos
    @FXML
    public ImageView img1;
    @FXML
    public ImageView img2;
    @FXML
    public ImageView img3;
    @FXML
    public ImageView img4;
    @FXML
    public ImageView img5;

    // Nombres de los productos
    @FXML
    public Text name1;
    @FXML
    public Text name2;
    @FXML
    public Text name3;
    @FXML
    public Text name4;
    @FXML
    public Text name5;

    // Descripciones de los productos
    @FXML
    public Text description1;
    @FXML
    public Text description2;
    @FXML
    public Text description3;
    @FXML
    public Text description4;
    @FXML
    public Text description5;

    // Precios de los productos
    @FXML
    public Text price1;
    @FXML
    public Text price2;
    @FXML
    public Text price3;
    @FXML
    public Text price4;
    @FXML
    public Text price5;

    public void initialize() {
        getInfo();
    }


    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }

    public void getInfo() {
        ObservableList<String> topProducts = getTopProducts();
        if (!topProducts.equals(null)) {
            for (String product : topProducts) {
                System.out.println(product);
                String[] productPart = product.split(":");
                String NameProduct = productPart[0];
                String[] quantity = productPart[1].trim().split(" ");
                System.out.println(NameProduct + " - " + quantity[0]);

                // Tengo que cargar el XML, y cuando este cargado, coger la description del producto y el price. La imagen tiene que pasarse desde el servidor al cliente como base64
            }
        }

    }

    private void sendRequest(String type,String msg) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("message", msg);
        CtrlLogin.wsClient.safeSend(message.toString());
    }

}
