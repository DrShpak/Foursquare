package map.buildings;

import xmlSaver.XML;

@XML
public class Edge extends Type {
    @XML
    private Point p1;
    @XML
    private Point p2;

    @SuppressWarnings("unused")
    public Edge() {
    }

    Edge(Point p1, Point p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    boolean isOnEdge(Point p) {
        var crossProduct = (p.getY() - p1.getY()) * (p2.getX() - p1.getX()) - (p.getX() - p1.getX()) * (p2.getY() - p1.getY());

        if (Math.abs(crossProduct) > 0.1) // != 0
            return false;

        var dotProduct = (p.getX() - p1.getX()) * (p2.getX() - p1.getX()) + (p.getY() - p1.getY()) * (p2.getY() - p1.getY());
        if (dotProduct < 0)
            return false;

        var squaredLengthBA = Math.pow((p2.getX() - p1.getX()), 2) + Math.pow((p2.getY() - p1.getY()), 2);
        return !(dotProduct > squaredLengthBA);
    }

    boolean checkIntersection(Point p3, Point p4) { //p1 p2 - точки ребра
        if (p1.x - p2.x == 0) {

            //найдём Xa, Ya - точки пересечения двух прямых
            double Xa = p1.x;
            double A2 = (p3.y - p4.y) / (p3.x - p4.x);
            double b2 = p3.y - A2 * p3.x;
            double Ya = A2 * Xa + b2;

            return p3.x <= Xa && p4.x >= Xa
                && Math.min(p1.y, p2.y) <= Ya
                && Math.max(p1.y, p2.y) >= Ya;
        }

        double A1 = (p1.y - p2.y) / (p1.x - p2.x);
        double A2 = (p3.y - p4.y) / (p3.x - p4.x);
        double b1 = p1.y - A1 * p1.x;
        double b2 = p3.y - A2 * p3.x;

        if (A1 == A2) {
            return false; //отрезки параллельны
        }

        //Xa - абсцисса точки пересечения двух прямых
        double Xa = (b2 - b1) / (A1 - A2);

        return (!(Xa < Math.max(p1.x, p3.x)))
            && (!(Xa > Math.min(p2.x, p4.x))); //точка Xa находится вне пересечения проекций отрезков на ось X
    }

    public Point getP1() {
        return p1;
    }

    public Point getP2() {
        return p2;
    }

    @Override
    public boolean isInside(Point p) {
        return isOnEdge(p);
    }
}
