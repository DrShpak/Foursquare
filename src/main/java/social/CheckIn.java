package social;

import map.Place;
import xmlSaver.XML;

import java.util.Date;

@XML
public class CheckIn {
    @XML
    private Place place;
    @XML
    private User user;
    @XML
    private Date date;

    // чекин представляет из себя место, челика и дату чекина
    @SuppressWarnings("unused")
    public CheckIn() {
    }

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
