package scenecontrollers;
import connection.Connection;
import data.datamain.Route;
import data.datamain.Station;
import data.datamain.utils.DateBuilder;
import data.datamain.utils.ListRoutesUtils;
import data.dataprocessing.JavaBean;
import data.dataprocessing.JsonWriter;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import javafx.util.Callback;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class AddRouteController {
    private ObservableList<Station> listStations = FXCollections.observableArrayList();
    private ObservableList<Integer> listIndex = FXCollections.observableArrayList();
    private JavaBean map;
    private Station tempStation = new Station();
    private Route tempRoute = new Route();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableColumn<Station, Station> number;

    @FXML
    private TableView<Station> tableStations;

    @FXML
    private TableColumn<Station, String> nameStation;

    @FXML
    private TableColumn<Station, String> departure;

    @FXML
    private TableColumn<Station, String> arrival;

    @FXML
    private TableColumn<Station, Station> change;

    @FXML
    private TableColumn<Station, Station> delete;

    @FXML
    private Button buttonCancelChangeStation;

    @FXML
    private Button buttonConfirmChangeStation;

    @FXML
    private Label numberRoute;

    @FXML
    private Label nameRoute;

    @FXML
    private TextField numberLabel;

    @FXML
    private TextField nameLabel;

    @FXML
    private TextField departureHoursLabel;

    @FXML
    private TextField departureMinutesLabel;

    @FXML
    private TextField arrivalHoursLabel;

    @FXML
    private TextField arrivalMinutesLabel;

    @FXML
    private Label nameChangeStation;

    @FXML
    private TextField arrivalMinutesLabelAdd;

    @FXML
    private TextField departureMinutesLabelAdd;

    @FXML
    private TextField departureHoursLabelAdd;

    @FXML
    private TextField arrivalHoursLabelAdd;

    @FXML
    private TextField nameStationAdd;

    @FXML
    private Button buttonConfirmAddStation;

    @FXML
    private ComboBox<Integer> indexAddStation;

    @FXML
    private void initialize() {
        nameStation.setCellValueFactory(new PropertyValueFactory<>("name"));
        departure.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateBuilder.printDate(cellData.getValue().getDeparture())));
        arrival.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateBuilder.printDate(cellData.getValue().getArrival())));
        number.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        number.setCellFactory(new Callback<TableColumn<Station, Station>, TableCell<Station, Station>>() {

            @Override
            public TableCell<Station, Station> call(TableColumn<Station, Station> param) {
                return new TableCell<Station, Station>() {
                    Label label = new Label();

                    public void updateItem(Station station, boolean empty) {
                        super.updateItem(station, empty);
                        if (station != null) {
                            label.setText(String.valueOf(listStations.indexOf(station) + 1));
                            setGraphic(label);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        delete.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        delete.setCellFactory(new Callback<TableColumn<Station, Station>, TableCell<Station, Station>>() {

            @Override
            public TableCell<Station, Station> call(TableColumn<Station, Station> param) {
                return new TableCell<Station, Station>() {
                    Button button = new Button();

                    {
                        button.setText("Удалить");
                        button.setMinWidth(65);
                    }

                    public void updateItem(Station station, boolean empty) {
                        super.updateItem(station, empty);
                        if (station != null) {
                            setGraphic(button);

                            button.setOnAction(event -> {
                                if (listStations.size() > 1) {
                                    listStations.remove(station);
                                    setVoidFieldsValues();
                                    nameRoute.setText(tempRoute.getName());
                                } else {
                                    Alert alertBox = new Alert(Alert.AlertType.WARNING);
                                    alertBox.setTitle("Внимание");
                                    alertBox.setContentText("Первая станция не может быть удалена");
                                    alertBox.setHeaderText("Возможно только редактирование");
                                    alertBox.show();
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
        change.setCellFactory(new Callback<TableColumn<Station, Station>, TableCell<Station, Station>>() {

            @Override
            public TableCell<Station, Station> call(TableColumn<Station, Station> param) {
                return new TableCell<Station, Station>() {
                    Button button = new Button();

                    {
                        button.setText("Изменить");
                        button.setMinWidth(65);
                    }

                    public void updateItem(Station station, boolean empty) {
                        super.updateItem(station, empty);
                        if (station != null) {
                            setGraphic(button);

                            button.setOnAction(event -> {
                                tempStation = station;
                                setStartFieldsValues(tempStation);
                                buttonConfirmAddStation.setDisable(true);
                                nameRoute.setText(tempRoute.getName());
                            });
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    public void initData(HashMap<String, Route> map) {
        this.map = (JavaBean) map;
        tableStations.setItems(listStations);
        tempRoute.setStops(listStations);
        indexAddStation.setValue(listStations.size() + 1);
    }

    @FXML
    private void confirmChangeNumber(ActionEvent event) {
        boolean checkDuplicate = true;

        if (numberLabel.getLength() == 0 || numberLabel.getLength() > 5) {
            Alert alertBox = new Alert(Alert.AlertType.WARNING);
            alertBox.setTitle("Внимание");
            alertBox.setContentText("Возможно поле пустое или имеет большое количество символов");
            alertBox.setHeaderText("Некорректные данные");
            alertBox.show();
        } else {

            if (map.size() == 0) {
                checkDuplicate = false;
            }

            for (Map.Entry<String, Route> entry : map.entrySet()) {
                if (entry.getValue().getNumber().equals(numberLabel.getText())) {
                    checkDuplicate = true;
                    Alert alertBox = new Alert(Alert.AlertType.WARNING);
                    alertBox.setTitle("Внимание");
                    alertBox.setContentText("Попробуйте другой номер");
                    alertBox.setHeaderText("Данный номер уже существует");
                    alertBox.show();
                    break;
                } else {
                    checkDuplicate = false;
                }
            }

            if (!checkDuplicate) {
                tempRoute.setNumber(numberLabel.getText());
                numberRoute.setText(tempRoute.getNumber());

                Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
                alertBox.setTitle("Изменено");
                alertBox.setHeaderText("Номер успешно изменен");
                alertBox.show();
            }
        }
    }

    @FXML
    private void calculateIndexAddStation(MouseEvent event) {
        if (listIndex.size() > 0) {
            listIndex.clear();
        }

        for (int i = 0; i < listStations.size(); i++) {
            listIndex.add(i + 1);
        }

        listIndex.add(listIndex.size() + 1);
        indexAddStation.setItems(listIndex);
    }

    @FXML
    private void confirmAddRoute(ActionEvent event) throws IOException {
        if (listStations.size() < 2 || numberRoute.getText().equals("№")) {
            Alert alertBox = new Alert(Alert.AlertType.WARNING);
            alertBox.setTitle("Внимание");
            alertBox.setHeaderText("Маршрут не может быть создан");
            alertBox.setContentText("Некорректные данные.(Минимальное количество станций - 2, номер маршрута должен быть указан)");
            alertBox.show();
        } else {
            map.put(ListRoutesUtils.routeNameBuilder(listStations, numberRoute.getText()), tempRoute);
            Connection connection = new Connection();
            JsonWriter jsonWriter = new JsonWriter();
            connection.sendDataToServer(jsonWriter.writeStringToDataBase(map));

            ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
        }
    }

    @FXML
    private void confirmAddStation(ActionEvent event) {
        boolean checkName = true;
        boolean checkDepartureTime = true;
        boolean checkArrivalTime = true;

        if (nameStationAdd.getLength() == 0) {
            checkName = false;
        }

        for (Station station : listStations) {
            if (nameStationAdd.getText().equals(station.getName())) {
                checkName = false;
                break;
            }
        }

        if (departureHoursLabelAdd.getLength() == 0 || departureHoursLabelAdd.getLength() > 2 ||
                !checkCorrectHoursParse(departureHoursLabelAdd.getText())) {
            checkDepartureTime = false;
        }

        if (departureMinutesLabelAdd.getLength() == 0 || departureMinutesLabelAdd.getLength() > 2 ||
                !checkCorrectMinutesParse(departureMinutesLabelAdd.getText())) {
            checkDepartureTime = false;
        }

        if (arrivalHoursLabelAdd.getLength() == 0 || arrivalHoursLabelAdd.getLength() > 2 ||
                !checkCorrectHoursParse(arrivalHoursLabelAdd.getText())) {
            checkArrivalTime = false;
        }

        if (arrivalMinutesLabelAdd.getLength() == 0 || arrivalMinutesLabelAdd.getLength() > 2 ||
                !checkCorrectMinutesParse(arrivalMinutesLabelAdd.getText())) {
            checkArrivalTime = false;
        }

        if (!checkName || !checkDepartureTime || !checkArrivalTime || indexAddStation.getValue() == null) {
            Alert alertBox = new Alert(Alert.AlertType.WARNING);
            alertBox.setTitle("Ошибка");
            alertBox.setHeaderText("Неверный формат ввода");
            alertBox.show();
        } else {
            String hoursDeparture = doCorrectTime(departureHoursLabelAdd.getText());
            String minutesDeparture = doCorrectTime(departureMinutesLabelAdd.getText());
            String hoursArrival = doCorrectTime(arrivalHoursLabelAdd.getText());
            String minutesArrival = doCorrectTime(arrivalMinutesLabelAdd.getText());

            Station newStation = new Station(
                    nameStationAdd.getText(),
                    String.format("%s:%s", hoursArrival, minutesArrival),
                    String.format("%s:%s", hoursDeparture, minutesDeparture));

            int index = indexAddStation.getSelectionModel().getSelectedItem() - 1;

            listStations.add(index, newStation);
            tableStations.refresh();
            indexAddStation.setValue(listStations.size() + 1);

            Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
            alertBox.setTitle("Успех");
            alertBox.setHeaderText("Станция успено добавлена");
            alertBox.show();

            tempRoute.setStops(listStations);
            nameRoute.setText(tempRoute.getName());
        }
    }

    @FXML
    private void closeWindow(ActionEvent event) {
        ((Stage) (((Button) event.getSource()).getScene().getWindow())).close();
    }

    @FXML
    private void confirmChangeStation(ActionEvent event) {
        boolean checkName = true;
        boolean checkDepartureTime = true;
        boolean checkArrivalTime = true;

        if (nameLabel.getLength() == 0) {
            checkName = false;
        }

        for (Station station : listStations) {
            if (nameLabel.getText().equals(station.getName()) && !(tempStation.getName().equals(nameLabel.getText()))) {
                checkName = false;
                break;
            }
        }

        if (departureHoursLabel.getLength() == 0 || departureHoursLabel.getLength() > 2 ||
                !checkCorrectHoursParse(departureHoursLabel.getText())) {
            checkDepartureTime = false;
        }

        if (departureMinutesLabel.getLength() == 0 || departureMinutesLabel.getLength() > 2 ||
                !checkCorrectMinutesParse(departureMinutesLabel.getText())) {
            checkDepartureTime = false;
        }

        if (arrivalHoursLabel.getLength() == 0 || arrivalHoursLabel.getLength() > 2 ||
                !checkCorrectHoursParse(arrivalHoursLabel.getText())) {
            checkArrivalTime = false;
        }

        if (arrivalMinutesLabel.getLength() == 0 || arrivalMinutesLabel.getLength() > 2 ||
                !checkCorrectMinutesParse(arrivalMinutesLabel.getText())) {
            checkArrivalTime = false;
        }

        if (!checkName || !checkDepartureTime || !checkArrivalTime) {
            Alert alertBox = new Alert(Alert.AlertType.WARNING);
            alertBox.setTitle("Ошибка");
            alertBox.setHeaderText("Неверный формат ввода");
            alertBox.show();
            setStartFieldsValues(tempStation);
        } else {
            tempStation.setName(nameLabel.getText());

            String hoursDeparture = doCorrectTime(departureHoursLabel.getText());
            String minutesDeparture = doCorrectTime(departureMinutesLabel.getText());
            String hoursArrival = doCorrectTime(arrivalHoursLabel.getText());
            String minutesArrival = doCorrectTime(arrivalMinutesLabel.getText());

            tempStation.setArrival(DateBuilder.createDate(String.format("%s:%s", hoursArrival, minutesArrival)));
            tempStation.setDeparture(DateBuilder.createDate(String.format("%s:%s", hoursDeparture, minutesDeparture)));

            tableStations.refresh();

            Alert alertBox = new Alert(Alert.AlertType.INFORMATION);
            alertBox.setTitle("Успех");
            alertBox.setHeaderText("Станция изменена");
            alertBox.show();

            setVoidFieldsValues();
            buttonConfirmAddStation.setDisable(false);
        }
    }

    @FXML
    private void cancelChangeStation(ActionEvent event) {
        setVoidFieldsValues();
        buttonConfirmAddStation.setDisable(false);
    }

    private String doCorrectTime(String text) {
        if (text.length() == 1) {
            return String.format("0%s", text);
        }

        return text;
    }

    private boolean checkCorrectHoursParse(String text) {
        try {
            if (Integer.parseInt(text) < 0 || Integer.parseInt(text) > 23) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private boolean checkCorrectMinutesParse(String text) {
        try {
            if (Integer.parseInt(text) < 0 || Integer.parseInt(text) > 59) {
                return false;
            }
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    private void setVoidFieldsValues() {
        nameLabel.setText("");
        departureHoursLabel.setText("");
        departureMinutesLabel.setText("");
        arrivalHoursLabel.setText("");
        arrivalMinutesLabel.setText("");
        nameChangeStation.setVisible(false);
        buttonConfirmChangeStation.setDisable(true);
        buttonCancelChangeStation.setDisable(true);
    }

    private void setStartFieldsValues(Station station) {
        buttonConfirmChangeStation.setDisable(false);
        buttonCancelChangeStation.setDisable(false);
        nameChangeStation.setVisible(true);
        nameChangeStation.setText(station.getName());
        nameLabel.setText(station.getName());
        String departureTime = DateBuilder.printDate(station.getDeparture());
        departureHoursLabel.setText(splitHours(departureTime));
        departureMinutesLabel.setText(splitMinutes(departureTime));
        String arrivalTime = DateBuilder.printDate(station.getArrival());
        arrivalHoursLabel.setText(splitHours(arrivalTime));
        arrivalMinutesLabel.setText(splitMinutes(arrivalTime));
    }

    private String splitHours(String time) {
        String[] array = time.split("[:]");
        return array[0];
    }

    private String splitMinutes(String time) {
        String[] array = time.split("[:]");
        return array[1];
    }
}
