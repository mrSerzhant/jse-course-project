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
        writer.close();
        reader.close();
        return javaBean;
    }

    public String sendUserJson(String jsonString) throws IOException {
        createSocket();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("GetUser\n");
        writer.flush();

        writer.write(jsonString + "\n");
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response =  reader.readLine();
        writer.close();
        reader.close();
        return response;

    }

    public void sendUserData(String jsonString) throws IOException {
        createSocket();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("GetUserData\n");
        writer.flush();

        writer.write(jsonString + "\n");
        writer.flush();
        writer.close();
    }

    public String getUserData(String jsonString) throws IOException {
        createSocket();

        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
        writer.write("GiveUserData\n");
        writer.flush();

        writer.write(jsonString + "\n");
        writer.flush();

        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        String response =  reader.readLine();
        writer.close();
        reader.close();
        return response;

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
