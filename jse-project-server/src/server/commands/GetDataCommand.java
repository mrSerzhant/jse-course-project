package server.commands;
import database.dataprocessing.DomWriter;
import database.dataprocessing.JsonReader;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GetDataCommand implements Command {
    private DomWriter xmlWriter;
    private JsonReader jsonReader;

    GetDataCommand(){
        try {
            xmlWriter = new DomWriter();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        jsonReader = new JsonReader();
    }

    @Override
    public void processCommand(BufferedReader reader, BufferedWriter writer) throws IOException, TransformerException {
        xmlWriter.writeDataBaseXmlFile(jsonReader.doJavaBean(reader.readLine()));
    }
}
