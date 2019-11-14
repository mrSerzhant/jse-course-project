package data.datamain;

public class Ticket {
    private String number;
    private String nameWay;
    private String timeDeparture;
    private String timeArrival;

    public Ticket(String number, String nameWay, String timeDeparture, String timeArrival){
        this.number = number;
        this.nameWay = nameWay;
        this.timeDeparture = timeDeparture;
        this.timeArrival = timeArrival;
    }

    public String getNumber(){
        return number;
    }

    public String getWay(){
        return nameWay;
    }

    public String getTimeDeparture(){
        return timeDeparture;
    }

    public String getTimeArrival(){
        return timeArrival;
    }

}
