package database.datamain;
import database.datamain.utils.DateBuilder;
import java.util.Date;

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

    public void setName(String name) {
        this.name = name;
    }

    public void setArrival(Date arrival) {
        this.arrival = arrival;
    }

    public void setDeparture(Date departure) {
        this.departure = departure;
    }

    public void setStopTime(String stopTime) {
        this.stopTime = stopTime;
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
}

