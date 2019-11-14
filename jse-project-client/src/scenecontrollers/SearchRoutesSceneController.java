package scenecontrollers;
import data.datamain.Route;
import data.datamain.Station;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import org.controlsfx.control.textfield.TextFields;
import java.net.URL;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.stream.Collectors;

public class SearchRoutesSceneController {
    private static SearchRoutesSceneController controller;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private AnchorPane anchorPane;

    @FXML
    private TextField secondTextField;

    @FXML
    private TextField firstTextField;

    public SearchRoutesSceneController() {
        controller = this;
    }

    @FXML
    void initialize() {
        HashMap<String, Route> map = ClientMainController.getController().getMap();
        autoCompleteTextField(map);
    }

    private void autoCompleteTextField(HashMap<String, Route> map) {
        HashSet<Station> stationHashSet = new HashSet<>();

        for (Map.Entry<String, Route> m : map.entrySet()) {
            stationHashSet.addAll(m.getValue().getStops());
        }

        firstTextField.setPromptText("Откуда");
        secondTextField.setPromptText("Куда");

        TextFields.bindAutoCompletion(firstTextField, t -> stationHashSet.stream().filter(elem
                -> elem.getName().toLowerCase().startsWith(t.getUserText().toLowerCase())).collect(Collectors.toList()));
        TextFields.bindAutoCompletion(secondTextField, t -> stationHashSet.stream().filter(elem
                -> elem.getName().toLowerCase().startsWith(t.getUserText().toLowerCase())).collect(Collectors.toList()));
    }

    @FXML
    private void searchRoutes(ActionEvent event) {
        ClientMainController.getController().startSearch(firstTextField.getText(), secondTextField.getText());
    }

    public static SearchRoutesSceneController getController() {
        return controller;
    }

    public TextField getSecondTextField() {
        return secondTextField;
    }

    public TextField getFirstTextField() {
        return firstTextField;
    }
}
