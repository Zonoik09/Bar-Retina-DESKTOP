    package com.client;

    import javafx.application.Application;
    import javafx.scene.Scene;
    import javafx.scene.layout.StackPane;
    import javafx.stage.Stage;

    import java.io.File;

    public class Main extends Application {
        File file = new File(System.getProperty("user.dir") + "/data/CONFIG.xml");
        @Override
        public void start(Stage primaryStage) throws Exception {
            // Configura el parentContainer en la clase UtilsViews
            UtilsViews.parentContainer = new StackPane();

            // Carga la vista de inicio de sesión (view_Login.fxml)
            if (file.exists()) {
                UtilsViews.addView(getClass(), "loginView", "/view_Main.fxml");
            } else {
                UtilsViews.addView(getClass(), "loginView", "/view_Login.fxml");
            }


            // Establece el contenedor como la escena principal
            Scene scene = new Scene(UtilsViews.parentContainer, 1280, 720);
            primaryStage.setScene(scene);
            primaryStage.show();
        }

        public static void main(String[] args) {
            launch(args);
        }
    }
