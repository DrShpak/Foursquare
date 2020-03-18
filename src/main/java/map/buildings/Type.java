package map.buildings;

import xmlSaver.XML;

@XML
public abstract class Type {

    public abstract boolean isInside(Point p);
}
