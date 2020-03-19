package map.buildings;

import xmlSaver.XML;

import java.util.ArrayList;
import java.util.List;

@XML
public class Polygon extends Type {
    @XML
    private List<Edge> edges;

    public Polygon(ArrayList<Point> points) {
        this.edges = new ArrayList<>();
        var p1 = points.get(0);
        for (int i = 0; i < points.size() - 1; i++) {
            edges.add(new Edge(points.get(i), points.get(i + 1)));
        }
        edges.add(new Edge(points.get(points.size() - 1), p1));
    }

    @SuppressWarnings("unused")
    public Polygon() {
    }

    @Override
    public boolean isInside(Point p) {
        return getCountOfIntersections(p) % 2 != 0 || isOnBorder(p);
    }

    private boolean isOnBorder(Point p) {
        return edges.stream().
            anyMatch(x -> x.isOnEdge(p));
    }

    private int getCountOfIntersections(Point p) {
        return (int) edges.stream().
            filter(x -> x.checkIntersection(new Point(0, p.y), p)).
            count();
    }
}
