package data.datamain.utils;
import data.datamain.Station;
import java.util.List;

public class ListRoutesUtils {

    public static String routeNameMaker(List<Station> stationArrayList){
        StringBuilder stringBuilder = new StringBuilder();
        for(Station station : stationArrayList){
            stringBuilder.append(station.getName());
        }

        return stringBuilder.toString();
    }
}
