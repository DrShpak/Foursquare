package map;

import map.buildings.Type;
import xmlSaver.XML;

@XML
public class Place {
//    private Coordinates coordinates;
    @XML
    private String name;
    @XML
    private Type type;

    @SuppressWarnings("unused")
    public Place() {
    }

    public Place(String name, Type type) {
        this.name = name;
        this.type = type;
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
