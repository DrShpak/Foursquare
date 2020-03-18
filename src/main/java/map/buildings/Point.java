package map.buildings;

import xmlSaver.XML;

@XML
public class Point extends Type {
    @XML
    double x;
    @XML
    double y;

    @SuppressWarnings("unused")
    public Point() {
    }

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    double getX() {
        return x;
    }

    double getY() {
        return y;
    }

    @Override
    public boolean isInside(Point p) {
        return p.x == x && p.y == y;
    }
}