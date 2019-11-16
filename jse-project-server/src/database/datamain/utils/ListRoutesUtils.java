package database.datamain.utils;
import database.datamain.Station;
import java.util.List;

public class ListRoutesUtils {

    public static String routeNameBuilder(List<Station> stationArrayList, String routeNumber){
        StringBuilder stringBuilder = new StringBuilder();
        for(Station station : stationArrayList){
            stringBuilder.append(station.getName());
        }

        return stringBuilder.toString() + routeNumber;
    }
}
