package data.datamain;
import data.datamain.utils.DateBuilder;
import java.util.Date;
import java.util.Objects;

public class Station {
    private String name;
    private Date arrival;
    private Date departure;
    private String stopTime;

    public Station(String name, String arrival, String departure) {
        this.name = name;
        this.arrival = DateBuilder.createDate(arrival);
        this.departure = DateBuilder.createDate(departure);
        this.stopTime = DateBuilder.createDifferenceTime(this.arrival,this.departure);
    }

    public String toString(){
        return name;
    }

    public String getName(){
        return name;
    }

    public Date getArrival(){
        return arrival;
    }

    public Date getDeparture(){
        return departure;
    }

    public String getStopTime(){
        return stopTime;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Station station = (Station) o;
        return Objects.equals(name, station.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name);
    }


}



