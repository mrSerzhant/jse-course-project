package scenecontrollers;
import data.datamain.Ticket;
import data.datamain.User;
import data.dataprocessing.JsonReader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ListTicketsController {
    private ObservableList<Ticket> listTickets;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<Ticket> ticketsTable;

    @FXML
    private TableColumn<Ticket, Ticket> number;

    @FXML
    private TableColumn<Ticket, String> numberTicket;

    @FXML
    private TableColumn<Ticket, String> nameWay;

    @FXML
    void initialize() {
        listTickets = FXCollections.observableArrayList();

        number.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        number.setCellFactory(new Callback<TableColumn<Ticket, Ticket>, TableCell<Ticket, Ticket>>() {

            @Override
            public TableCell<Ticket, Ticket> call(TableColumn<Ticket, Ticket> param) {
                return new TableCell<Ticket, Ticket>() {
                    Label label = new Label();

                    public void updateItem(Ticket ticket, boolean empty) {
                        super.updateItem(ticket, empty);
                        if (ticket != null) {
                            label.setText(String.valueOf(listTickets.indexOf(ticket)+1));
                            setGraphic(label);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
        numberTicket.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getNumber()));
        nameWay.setCellValueFactory(cellData -> new ReadOnlyStringWrapper(cellData.getValue().getWay()));

    }

    public void initData(String stringUsersBase) {
        JsonReader jsonReader = new JsonReader();
        HashSet<User> list = jsonReader.doUsersList(stringUsersBase);

        for(User user : list){
            listTickets.addAll(user.getTickets());
        }
        ticketsTable.setItems(listTickets);
    }
}
