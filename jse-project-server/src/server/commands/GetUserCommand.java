package server.commands;

import database.datamain.User;
import database.dataprocessing.DomReader;
import database.dataprocessing.DomWriter;
import database.dataprocessing.JsonReader;
import database.dataprocessing.JsonWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.util.HashSet;
import java.util.Set;

public class GetUserCommand implements Command {
    private Set<User> userList;
    private DomReader xmlReader;
    private DomWriter xmlWriter;
    private JsonReader jsonReader;

    GetUserCommand() {
        userList = new HashSet<>();
        try {
            xmlReader = new DomReader();
            xmlWriter = new DomWriter();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        jsonReader = new JsonReader();
    }

    @Override
    public void processCommand(BufferedReader reader, BufferedWriter writer) throws IOException, SAXException, TransformerException {
        User user = jsonReader.doUserBean(reader.readLine());
        userList.addAll(xmlReader.readUsersBaseXml());

        if (userList.contains(user)) {
            writer.write("Duplicate\n");
        } else {
            userList.add(user);
            xmlWriter.writeUsersBaseXmlFile(userList);
            writer.write("Success\n");
        }
    }
}