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
    private List<CheckIn> checkins; //история всех чекинов на карте
    @XML


    public Map() {
        places = new PlaceBuilder().buildPlaces();
        places.add(new Place("Кинотеатр \"Дружба\"",
            new Polygon(new ArrayList<>(Arrays.asList(new Point(10, 10), new Point(15, 15), new Point(20, 10))))));
        places.add(new Place("Парк \"Динамо\"",
            new Polygon(new ArrayList<>(Arrays.asList(new Point(5, 5), new Point(15, 40), new Point(40, 5))))));
        this.checkins = new ArrayList<>();
    }

    //для графики
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
