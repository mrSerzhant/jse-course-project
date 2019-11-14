package database.dataprocessing;
import database.datamain.Route;
import database.datamain.Station;
import database.datamain.Ticket;
import database.datamain.User;
import database.datamain.utils.DateBuilder;
import org.json.JSONWriter;
import java.util.Map;
import java.util.Set;

public class JsonWriter {

    public String writeStringDataBase(JavaBean javaBean) {
        StringBuilder jsonString = new StringBuilder();

        JSONWriter jsonWriter = new JSONWriter(jsonString);
        jsonWriter.array();

        for (Map.Entry<String, Route> map : javaBean.entrySet()) {
            jsonWriter.object();

            jsonWriter.key("route").object();

            jsonWriter.key("routeName").value(map.getValue().getName());
            jsonWriter.key("routeNumber").value(map.getValue().getNumber());
            jsonWriter.key("routeTravelTime").value(map.getValue().getTravelTime());

            jsonWriter.key("station").array();

            for (Station station : map.getValue().getStops()) {
                jsonWriter.object();
                jsonWriter.key("name").value(station.getName());
                jsonWriter.key("timeArrival").value(DateBuilder.printDate(station.getArrival()));
                jsonWriter.key("timeDeparture").value(DateBuilder.printDate(station.getDeparture()));
                jsonWriter.key("stopTime").value(DateBuilder.printDate(station.getDeparture()));
                jsonWriter.endObject();
            }
            jsonWriter.endArray();

            jsonWriter.endObject();
            jsonWriter.endObject();
        }
        jsonWriter.endArray();

        return jsonString.toString();
    }

    public String writeStringUserDateToClient(User user) {
        StringBuilder jsonString = new StringBuilder();

        JSONWriter jsonWriter = new JSONWriter(jsonString);
        jsonWriter.object();
        jsonWriter.key("userName").value(user.getName());
        jsonWriter.key("userPassword").value(user.getPassword());

        jsonWriter.key("ticket").array();

        for (Ticket ticket : user.getTickets()) {
            jsonWriter.object();
            jsonWriter.key("number").value(ticket.getNumber());
            jsonWriter.key("nameWay").value(ticket.getWay());
            jsonWriter.key("timeDeparture").value(ticket.getTimeDeparture());
            jsonWriter.key("timeArrival").value(ticket.getTimeArrival());
            jsonWriter.endObject();
        }
        jsonWriter.endArray();
        jsonWriter.endObject();

        return jsonString.toString();
    }

    public String writeUsersBaseListToAdmin(Set<User> usersList) {
        StringBuilder jsonString = new StringBuilder();

        JSONWriter jsonWriter = new JSONWriter(jsonString);
        jsonWriter.array();

        for (User user : usersList) {
            jsonWriter.object();

            jsonWriter.key("user").object();
            jsonWriter.key("userName").value(user.getName());
            jsonWriter.key("userPassword").value(user.getPassword());

            jsonWriter.key("ticket").array();

            for (Ticket ticket : user.getTickets()) {
                jsonWriter.object();
                jsonWriter.key("number").value(ticket.getNumber());
                jsonWriter.key("nameWay").value(ticket.getWay());
                jsonWriter.key("timeDeparture").value(ticket.getTimeDeparture());
                jsonWriter.key("timeArrival").value(ticket.getTimeArrival());
                jsonWriter.endObject();
            }

            jsonWriter.endArray();
            jsonWriter.endObject();
            jsonWriter.endObject();
        }

        jsonWriter.endArray();

        return jsonString.toString();
    }
}













