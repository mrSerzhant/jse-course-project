package data.datamain;
import data.datamain.utils.DateBuilder;
import java.util.ArrayList;
import java.util.List;

public class Route {
    private List<Station> stops = new ArrayList<>();
    private String number;

    public Route(){}

    public Route(String number){
        this.number = number;
    }

    public  List<Station> getStops() {
        return stops;
    }

    public String getNumber(){
        return number;
    }

    public void addStop(Station station){
        stops.add(station);
    }

    public String getName(){
        return String.format("%s-%s",stops.get(0).getName(),stops.get(stops.size()-1).getName());
    }

    public String getTravelTime(){
        return DateBuilder.createDifferenceTime(stops.get(0).getDeparture(),stops.get(stops.size()-1).getArrival());
    }

    public String getDepartureTime(){
        return DateBuilder.printDate(stops.get(0).getDeparture());
    }

    public String getArrival(){
        return DateBuilder.printDate(stops.get(stops.size()-1).getArrival());
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getWay(int firstIndex, int lastIndex){
        return String.format("%s - %s",stops.get(firstIndex),stops.get(lastIndex));
    }

    public void setStops(List<Station> stops) {
        this.stops = stops;
    }
}
