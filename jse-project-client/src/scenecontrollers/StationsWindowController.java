package scenecontrollers;
import data.datamain.Station;
import data.datamain.utils.DateBuilder;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class StationsWindowController {
    private ObservableList<Station> listStation = FXCollections.observableArrayList();

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Station> stationsTable;

    @FXML
    private TableColumn<Station, String> name;

    @FXML
    private TableColumn<Station, String> arrival;

    @FXML
    private TableColumn<Station, String> departure;

    @FXML
    private TableColumn<Station, String> stopTime;

    @FXML
    void initialize()  {
        name.setCellValueFactory(new PropertyValueFactory<>("name"));
        arrival.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateBuilder.printDate(cellData.getValue().getArrival())));
        departure.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(DateBuilder.printDate(cellData.getValue().getDeparture())));
        stopTime.setCellValueFactory(new PropertyValueFactory<>("stopTime"));
    }

    public void initData(List<Station> arrayList){
        listStation.addAll(arrayList);
        stationsTable.setItems(listStation);
    }

}
