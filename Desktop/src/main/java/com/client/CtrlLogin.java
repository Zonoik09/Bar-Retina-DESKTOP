package com.client;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import org.json.JSONObject;

import static com.client.CtrlMain.updateView;
import static java.lang.System.out;

public class CtrlLogin {
    @FXML
    public ComboBox<String> comboBox = new ComboBox<>();
    @FXML
    public TextField urlText;
    @FXML
    public Button button;
    public static UtilsWS wsClient;

    @FXML
    private void loginLocation(ActionEvent actionEvent) {
        if (urlText.getText().equals("")) {
            Main.showAlert("URL not set","The URL is not set. Please enter a valid URL.");
        } else {
            createXML(comboBox.getValue(),urlText.getText());
            connectToServer(urlText.getText());
        }
    }

    public void initialize() {
        comboBox.getItems().addAll("Kitchen", "Bar");
        comboBox.setValue("Bar");
    }

    // Para crear el archivo XML de la configuracion
    private void createXML(String Location, String URL) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            Document doc = db.newDocument();
            Element elmRoot = doc.createElement("login");
            doc.appendChild(elmRoot);
            Element elmLocation = doc.createElement("location");
            Text nodeLocation = doc.createTextNode(Location);
            elmLocation.appendChild(nodeLocation);
            elmRoot.appendChild(elmLocation);
            Element elmUrl = doc.createElement("URL");
            Text nodeUrl = doc.createTextNode(URL);
            elmUrl.appendChild(nodeUrl);
            elmRoot.appendChild(elmUrl);

            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            Transformer transformer = transformerFactory.newTransformer();

            DOMSource source = new DOMSource(doc);
            File outputFile = new File(String.valueOf(Main.file));
            StreamResult result = new StreamResult(outputFile);

            transformer.transform(source, result);

            out.println("XML file saved successfully.");
        } catch (ParserConfigurationException | TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void connectToServer(String URL){
        wsClient = UtilsWS.getSharedInstance("ws://"+URL);

        wsClient.onMessage(CtrlLogin::wsMessage);
        wsClient.onError(CtrlLogin::wsError);
        wsClient.onOpen(CtrlLogin::wsOpen);
    }

    private static void wsOpen(String s) {
        Platform.runLater(() -> {
            if (!UtilsViews.getActiveView().equals("MainView")) {
                try {
                    UtilsViews.addView(CtrlLogin.class, "MainView", "/view_Main.fxml");
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                UtilsViews.setView("MainView");
            }
        });
    }


    public static void wsMessage(String response) {
        JSONObject msgObj = new JSONObject(response);
        switch (msgObj.getString("type")) {
            case "products":
                String products = msgObj.getString("message");
                updateView(products, msgObj.getString("type"));
                break;
            case "tags":
                String tags = msgObj.getString("message");
                updateView(tags, msgObj.getString("type"));
                break;
        }
    }

    private static void wsError(String response) {
        String connectionRefused = "S'ha refusat la connexió";
        Platform.runLater(() -> {
            out.println(connectionRefused);
            wsClient = null;
        });
    }

}
