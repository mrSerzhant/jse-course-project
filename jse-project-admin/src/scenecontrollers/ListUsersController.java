package scenecontrollers;
import data.datamain.User;
import data.dataprocessing.JsonReader;
import javafx.beans.property.ReadOnlyObjectWrapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.Callback;
import java.net.URL;
import java.util.HashSet;
import java.util.ResourceBundle;

public class ListUsersController {
    private ObservableList<User> userList;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TableView<User> tableUsers;

    @FXML
    private TableColumn<User, User> number;

    @FXML
    private TableColumn<User, String> login;

    @FXML
    private TableColumn<User, String> password;

    @FXML
    private TableColumn<User, User> countTickets;

    @FXML
    void initialize() {
        userList = FXCollections.observableArrayList();
        login.setCellValueFactory(new PropertyValueFactory<>("name"));
        password.setCellValueFactory(new PropertyValueFactory<>("password"));
        number.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        number.setCellFactory(new Callback<TableColumn<User, User>, TableCell<User, User>>() {

            @Override
            public TableCell<User, User> call(TableColumn<User, User> param) {
                return new TableCell<User, User>() {
                    Label label = new Label();

                    public void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            label.setText(String.valueOf(userList.indexOf(user) + 1));
                            setGraphic(label);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });

        countTickets.setCellValueFactory(cellData -> new ReadOnlyObjectWrapper<>(cellData.getValue()));
        countTickets.setCellFactory(new Callback<TableColumn<User, User>, TableCell<User, User>>() {

            @Override
            public TableCell<User, User> call(TableColumn<User, User> param) {
                return new TableCell<User, User>() {
                    Label label = new Label();

                    public void updateItem(User user, boolean empty) {
                        super.updateItem(user, empty);
                        if (user != null) {
                            label.setText(String.valueOf(user.getTickets().size()));
                            setGraphic(label);
                        } else {
                            setGraphic(null);
                        }
                    }
                };
            }
        });
    }

    public void initData(String stringUsersBase) {
        JsonReader jsonReader = new JsonReader();
        HashSet<User> list = jsonReader.doUsersList(stringUsersBase);
        userList.addAll(list);
        tableUsers.setItems(userList);
    }
}
