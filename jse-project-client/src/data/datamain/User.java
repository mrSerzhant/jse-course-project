package data.datamain;
import java.util.ArrayList;
import java.util.List;

public class User {
    private String name;
    private String password;
    private List<Ticket> tickets;

    public User(String name, String password) {
        this.name = name;
        this.password = password;
        tickets = new ArrayList<>();
    }

    public void addTicket(Ticket ticket) {
        tickets.add(ticket);
    }

    public String getName(){
        return name;
    }

    public String getPassword(){
        return password;
    }

    public List<Ticket> getTickets(){
        return tickets;
    }
}
