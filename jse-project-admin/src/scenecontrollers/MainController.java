package scenecontrollers;
import connection.Connection;
import data.datamain.Route;
import data.datamain.Station;
import data.datamain.utils.ListRoutesUtils;
import data.dataprocessing.JavaBean;
import data.dataprocessing.JsonWriter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Callback;
import org.controlsfx.control.textfield.TextFields;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.stream.Collectors;

public class MainController {
    private ObservableList<Route> listRoutes = FXCollections.observableArrayList();
    private ObservableList<Route> listForSearch = FXCollections.observableArrayList();
    private Connection connection;
    private JavaBean map;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Route> tableRoutes;

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
    private TableColumn<Route, Route> change;

    @FXML
    private TableColumn<Route, Route> delete;

    @FXML
    private Label countRoutes;

    @FXML
    private TextField firstTextField;

    @FXML
    private void initialize() throws IOException {
        connection = new Connection();
        updateData();

        number.setCellValueFactory(new PropertyValueFactory<>("number"));
        route.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getName()));
        departure.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getDepartureTime()));
        arrival.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getArrival()));
        travelTime.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTravelTime()));
        delete.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        delete.setCellFactory(new Callback<TableColumn<Route, Route>, TableCell<Route, Route>>() {

            @Override
            public TableCell<Route, Route> call(TableColumn<Route, Route> param) {
                return new TableCell<Route, Route>() {
                    Button button = new Button();

                    {
                        button.setText("Удалить");
                        button.setMinWidth(65);
                    }

                    public void updateItem(Route route, boolean empty) {
                        super.updateItem(route, empty);
                        if (route != null) {
                            setGraphic(button);

                            button.setOnAction(event -> {

                                try {
                                    confirmDelete(route);
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
        change.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        change.setCellFactory(new Callback<TableColumn<Route, Route>, TableCell<Route, Route>>() {

            @Override
            public TableCell<Route, Route> call(TableColumn<Route, Route> param) {
                return new TableCell<Route, Route>() {
                    Button button = new Button();

                    {
                        button.setText("Изменить");
                        button.setMinWidth(65);
                    }

                    public void updateItem(Route route, boolean empty) {
                        super.updateItem(route, empty);
                        if (route != null) {
                            setGraphic(button);

                            button.setOnAction(event -> {

                                try {
                                    openChangeRouteWindow(route);
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

    private void updateData() throws IOException {
        map = connection.getServerData();
        initData(map);
        autoCompleteTextField(map);
        countRoutes.setText(String.valueOf(listRoutes.size()));
    }


    private void confirmDelete(Route route) throws IOException {
        Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
        alertBox.setTitle("Внимание");
        alertBox.setContentText("Нажмите 'ОК' для подтверждения");
        alertBox.setHeaderText("Удалить маршрут : " + route.getName() + "?");

        Optional<ButtonType> result = alertBox.showAndWait();
        if (result.get() == ButtonType.OK) {
            String mapKey = ListRoutesUtils.routeNameMaker(route.getStops());
            map.remove(mapKey);
            sendData();
        }
    }

    @FXML
    private void searchRoutes(ActionEvent event) {
        Alert alertBox = new Alert(Alert.AlertType.WARNING);
        String stationName = firstTextField.getText();

        if (stationName.length() == 0) {
            alertBox.setTitle("Внимание");
            alertBox.setContentText("Необходимо заполнить поля поиска");
            alertBox.setHeaderText("Упс, искать нечего");
            alertBox.show();
        } else {
            if (listForSearch.size() > 0) {
                listForSearch.clear();
            }

            for (Map.Entry<String, Route> mapElements : map.entrySet()) {
                if (mapElements.getKey().toLowerCase().contains(stationName.toLowerCase())) {
                    listForSearch.add(mapElements.getValue());
                }
            }

            if (listForSearch.size() == 0) {
                alertBox.setAlertType(Alert.AlertType.INFORMATION);
                alertBox.setContentText("Попробуйте в другой раз");
                alertBox.setHeaderText("Маршрут не найден");
                alertBox.show();
                tableRoutes.setItems(listRoutes);
            } else {
                tableRoutes.setItems(listForSearch);
                countRoutes.setText(String.valueOf(listForSearch.size()));
            }
        }
    }

    private void initData(HashMap<String, Route> map) {
        if (tableRoutes.getItems().size() != 0) {
            listRoutes.clear();

            tableRoutes.getItems().clear();
        }

        for (Map.Entry<String, Route> entry : map.entrySet()) {
            listRoutes.add(entry.getValue());
        }
        tableRoutes.setItems(listRoutes);
    }

    private void autoCompleteTextField(HashMap<String, Route> map) {
        HashSet<Station> stationHashSet = new HashSet<>();

        for (Map.Entry<String, Route> m : map.entrySet()) {
            stationHashSet.addAll(m.getValue().getStops());
        }

        firstTextField.setPromptText("Название станции");

        TextFields.bindAutoCompletion(firstTextField, t -> stationHashSet.stream().filter(elem
                -> elem.getName().toLowerCase().startsWith(t.getUserText().toLowerCase())).collect(Collectors.toList()));
    }

    private void openChangeRouteWindow(Route route) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlscenes/changeRouteScene.fxml"));
        loader.load();
        ChangeRouteController controller = loader.getController();
        controller.initData(route);
        Parent parent = loader.getRoot();
        Stage newWindow = new Stage();
        newWindow.setTitle("Окно изменения маршрута");
        Scene scene = new Scene(parent);
        newWindow.setScene(scene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.showAndWait();
        sendData();
    }

    @FXML
    private void addRoute(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlscenes/addRouteScene.fxml"));
        loader.load();
        AddRouteController controller = loader.getController();
        controller.initData(connection.getServerData());
        Parent parent = loader.getRoot();
        Stage newWindow = new Stage();
        newWindow.setTitle("Окно добавления маршрута");
        Scene scene = new Scene(parent);
        newWindow.setScene(scene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.showAndWait();
        updateData();
    }

    private void sendData() throws IOException{
        JsonWriter jsonWriter = new JsonWriter();
        connection.sendDataToServer(jsonWriter.writeStringToDataBase(map));
        updateData();
    }

    @FXML
    private void printAllRoutes(ActionEvent event) throws IOException {
        updateData();
    }

    @FXML
    private void printAllUsers(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlscenes/listUsersScene.fxml"));
        loader.load();
        ListUsersController controller = loader.getController();
        controller.initData(connection.getUsersBase());
        Parent parent = loader.getRoot();
        Stage newWindow = new Stage();
        newWindow.setTitle("Список всех пользователей");
        Scene scene = new Scene(parent);
        newWindow.setScene(scene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.showAndWait();
        updateData();
    }

    @FXML
    private void printListTickets(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("../fxmlscenes/listTicketsScene.fxml"));
        loader.load();
        ListTicketsController controller = loader.getController();
        controller.initData(connection.getUsersBase());
        Parent parent = loader.getRoot();
        Stage newWindow = new Stage();
        newWindow.setTitle("Список всех купленных билетов");
        Scene scene = new Scene(parent);
        newWindow.setScene(scene);
        newWindow.initModality(Modality.APPLICATION_MODAL);
        newWindow.setResizable(false);
        newWindow.showAndWait();
        updateData();
    }

    @FXML
    private void resetDataBase(ActionEvent event) throws IOException {
        Alert alertBox = new Alert(Alert.AlertType.CONFIRMATION);
        alertBox.setTitle("Внимание");
        alertBox.setContentText("Нажмите 'ОК' для подтверждения");
        alertBox.setHeaderText("Будут загружены маршруты, установленные по умолчанию");

        Optional<ButtonType> result = alertBox.showAndWait();
        if (result.get() == ButtonType.OK) {
            connection.resetDataBase();
            updateData();
        }
    }

    @FXML
    private void exitApp(ActionEvent event) {
        ((Stage)(((Button)event.getSource()).getScene().getWindow())).close();
    }
}
