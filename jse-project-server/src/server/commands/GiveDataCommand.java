package server.commands;

import database.dataprocessing.DomReader;
import database.dataprocessing.JavaBean;
import database.dataprocessing.JsonWriter;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public class GiveDataCommand implements Command {
    private DomReader xmlReader;
    private JsonWriter jsonWriter;

    GiveDataCommand(){
        try {
            xmlReader = new DomReader();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        jsonWriter = new JsonWriter();
    }

    @Override
    public void processCommand(BufferedReader reader, BufferedWriter writer) throws IOException, SAXException{
        JavaBean javaBean = xmlReader.readDataBaseXml();
        writer.write(jsonWriter.writeStringDataBase(javaBean));
        writer.write("\n");
    }
}
