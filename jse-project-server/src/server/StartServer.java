package server;
import database.datamain.MapBuilder;
import database.datamain.Route;
import database.dataprocessing.*;
import org.xml.sax.SAXException;
import server.commands.Command;
import server.commands.CommandsFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

class StartServer implements Runnable {

    private StartServer() throws TransformerException, ParserConfigurationException {
        File dataBase = new File("jse-project-server\\src\\database\\databasefiles\\database.xml");
        DomWriter xmlWriter = new DomWriter();

        if (!dataBase.exists()) {
            HashMap<String, Route> map = new MapBuilder();
            xmlWriter.writeDataBaseXmlFile(map);
        }
    }

    @Override
    public void run() {
        try (ServerSocket serverSocket = new ServerSocket(8000)) {
            System.out.println("Server started");

            while (true) {
                Socket socket = serverSocket.accept();

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

                String requestString = reader.readLine();

                CommandsFactory factory = CommandsFactory.getFactory();
                Command command = factory.getCommand(requestString);
                command.processCommand(reader, writer);

                writer.flush();
                reader.close();
                writer.close();
            }
        } catch (IOException e) {
            throw new RuntimeException();
        } catch (SAXException | TransformerException | ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        new Thread(new StartServer()).run();

    }
}






