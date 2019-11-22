package server.commands;
import database.datamain.MapBuilder;
import database.datamain.Route;
import database.dataprocessing.DomWriter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.util.HashMap;

public class ResetDataBaseCommand implements Command {
    private DomWriter xmlWriter;

    ResetDataBaseCommand(){
        try {
            xmlWriter = new DomWriter();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void processCommand(BufferedReader reader, BufferedWriter writer) throws TransformerException {
        HashMap<String, Route> newMap = new MapBuilder();
        xmlWriter.writeDataBaseXmlFile(newMap);
    }
}
