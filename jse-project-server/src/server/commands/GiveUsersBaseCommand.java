package server.commands;

import database.datamain.User;
import database.dataprocessing.DomReader;
import database.dataprocessing.JsonWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GiveUsersBaseCommand implements Command {
    private DomReader xmlReader;
    private JsonWriter jsonWriter;

    GiveUsersBaseCommand(){
        try {
            xmlReader = new DomReader();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        jsonWriter = new JsonWriter();
    }

    @Override
    public void processCommand(BufferedReader reader, BufferedWriter writer) throws IOException, SAXException {
        Set<User> userList = new HashSet<>(xmlReader.readUsersBaseXml());
        writer.write(jsonWriter.writeUsersBaseListToAdmin(userList));
        writer.write("\n");
    }
}
