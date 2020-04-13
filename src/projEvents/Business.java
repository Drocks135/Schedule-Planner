package projEvents;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public class Business extends Events{
    private String Location;
    private double duration;//duration

    public Business(String name, String details, LocalDate due, String location, double duration) {
        setName(name);
        setDetails(details);
        setDue(due);
        setLocation(location);
        setDuration(duration);
    }

    public Business() {
    }


    /*
    Bunch of get methods to return attributes of homework specific events
    */
    public String getLocation() {
        return Location;
    }

    public double getDuration() {
        return duration;
    }

    public void setLocation(String location) {
        this.Location = location;
    }

    public void setDuration(double duration) {
        Errors e = Errors.getInstance();
        if (duration > 24)
            e.setError("Business events cannot last longer than 24 hours");
        else if (duration <= 0)
            e.setError("Duration of an event cannot be zero");
        else
            this.duration = duration;
    }

    public String toString() {
        String eventString = "";
        DateTimeFormatter ft = DateTimeFormatter.ofPattern("dMMyyyy");
        String date = this.getDue().format(ft);
        eventString += (this.getName() + "," + this.getDetails() + "," +
                date + "," + this.getLocation() + "," + this.getDuration() + ";");
        return eventString;
    }
}