package map;

import map.buildings.Point;
import map.buildings.Polygon;
import social.CheckIn;
import social.User;
import xmlSaver.XML;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

@XML
public class Map {
    @XML
    private List<Place> places;
    @XML
    private List<CheckIn> checkins; // история всех чекинов на карте

    @XML
    public Map() {
        places = new PlaceBuilder().buildPlaces();
        this.checkins = new ArrayList<>();
    }

    // показываем всех людей в данном конкретном месте (пришли в прак и смотри кто здесь отмечался последние 15 секунд)
    public List<User> getPeopleInThisPlace(Place place) {
        return this.checkins.stream().
            filter(x -> x.getPlace().equals(place)).
            filter(x -> TimeUnit.MILLISECONDS.toMinutes(new Date().getTime() - x.getDate().getTime()) <= 15).
            map(CheckIn::getUser).
            collect(Collectors.toList());
    }

    public List<Place> getPlaces() {
        return places;
    }

    public List<CheckIn> getCheckins() {
        return checkins;
    }
}
