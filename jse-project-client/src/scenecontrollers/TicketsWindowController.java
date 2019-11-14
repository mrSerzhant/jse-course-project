package scenecontrollers;
import data.datamain.Ticket;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class TicketsWindowController {
    private ObservableList<Ticket> listTickets;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Ticket> ticketsTable;

    @FXML
    private TableColumn<Ticket, String> number;

    @FXML
    private TableColumn<Ticket, String> nameWay;

    @FXML
    private TableColumn<Ticket, String> departure;

    @FXML
    private TableColumn<Ticket, String> arrival;

    @FXML
    void initialize() {
        listTickets =  FXCollections.observableArrayList();
        number.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNumber()));
        nameWay.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getWay()));
        departure.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTimeDeparture()));
        arrival.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getTimeArrival()));
    }

    public void initData(List<Ticket> list){
        listTickets.addAll(list);
        ticketsTable.setItems(listTickets);
    }
}
