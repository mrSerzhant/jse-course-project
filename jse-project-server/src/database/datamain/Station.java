package database.datamain;
import database.datamain.utils.DateBuilder;
import java.util.Date;

public class Station {
    private String name;
    private Date arrival;
    private Date departure;

    public Station(String name, String arrival, String departure) {
        this.name = name;
        this.arrival = DateBuilder.createDate(arrival);
        this.departure = DateBuilder.createDate(departure);
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
}

