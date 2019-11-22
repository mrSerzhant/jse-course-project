package server.commands;
import database.datamain.User;
import database.dataprocessing.DomReader;
import database.dataprocessing.JsonReader;
import database.dataprocessing.JsonWriter;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

public class GiveUserDataCommand implements Command {
    private Set<User> userList;
    private DomReader xmlReader;
    private JsonWriter jsonWriter;
    private JsonReader jsonReader;

    GiveUserDataCommand(){

        try {
            xmlReader = new DomReader();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        jsonWriter = new JsonWriter();
        jsonReader = new JsonReader();
    }

    @Override
    public void processCommand(BufferedReader reader, BufferedWriter writer) throws IOException, SAXException, ParserConfigurationException, TransformerException {
        User user = jsonReader.doUserBean(reader.readLine());
        userList = new HashSet<>();
        userList.addAll(xmlReader.readUsersBaseXml());

        boolean checkEquals = false;
        for (User userFromBase : userList) {
            if (userFromBase.equals(user)) {
                writer.write(jsonWriter.writeStringUserDateToClient(userFromBase) + "\n");
                checkEquals = true;
                break;
            }
        }
        if (!checkEquals) {
            writer.write("Not found" + "\n");
        }
    }
}