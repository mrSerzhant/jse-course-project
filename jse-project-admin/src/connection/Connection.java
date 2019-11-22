package connection;
import data.dataprocessing.JavaBean;
import data.dataprocessing.JsonReader;
import javafx.scene.control.Alert;
import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Connection {
    private Socket socket;

    public JavaBean getServerData() throws IOException {
        createSocket();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("GiveData\n");
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        JsonReader jsonReader = new JsonReader();
        JavaBean javaBean = jsonReader.doJavaBean(reader.readLine());
        reader.close();
        writer.close();
        return javaBean;
    }

    public void sendDataToServer(String jsonString) throws IOException {
        createSocket();

        BufferedWriter writerOne = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writerOne.write("GetData\n");
        writerOne.flush();

        writerOne = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writerOne.write(jsonString+"\n");
        writerOne.flush();
        writerOne.close();
    }

    public String getUsersBase() throws IOException {
        createSocket();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("GiveUsersBase\n");
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response = reader.readLine();
        reader.close();
        writer.close();

        return response;
    }

    public void resetDataBase() throws IOException {
        createSocket();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("ResetDataBase\n");
        writer.flush();
        writer.close();
    }

    private void createSocket(){
        try {
            socket = new Socket(InetAddress.getLocalHost(), 8000);
        } catch (IOException e) {
            Alert alertBox = new Alert(Alert.AlertType.WARNING);
            alertBox.setTitle("Внимание");
            alertBox.setHeaderText("Сервер не работает");
            alertBox.setContentText("Попробуйте позже");
            alertBox.showAndWait();
            System.exit(-1);
        }
    }
}
