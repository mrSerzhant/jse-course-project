package data.dataprocessing;
import data.datamain.Ticket;
import data.datamain.User;
import org.json.JSONWriter;

public class JsonWriter {

    public String writeStringUserDateToServer(User user){
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
}













