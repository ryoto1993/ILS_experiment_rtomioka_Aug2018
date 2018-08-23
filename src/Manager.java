import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;

public class Manager extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        // Read FXML
        URL location = getClass().getResource("UI/main_window.fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(location);

        // Generate scene graph
        Pane root = null;
        try {
            root = fxmlLoader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Generate scene
        Scene scene = new Scene(root, 800, 500);

        // Set stage
        primaryStage.setTitle("Intelligent Lighting System Controller");

        // Show window
        primaryStage.setScene(scene);
        primaryStage.show();
    }
}
