package data.dataprocessing;
import data.datamain.Route;
import data.datamain.Station;
import data.datamain.utils.DateBuilder;
import org.json.JSONWriter;
import java.util.Map;

public class JsonWriter {

    public String writeStringToDataBase(JavaBean javaBean){
        StringBuilder jsonString = new StringBuilder();

        JSONWriter jsonWriter = new JSONWriter(jsonString);
        jsonWriter.array();

        for (Map.Entry<String, Route> map : javaBean.entrySet()) {
            jsonWriter.object();
            jsonWriter.key("route").object();

            jsonWriter.key("routeName").value(map.getValue().getName());
            jsonWriter.key("routeNumber").value(map.getValue().getNumber());
            jsonWriter.key("station").array();

            for (Station station : map.getValue().getStops()) {
                jsonWriter.object();
                jsonWriter.key("name").value(station.getName());
                jsonWriter.key("timeArrival").value(DateBuilder.printDate(station.getArrival()));
                jsonWriter.key("timeDeparture").value(DateBuilder.printDate(station.getDeparture()));
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













