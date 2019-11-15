package database.dataprocessing;
import database.datamain.Route;
import database.datamain.Station;
import database.datamain.Ticket;
import database.datamain.User;
import database.datamain.utils.ListRoutesUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DomReader {
    private DocumentBuilder documentBuilder;
    private File file;

    public DomReader() throws ParserConfigurationException {
        DocumentBuilderFactory builderFactory = DocumentBuilderFactory.newInstance();
        documentBuilder = builderFactory.newDocumentBuilder();
        file = new File("jse-project-server\\src\\database\\databasefiles\\usersBase.xml");
    }

    public JavaBean readDataBaseXml() throws IOException, SAXException {
        Document document = documentBuilder.parse("jse-project-server\\src\\database\\databasefiles\\database.xml");
        NodeList nodeList = document.getElementsByTagName("route");

        JavaBean javaBean = new JavaBean();
        int nodeListLength = nodeList.getLength();

        for (int i = 0; i < nodeListLength; i++) {
            Node node = nodeList.item(i);

            Element routeNode = (Element) node;
            String routeNumber = routeNode.getAttribute("routeNumber");

            Route route = new Route();
            route.setNumber(routeNumber);

            List<Station> stationList = new ArrayList<>();

            NodeList stationNodeList = routeNode.getElementsByTagName("station");
            int stationNodeListLength = stationNodeList.getLength();

            for (int j = 0; j < stationNodeListLength; j++) {
                Node stationNode = stationNodeList.item(j);

                Element stationElement = (Element) stationNode;

                String stationName = stationElement.getAttribute("name");

                NodeList childNodes = stationElement.getChildNodes();

                Element timeArrival = (Element) childNodes.item(0);
                Element timeDeparture = (Element) childNodes.item(1);

                String arrival = timeArrival.getTextContent();
                String departure = timeDeparture.getTextContent();

                Station station = new Station(stationName, arrival, departure);
                stationList.add(station);
            }

            route.setStops(stationList);
            javaBean.put(ListRoutesUtils.routeNameMaker(stationList), route);
        }

        return javaBean;
    }

    public List<User> readUsersBaseXml() throws IOException, SAXException {
        List<User> userList = new ArrayList<>();

        if(!file.exists()){
            return userList;
        }

        Document document = documentBuilder.parse("jse-project-server\\src\\database\\databasefiles\\usersBase.xml");
        NodeList nodeList = document.getElementsByTagName("user");
        int nodeListLength = nodeList.getLength();

        for (int i = 0; i < nodeListLength; i++) {
            Node node = nodeList.item(i);

            Element userNode = (Element) node;
            String userName = userNode.getAttribute("userName");
            String userPassword = userNode.getAttribute("userPassword");

            User user = new User(userName, userPassword);

            NodeList ticketsNodeList = userNode.getElementsByTagName("ticket");
            int ticketsNodeListLength = ticketsNodeList.getLength();

            for (int j = 0; j < ticketsNodeListLength; j++) {
                Node stationNode = ticketsNodeList.item(j);

                Element ticketElement = (Element) stationNode;

                String ticketNumber = ticketElement.getAttribute("number");

                NodeList childNodes = ticketElement.getChildNodes();

                Element nameTicket = (Element) childNodes.item(0);
                Element timeDeparture = (Element) childNodes.item(1);
                Element timeArrival = (Element) childNodes.item(2);

                String nameWay = nameTicket.getTextContent();
                String departure = timeDeparture.getTextContent();
                String arrival = timeArrival.getTextContent();

                Ticket ticket = new Ticket(ticketNumber, nameWay, departure, arrival);
                user.addTicket(ticket);
            }

            userList.add(user);
        }
        return userList;
    }
}
