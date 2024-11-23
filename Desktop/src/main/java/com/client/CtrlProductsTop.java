package com.client;

import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.text.Text;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.util.Base64;

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
        CtrlLogin.wsClient.onMessage(this::onMessageReceived);
        getInfo();
    }

    public void go_back(MouseEvent mouseEvent) {
        UtilsViews.setView("MainView");
    }

    public void getInfo() {
        ObservableList<String> topProducts = getTopProducts();
        if (topProducts != null && !topProducts.isEmpty()) {
            // Solicitar los productos con la imagen en Base64
            sendRequest("productswithimage");
        }
    }

    private void sendRequest(String type) {
        JSONObject message = new JSONObject();
        message.put("type", type);
        message.put("message", type);
        CtrlLogin.wsClient.safeSend(message.toString());
    }

    private void onMessageReceived(String message) {
        // El mensaje debe estar en formato JSON
        JSONObject msg = new JSONObject(message);
        if ("productswithimage".equals(msg.getString("type"))) {
            // Obtener los productos con imagenes
            JSONArray products = msg.getJSONArray("message");

            // Actualizar la UI con la información de los productos
            updateProductUI(products);
        }
    }

    private void updateProductUI(JSONArray products) {
        // Asumimos que siempre habrá al menos 5 productos
        for (int i = 0; i < 5 && i < products.length(); i++) {
            JSONObject product = products.getJSONObject(i);

            // Extraer la información del producto
            String name = product.getString("name");
            String description = product.getString("description");
            String price = product.getString("price");

            // Establecer los valores en los Texts
            switch (i) {
                case 0:
                    name1.setText(name);
                    description1.setText(description);
                    price1.setText(price+"€");
                    loadImageToView(img1, product.getString("image"));
                    break;
                case 1:
                    name2.setText(name);
                    description2.setText(description);
                    price2.setText(price+"€");
                    loadImageToView(img2, product.getString("image"));
                    break;
                case 2:
                    name3.setText(name);
                    description3.setText(description);
                    price3.setText(price+"€");
                    loadImageToView(img3, product.getString("image"));
                    break;
                case 3:
                    name4.setText(name);
                    description4.setText(description);
                    price4.setText(price+"€");
                    loadImageToView(img4, product.getString("image"));
                    break;
                case 4:
                    name5.setText(name);
                    description5.setText(description);
                    price5.setText(price+"€");
                    loadImageToView(img5, product.getString("image"));
                    break;
            }
        }
    }

    // Cargar la imagen Base64 al ImageView
    private void loadImageToView(ImageView imageView, String base64Image) {
        try {
            base64Image = base64Image.replaceAll("\\s", "");
            byte[] imageBytes = Base64.getDecoder().decode(base64Image);
            Image image = new Image(new ByteArrayInputStream(imageBytes));
            imageView.setImage(image);
        } catch (IllegalArgumentException e) {
            System.err.println("Error decoding Base64 image: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("Error loading image: " + e.getMessage());
            e.printStackTrace();
        }
    }

}
