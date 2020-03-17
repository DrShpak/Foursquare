package map.buildings;

import xmlSaver.XML;

@XML
public class Point extends Type {
    double x;
    double y;

    public Point(double x, double y) {
        this.x = x;
        this.y = y;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    @Override
    public boolean isInside(Point p) {
        return p.x == x && p.y == y;
    }
}