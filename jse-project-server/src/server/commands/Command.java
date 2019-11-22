package server.commands;
import org.xml.sax.SAXException;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;

public interface Command {
    void processCommand(BufferedReader reader, BufferedWriter writer) throws IOException, SAXException, ParserConfigurationException, TransformerException;
}
