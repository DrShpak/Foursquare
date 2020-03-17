package social;

import map.Place;
import xmlSaver.XML;

import java.util.Date;

@XML
public class CheckIn {
    private Place place;
    private User user;
    private Date date;

    public CheckIn(Place place, User user, Date date) {
        this.place = place;
        this.user = user;
        this.date = date;
    }

    public Place getPlace() {
        return place;
    }

    public User getUser() {
        return user;
    }

    public Date getDate() {
        return date;
    }
}
