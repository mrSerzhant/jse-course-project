package scenecontrollers;
import connection.Connection;
import data.datamain.User;
import data.dataprocessing.JsonReader;
import data.dataprocessing.JsonWriter;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class LoginController {
    private Connection connection;
    private User user;
    private static LoginController controller;

    public LoginController() {
        controller = this;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TextField loginField;

    @FXML
    private TextField passwordField;

    @FXML
    void initialize() {
        connection = new Connection();
    }

    @FXML
    private void goLogin(ActionEvent event) throws IOException {
        String name = loginField.getText().replaceAll("\\s+", "");
        String password = passwordField.getText().replaceAll("\\s+", "");
        Alert alertBox = new Alert(Alert.AlertType.WARNING);

        if (name.isEmpty() || password.isEmpty()) {
            alertBox.setTitle("Внимание");
            alertBox.setHeaderText("Необходимо заполнить поля");
            alertBox.show();
        } else {
            JsonWriter jsonWriter = new JsonWriter();
            String responseString = connection.getUserData(jsonWriter.writeStringUserDateToServer(new User(name, password)));

            if (responseString.equals("Not found")) {
                alertBox.setTitle("Внимание");
                alertBox.setHeaderText("Неверный логин или пароль");
                alertBox.show();
            } else {
                JsonReader jsonReader = new JsonReader();
                user = jsonReader.doUserBean(responseString);
                Parent parent = FXMLLoader.load(getClass().getResource("../fxmlscenes/mainClientScene.fxml"));
                Scene mainScene = new Scene(parent);
                Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.setScene(mainScene);
                stage.show();
            }
        }
    }

    @FXML
    private void goRegistration(ActionEvent event) throws IOException {
        String name = loginField.getText().replaceAll("\\s+", "");
        String password = passwordField.getText().replaceAll("\\s+", "");
        Alert alertBox = new Alert(Alert.AlertType.WARNING);

        String passwordLower = passwordField.getText();

        if (name.isEmpty() || password.isEmpty()) {
            alertBox.setTitle("Внимание");
            alertBox.setHeaderText("Необходимо заполнить поля");
            alertBox.show();
        } else if (password.equals(passwordLower.toLowerCase())) {
            alertBox.setTitle("Внимание");
            alertBox.setHeaderText("Неподходящий пароль");
            alertBox.setContentText("Пароль должен содержать заглавную букву");
            alertBox.show();
        } else {
            JsonWriter jsonWriter = new JsonWriter();
            String responseString = connection.sendUserJson(jsonWriter.writeStringUserDateToServer(new User(name, password)));

            if (responseString.equals("Success")) {
                Alert alertBoxSuccess = new Alert(Alert.AlertType.INFORMATION);
                alertBoxSuccess.setTitle("Успех");
                alertBoxSuccess.setHeaderText("Регистрация прошла успешно");
                alertBoxSuccess.show();
            } else {
                alertBox.setTitle("Внимание");
                alertBox.setHeaderText("Логин занят");
                alertBox.show();
            }
        }
    }

    public static LoginController getController() {
        return controller;
    }

    public User getUser() {
        return user;
    }
}
