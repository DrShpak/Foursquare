package map;

import map.buildings.Polygon;
import map.buildings.Type;
import xmlSaver.XML;

@XML
public class Place {
    private Coordinates coordinates;
    private String name;
    private Type type;

    public Place(String name, Type type, Coordinates coordinates) {
        this.coordinates = coordinates;
        this.type = type;
        this.name = name;
    }

    public Place(String name, Type type) {
        this.name = name;
        this.type = type;
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public String getName() {
        return name;
    }

    public Type getType() {
        return type;
    }

    @Override
    public String toString() {
        return name;
    }
}
