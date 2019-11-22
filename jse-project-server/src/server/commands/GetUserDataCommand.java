package server.commands;

import database.datamain.User;
import database.dataprocessing.DomReader;
import database.dataprocessing.DomWriter;
import database.dataprocessing.JsonReader;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GetUserDataCommand implements Command {
    private Set<User> userList;
    private DomReader xmlReader;
    private DomWriter xmlWriter;
    private JsonReader jsonReader;

    GetUserDataCommand(){
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
        userList.addAll(xmlReader.readUsersBaseXml());
        User user = jsonReader.doUserBean(reader.readLine());
        userList.remove(user);
        userList.add(user);
        xmlWriter.writeUsersBaseXmlFile(userList);
    }
}
