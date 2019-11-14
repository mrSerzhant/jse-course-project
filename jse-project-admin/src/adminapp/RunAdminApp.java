package adminapp;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import java.io.IOException;

public class RunAdminApp extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    public static Stage parentWindow;

    @Override
    public void start(Stage primaryStage) throws IOException {
        parentWindow = primaryStage;
        primaryStage.setTitle("AdminPanel");
        Parent root = FXMLLoader.load(getClass().getResource("../fxmlscenes/adminMainScene.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setResizable(false);
        primaryStage.sizeToScene();
        primaryStage.show();
    }
}
