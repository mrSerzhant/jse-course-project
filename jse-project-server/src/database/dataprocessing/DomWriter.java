package database.dataprocessing;
import database.datamain.Route;
import database.datamain.Station;
import database.datamain.Ticket;
import database.datamain.User;
import database.datamain.utils.DateBuilder;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class DomWriter {
    private DocumentBuilder documentBuilder;

    public DomWriter() throws ParserConfigurationException {
        DocumentBuilderFactory documentFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = documentFactory.newDocumentBuilder();
    }

    public void writeDataBaseXmlFile(HashMap<String, Route> map) throws TransformerException {
        Document document = documentBuilder.newDocument();

        Element root = document.createElement("routes");
        document.appendChild(root);

        for (Map.Entry<String, Route> entry : map.entrySet()) {
            Element valueMap = document.createElement("route");

            Attr numberValueAttr = document.createAttribute("routeNumber");
            numberValueAttr.setValue(entry.getValue().getNumber());
            valueMap.setAttributeNode(numberValueAttr);

            for (Station station : entry.getValue().getStops()) {
                Element elementStation = document.createElement("station");

                Attr stationName = document.createAttribute("name");
                stationName.setValue(station.getName());
                elementStation.setAttributeNode(stationName);

                Element timeArrival = document.createElement("timeArrival");
                timeArrival.appendChild(document.createTextNode(DateBuilder.dateAsString(station.getArrival())));
                elementStation.appendChild(timeArrival);

                Element timeDeparture = document.createElement("timeDeparture");
                timeDeparture.appendChild(document.createTextNode(DateBuilder.dateAsString(station.getDeparture())));
                elementStation.appendChild(timeDeparture);

                valueMap.appendChild(elementStation);
            }

            root.appendChild(valueMap);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);
        StreamResult streamResult = new StreamResult("jse-project-server\\src\\database\\databasefiles\\database.xml");
        transformer.transform(domSource, streamResult);
    }

    public void writeUsersBaseXmlFile(Set<User> usersList) throws TransformerException {
        Document document = documentBuilder.newDocument();

        Element root = document.createElement("users");
        document.appendChild(root);

        for (User user : usersList) {

            Element userElement = document.createElement("user");

            Attr usernameAttr = document.createAttribute("userName");
            usernameAttr.setValue(user.getName());
            userElement.setAttributeNode(usernameAttr);

            Attr userPasswordAttr = document.createAttribute("userPassword");
            userPasswordAttr.setValue(user.getPassword());
            userElement.setAttributeNode(userPasswordAttr);

            List<Ticket> listTickets = user.getTickets();

            for (Ticket ticket : listTickets) {
                Element ticketElement = document.createElement("ticket");

                Attr numberTicket = document.createAttribute("number");
                numberTicket.setValue(ticket.getNumber());
                ticketElement.setAttributeNode(numberTicket);

                Element nameTicket = document.createElement("nameWay");
                nameTicket.appendChild(document.createTextNode(ticket.getWay()));
                ticketElement.appendChild(nameTicket);

                Element timeDeparture = document.createElement("timeDeparture");
                timeDeparture.appendChild(document.createTextNode(ticket.getTimeDeparture()));
                ticketElement.appendChild(timeDeparture);

                Element timeArrival = document.createElement("timeArrival");
                timeArrival.appendChild(document.createTextNode(ticket.getTimeArrival()));
                ticketElement.appendChild(timeArrival);

                userElement.appendChild(ticketElement);

            }

            root.appendChild(userElement);
        }

        TransformerFactory transformerFactory = TransformerFactory.newInstance();
        Transformer transformer = transformerFactory.newTransformer();
        DOMSource domSource = new DOMSource(document);

        StreamResult streamResult = new StreamResult("jse-project-server\\src\\database\\databasefiles\\usersBase.xml");
        transformer.transform(domSource, streamResult);
    }
}
