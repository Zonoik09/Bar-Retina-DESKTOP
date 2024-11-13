    package com.client;

    import javafx.application.Application;
    import javafx.scene.Scene;
    import javafx.scene.control.Alert;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;
    import org.w3c.dom.Document;
    import org.w3c.dom.Element;
    import org.w3c.dom.Node;
    import org.w3c.dom.NodeList;

    import javax.xml.parsers.DocumentBuilder;
    import javax.xml.parsers.DocumentBuilderFactory;
    import javax.xml.xpath.XPath;
    import javax.xml.xpath.XPathConstants;
    import javax.xml.xpath.XPathExpression;
    import javax.xml.xpath.XPathFactory;
    import java.io.File;

    import static com.client.CtrlLogin.connectToServer;

    public class Main extends Application {
        public static File file = new File(System.getProperty("user.dir") + "/data/CONFIG.xml");
        @Override
        public void start(Stage primaryStage) throws Exception {
            UtilsViews.parentContainer = new StackPane();
            if (file.exists()) {
                String url = getURL();
                System.out.println(url);
                connectToServer(url);
                UtilsViews.addView(getClass(), "MainView", "/view_Main.fxml");
            } else {
                UtilsViews.addView(getClass(), "loginView", "/view_Login.fxml");
            }
            Scene scene = new Scene(UtilsViews.parentContainer, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        // Metodo para mostrar alertas
        public static void showAlert(String title, String message) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle(title);
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();
        }

        private String getURL() {
            try {
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(file);
                XPathFactory xPathFactory = XPathFactory.newInstance();
                XPath xPath = xPathFactory.newXPath();

                // Expresión XPath para seleccionar el elemento URL
                XPathExpression expr = xPath.compile("/login/URL");

                // Evaluar la expresión XPath y obtener el valor del nodo
                String url = (String) expr.evaluate(doc, XPathConstants.STRING);
                return url;
            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }

        public static void main(String[] args) {
            launch(args);
        }

    }
