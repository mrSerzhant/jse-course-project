package scenecontrollers;

import connection.Connection;
import data.datamain.Route;
import data.datamain.Station;
import data.datamain.Ticket;
import data.datamain.User;
import data.dataprocessing.JsonWriter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.ComboBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class ConfirmSceneController {
    private ObservableList<Station> listStations =
            FXCollections.observableArrayList(ClientMainController.getController().getRouteForSelect().getStops());
    private static ConfirmSceneController controller;
    private Connection connection;

    public ConfirmSceneController() {
        controller = this;
    }

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private ComboBox<Station> comboBoxFirst;

    @FXML
    private ComboBox<Station> comboBoxSecond;

    @FXML
    void initialize() {
        connection = new Connection();
        comboBoxFirst.setItems(listStations);
        comboBoxSecond.setItems(listStations);

        setStartValueForComboBox();
    }

    @FXML
    private void confirmBuy(ActionEvent event) throws IOException {
        boolean checkIsEmpty = false;
        boolean checkIsCorrectIndex = false;

        if (comboBoxFirst.getSelectionModel().isEmpty() || comboBoxSecond.getSelectionModel().isEmpty()) {
            checkIsEmpty = true;
        }  else if (comboBoxSecond.getSelectionModel().getSelectedIndex() <= comboBoxFirst.getSelectionModel().getSelectedIndex()) {
            checkIsCorrectIndex = true;
        }

        if (checkIsEmpty || checkIsCorrectIndex) {
            alertBoxInfo();

        } else {
            ClientMainController controller = ClientMainController.getController();
            Route route = controller.getRouteForSelect();

            int firstIndexStation = comboBoxFirst.getSelectionModel().getSelectedIndex();
            int lastIndexStation = comboBoxSecond.getSelectionModel().getSelectedIndex();

            Ticket ticket = new Ticket(route, firstIndexStation, lastIndexStation);
            User user = controller.getUser();
            user.addTicket(ticket);
            JsonWriter jsonWriter = new JsonWriter();
            connection.sendUserData(jsonWriter.writeStringUserDateToServer(user));

            alertBoxSuccess();
            ClientMainController.getController().loadSearchScene();
        }
    }

    private void alertBoxInfo() {
        Alert alertBox = new Alert(Alert.AlertType.WARNING);
        alertBox.setTitle("Внимание");
        alertBox.setHeaderText("Ошибка заполнения данных");
        alertBox.show();
    }

    private void alertBoxSuccess() {
        Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
        alertBox.setTitle("Завершено");
        alertBox.setHeaderText("Покупка завершена успешно");
        alertBox.show();
    }

    private void setStartValueForComboBox() {
        String departureFieldValue = SearchRoutesSceneController.getController().getFirstTextField().getText();
        String arrivalFieldValue = SearchRoutesSceneController.getController().getSecondTextField().getText();

        boolean foundStation;
        if (!departureFieldValue.isEmpty()) {
            foundStation = searchComboBoxValue(departureFieldValue, comboBoxFirst);
            if (!foundStation) {
                comboBoxFirst.setPromptText("Выберите станцию");
            }
        } else {
            comboBoxFirst.setPromptText("Выберите станцию");
        }

        if (!departureFieldValue.isEmpty()) {
            foundStation = searchComboBoxValue(arrivalFieldValue, comboBoxSecond);
            if (!foundStation) {
                comboBoxFirst.setPromptText("Выберите станцию");
            }
        } else {
            comboBoxSecond.setPromptText("Выберите станцию");
        }
    }

    private boolean searchComboBoxValue(String nameStation, ComboBox<Station> comboBox) {
        for (Station station : listStations) {
            if (station.getName().equals(nameStation)) {
                int indexStation = listStations.indexOf(station);
                comboBox.getSelectionModel().select(indexStation);
                return true;
            }
        }
        return false;
    }

    public static ConfirmSceneController getController() {
        return controller;
    }
}
