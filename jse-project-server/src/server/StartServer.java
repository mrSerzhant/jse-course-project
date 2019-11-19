package server;

import database.datamain.MapBuilder;
import database.datamain.Route;
import database.datamain.User;
import database.dataprocessing.*;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

class StartServer implements Runnable {
    private DomReader xmlReader;
    private JsonWriter jsonWriter;
    private DomWriter xmlWriter;
    private JsonReader jsonReader;
    private Set<User> userList;

    StartServer() throws TransformerException, ParserConfigurationException {
        File dataBase = new File("jse-project-server\\src\\database\\databasefiles\\database.xml");
        xmlWriter = new DomWriter();

        if (!dataBase.exists()) {
            HashMap<String, Route> map = new MapBuilder();
            xmlWriter.writeDataBaseXmlFile(map);
        }

        xmlReader = new DomReader();
        jsonWriter = new JsonWriter();
        jsonReader = new JsonReader();
        userList = new HashSet<>();
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

                if (requestString.equals("GiveData")) {
                    JavaBean javaBean = xmlReader.readDataBaseXml();
                    writer.write(jsonWriter.writeStringDataBase(javaBean));
                    writer.write("\n");
                    writer.flush();
                    writer.close();
                    continue;
                }

                if (requestString.equals("GetData")) {
                    socket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    xmlWriter.writeDataBaseXmlFile(jsonReader.doJavaBean(reader.readLine()));
                    reader.close();
                    continue;
                }

                if (requestString.equals("GetUser")) {
                    socket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    User user = jsonReader.doUserBean(reader.readLine());

                    userList.addAll(xmlReader.readUsersBaseXml());

                    if (userList.contains(user)) {
                        socket = serverSocket.accept();
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        writer.write("Duplicate\n");
                        writer.flush();
                        writer.close();
                    } else {
                        userList.add(user);
                        xmlWriter.writeUsersBaseXmlFile(userList);
                        socket = serverSocket.accept();
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        writer.write("Success\n");
                        writer.flush();
                        writer.close();
                    }
                    continue;
                }

                if (requestString.equals("GetUserData")) {
                    socket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    userList.addAll(xmlReader.readUsersBaseXml());
                    User user = jsonReader.doUserBean(reader.readLine());
                    userList.remove(user);
                    userList.add(user);
                    xmlWriter.writeUsersBaseXmlFile(userList);
                    reader.close();
                    continue;
                }

                if (requestString.equals("GiveUserData")) {
                    socket = serverSocket.accept();
                    reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    User user = jsonReader.doUserBean(reader.readLine());

                    userList.addAll(xmlReader.readUsersBaseXml());

                    boolean checkEquals = false;
                    for (User userFromBase : userList) {
                        if (userFromBase.equals(user)) {
                            socket = serverSocket.accept();
                            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                            writer.write(jsonWriter.writeStringUserDateToClient(userFromBase) + "\n");
                            writer.flush();
                            writer.close();
                            checkEquals = true;
                            break;
                        }
                    }
                    if (!checkEquals) {
                        socket = serverSocket.accept();
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                        writer.write("Not found" + "\n");
                        writer.flush();
                        writer.close();
                    }
                    continue;
                }

                if (requestString.equals("GiveUsersBase")) {
                    userList.addAll(xmlReader.readUsersBaseXml());
                    writer.write(jsonWriter.writeUsersBaseListToAdmin(userList));
                    writer.write("\n");
                    writer.flush();
                    writer.close();
                    continue;
                }

                if (requestString.equals("ResetDataBase")) {
                    HashMap<String, Route> newMap = new MapBuilder();
                    xmlWriter.writeDataBaseXmlFile(newMap);
                    reader.close();
                }
            }

        } catch (IOException e) {
            throw new RuntimeException();
        } catch (SAXException | TransformerException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws ParserConfigurationException, TransformerException {
        new Thread(new StartServer()).run();

    }
}






