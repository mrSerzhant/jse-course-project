package data.dataprocessing;
import data.datamain.Route;
import data.datamain.Station;
import data.datamain.Ticket;
import data.datamain.User;
import data.datamain.utils.ListRoutesUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;
import java.io.StringReader;
import java.util.HashSet;

public class JsonReader {

    public JavaBean doJavaBean(String jsonString) {
        JavaBean javaBean = new JavaBean();

        StringReader stringReader = new StringReader(jsonString);

        JSONTokener jsonTokener = new JSONTokener(stringReader);

        JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();

        int routesArrayLength = jsonArray.length();

        for (int i = 0; i < routesArrayLength; i++) {
            JSONObject routeJsonObject = jsonArray.getJSONObject(i);

            JSONObject valueJsonObject = routeJsonObject.getJSONObject("route");

            String routeNumber = valueJsonObject.getString("routeNumber");
            Route route = new Route(routeNumber);

            JSONArray stationsArray = valueJsonObject.getJSONArray("station");

            int stationsArrayLength = stationsArray.length();

            for (int j = 0; j < stationsArrayLength; j++) {
                JSONObject stationObject = stationsArray.getJSONObject(j);

                String nameStation = stationObject.getString("name");
                String timeArrival = stationObject.getString("timeArrival");
                String timeDeparture = stationObject.getString("timeDeparture");

                Station station = new Station(nameStation, timeArrival, timeDeparture);
                route.addStop(station);

            }

            javaBean.put(ListRoutesUtils.routeNameMaker(route.getStops(), routeNumber), route);

        }
        return javaBean;
    }

    public HashSet<User> doUsersList(String jsonStringUsersList) {
        HashSet<User> userList = new HashSet<>();
        StringReader stringReader = new StringReader(jsonStringUsersList);

        JSONTokener jsonTokener = new JSONTokener(stringReader);

        JSONArray jsonArray = (JSONArray) jsonTokener.nextValue();

        int usersArrayLength = jsonArray.length();

        for (int i = 0; i < usersArrayLength; i++) {
            JSONObject userJsonObject = jsonArray.getJSONObject(i);

            JSONObject valueJsonObject = userJsonObject.getJSONObject("user");
            String userName = valueJsonObject.getString("userName");
            String userPassword = valueJsonObject.getString("userPassword");
            User user = new User(userName, userPassword);

            JSONArray ticketsArray = valueJsonObject.getJSONArray("ticket");

            int ticketsArrayLength = ticketsArray.length();

            for (int j = 0; j < ticketsArrayLength; j++) {
                JSONObject stationObject = ticketsArray.getJSONObject(j);

                String numberTicket = stationObject.getString("number");
                String nameTicket = stationObject.getString("nameWay");
                String timeDeparture = stationObject.getString("timeDeparture");
                String timeArrival = stationObject.getString("timeArrival");

                Ticket ticket = new Ticket(numberTicket, nameTicket, timeDeparture, timeArrival);
                user.addTicket(ticket);
            }

            userList.add(user);
        }
        return userList;
    }
}

