package data.datamain;
import data.datamain.utils.DateBuilder;
import java.util.Random;

public class Ticket {
    private String number;
    private String nameWay;
    private String timeDeparture;
    private String timeArrival;

    public Ticket(Route route, int indexFirstStation, int indexLastStation) {
        this.nameWay = setNameWay(indexFirstStation, indexLastStation, route);
        this.number = setNumber();
        this.timeDeparture = setTimeDeparture(indexFirstStation, route);
        this.timeArrival = setTimeArrival(indexLastStation, route);
    }

    public Ticket(String number, String nameWay, String timeDeparture, String timeArrival){
        this.number = number;
        this.nameWay = nameWay;
        this.timeDeparture = timeDeparture;
        this.timeArrival = timeArrival;
    }

    private String setNameWay(int indexFirstStation, int indexLastStation, Route route) {
        return String.format("%s - %s", route.getStops().get((indexFirstStation)), route.getStops().get((indexLastStation)));
    }

    private String setTimeDeparture(int firstIndex, Route route) {
        return DateBuilder.printDate(route.getStops().get(firstIndex).getDeparture());
    }

    private String setTimeArrival(int lastIndex, Route route) {
        return DateBuilder.printDate(route.getStops().get(lastIndex).getArrival());
    }

    private String setNumber() {
        Random random = new Random();
        int randomNumber = random.nextInt(999);

        String firstLetter = String.valueOf(nameWay.charAt(0));
        String lastLetter = String.valueOf(nameWay.charAt(nameWay.length() - 1));

        return String.format("%s%d%s", firstLetter, randomNumber, lastLetter);
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
