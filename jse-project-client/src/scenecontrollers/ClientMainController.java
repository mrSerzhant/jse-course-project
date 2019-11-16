package scenecontrollers;

import connection.Connection;
import data.datamain.Route;
import data.datamain.Station;
import data.datamain.User;
import data.dataprocessing.JavaBean;
import data.dataprocessing.JsonReader;
import data.dataprocessing.JsonWriter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;

import java.io.IOException;
import java.net.URL;
import java.util.*;

public class ClientMainController {
    private ObservableList<Route> listRoutes = FXCollections.observableArrayList();
    private ObservableList<Route> listForSearch = FXCollections.observableArrayList();
    private Route routeForSelect = new Route();
    private User user;
    private Connection connection;
    private JavaBean map;
    private static ClientMainController controller;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Route> tableStation;

    @FXML
    private TableColumn<Route, String> number;

    @FXML
    private TableColumn<Route, String> route;

    @FXML
    private TableColumn<Route, String> departure;

    @FXML
    private TableColumn<Route, String> arrival;

    @FXML
    private TableColumn<Route, String> travelTime;

    @FXML
    private TableColumn<Route, Route> choice;

    @FXML
    private AnchorPane changeAnchorPane;

    @FXML
    private Label countRoutes;

    @FXML
    private Label userName;

    public ClientMainController() throws IOException {
        controller = this;
    }

    @FXML
    public void initialize() throws IOException {
        connection = new Connection();

        loadSearchScene();
        user = LoginController.getController().getUser();
        userName.setText(user.getName());

        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        route.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        departure.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartureTime()));
        arrival.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getArrival()));
        travelTime.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTravelTime()));
        choice.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        choice.setCellFactory(new Callback<TableColumn<Route, Route>, TableCell<Route, Route>>() {

            @Override
            public TableCell<Route, Route> call(TableColumn<Route, Route> param) {
                return new TableCell<Route, Route>() {
                    Button button = new Button();

                    {
                        button.setText("Купить");
                        button.setMinWidth(65);
                    }

                    public void updateItem(Route route, boolean empty) {
                        super.updateItem(route, empty);
                        if (route != null) {
                            setGraphic(button);

                            button.setOnAction(event -> {
                                routeForSelect = route;

                                try {
                                    createConfirmBuyScene(event);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            });

                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    public void loadSearchScene() throws IOException {
        map = connection.getServerData();

        if (!changeAnchorPane.getChildren().isEmpty()) {
            changeAnchorPane.getChildren().clear();
        }

        Node node = FXMLLoader.load(getClass().getResource("../fxmlscenes/searchRoutesScene.fxml"));
        changeAnchorPane.getChildren().add(node);
        initData(map);
    }

    private void initData(HashMap<String, Route> map) {
        if (!listRoutes.isEmpty()) {
            listRoutes.clear();
        }

        for (Map.Entry<String, Route> mapElements : map.entrySet()) {
            listRoutes.add(mapElements.getValue());
        }
        tableStation.setItems(listRoutes);
        countRoutes.setText(String.valueOf(listRoutes.size()));
    }

    public void startSearch(String departureValue, String arrivalValue) {
        Alert alertBox = new Alert(Alert.AlertType.WARNING);

        if (arrivalValue.isEmpty()) {
            alertBox.setTitle("Внимание");
            alertBox.setContentText("Необходимо заполнить поля поиска");
            alertBox.setHeaderText("Упс, искать нечего");
            alertBox.show();
        } else {
            if (!listRoutes.isEmpty()) {
                listForSearch.clear();
            }

            if (departureValue.isEmpty()) {
                String arrivalValueLowerCase = arrivalValue.toLowerCase();

                for (Map.Entry<String, Route> mapElements : map.entrySet()) {
                    if (mapElements.getKey().toLowerCase().contains(arrivalValueLowerCase) &&
                            mapElements.getKey().toLowerCase().indexOf(arrivalValueLowerCase) != 0) {
                        listForSearch.add(mapElements.getValue());
                    }
                }
            } else {
                String arrivalValueLowerCase = arrivalValue.toLowerCase();
                String departureValueLowerCase = departureValue.toLowerCase();

                for (Map.Entry<String, Route> mapElements : map.entrySet()) {

                    if (mapElements.getKey().toLowerCase().contains(departureValueLowerCase) &&
                            mapElements.getKey().toLowerCase().contains(arrivalValueLowerCase)) {
                        int indexArrivalValue =  mapElements.getKey().toLowerCase().indexOf(arrivalValueLowerCase);
                        int indexDepartureValue =  mapElements.getKey().toLowerCase().indexOf(departureValueLowerCase);

                        if(indexArrivalValue > indexDepartureValue){
                            listForSearch.add(mapElements.getValue());
                        }
                    }
                }
            }

            if (listForSearch.isEmpty()) {
                alertBox.setAlertType(Alert.AlertType.INFORMATION);
                alertBox.setContentText("Попробуйте в другой раз");
                alertBox.setHeaderText("Маршрут не найден");
                alertBox.show();
                tableStation.setItems(listRoutes);
            } else {
                tableStation.setItems(listForSearch);
            }
        }
    }

    private void createStationsListWindow(List<Station> stationList) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlscenes/stationsWindow.fxml"));
        loader.load();
        StationsWindowController runNewWindow = loader.getController();
        runNewWindow.initData(stationList);
        Parent parent = loader.getRoot();
        Stage newWindow = new Stage();
        newWindow.setTitle("Список станций");
        Scene scene = new Scene(parent);
        newWindow.setScene(scene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.show();
    }

    private void createConfirmBuyScene(ActionEvent event) throws IOException {
        changeAnchorPane.getChildren().clear();
        Node node = FXMLLoader.load(getClass().getResource("../fxmlscenes/confirmSearchScene.fxml"));
        changeAnchorPane.getChildren().add(node);
    }

    @FXML
    private void printAllRoutes(ActionEvent event) throws IOException {
        loadSearchScene();
    }

    @FXML
    private void clickOnTableRow(MouseEvent event) {
        if (!tableStation.getItems().isEmpty() && event.getClickCount() == 2 && event.getButton() == MouseButton.PRIMARY) {
            try {
                createStationsListWindow(tableStation.getFocusModel().getFocusedItem().getStops());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public User getUser() {
        return user;
    }

    public Route getRouteForSelect() {
        return routeForSelect;
    }

    @FXML
    private void openTicketsWindow(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlscenes/ticketsWindow.fxml"));
        loader.load();
        TicketsWindowController controller = loader.getController();
        JsonWriter jsonWriter = new JsonWriter();
        JsonReader jsonReader = new JsonReader();
        controller.initData(jsonReader.doUserBean(connection.getUserData(jsonWriter.writeStringUserDateToServer(user))).getTickets());
        Parent parent = loader.getRoot();
        Stage stationsWindow = new Stage();
        stationsWindow.setTitle("Список билетов");
        Scene scene = new Scene(parent);
        stationsWindow.setScene(scene);
        stationsWindow.initModality(Modality.APPLICATION_MODAL);
        stationsWindow.setResizable(false);
        stationsWindow.show();
    }

    @FXML
    private void exitApp(ActionEvent event) throws IOException {
        Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
        alertBox.setTitle("Внимание");
        alertBox.setHeaderText("Действительно желаете выйти?");

        Optional<ButtonType> result = alertBox.showAndWait();
        if (result.get() == ButtonType.OK) {
            Parent parent = FXMLLoader.load(getClass().getResource("../fxmlscenes/loginScene.fxml"));
            Scene mainScene = new Scene(parent);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(mainScene);
            stage.show();
        }
    }

    public static ClientMainController getController() {
        return controller;
    }

    public HashMap<String, Route> getMap() {
        return map;
    }
}








