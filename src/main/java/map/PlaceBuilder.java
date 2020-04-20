package map;

import map.buildings.Point;
import map.buildings.Polygon;
import org.javatuples.Pair;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Function;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class PlaceBuilder {
    private static final HashMap<Pattern, Function<String, Place>> parsingRegex;

    public static <T> Stream<Pair<T, T>> toPairs(final Stream<T> s) {
        final AtomicInteger counter = new AtomicInteger(0);
        return s.collect(
            Collectors.groupingBy(item -> {
                final int i = counter.getAndIncrement();
                return (i % 2 == 0) ? i : i - 1;
            })).values().stream().map(a -> Pair.with(a.get(0), (a.size() == 2 ? a.get(1) : null)));
    }

    static {
        parsingRegex = new HashMap<>();
        final var pointPattern = Pattern.compile
            ("^Point: \"(.+?)\" (\\([+-]?([0-9]*[.])?[0-9]+, [+-]?([0-9]*[.])?[0-9]+\\))$");
        final var polygonPattern = Pattern.compile
            ("^Polygon: \"(.+?)\" (\\((\\([+-]?([0-9]*[.])?[0-9]+, [+-]?([0-9]*[.])?[0-9]+\\)(, |\\)))+)$");
        final var polygonVerticesPattern = Pattern.compile
            ("\\(([+-]?([0-9]*[.])?[0-9]+), ([+-]?([0-9]*[.])?[0-9]+)\\)");

        final Function<String, Stream<Point>> coordinatesProvider = coordinates -> toPairs(
            polygonVerticesPattern.matcher(coordinates).
                results().
                flatMap(x -> Stream.of(x.group(1), x.group(3))).
                map(Double::parseDouble)
        ).map(x -> new Point(x.getValue0(), x.getValue1()));

        parsingRegex.put(
            pointPattern,
            str -> {
                var matcher = pointPattern.matcher(str);
                //noinspection ResultOfMethodCallIgnored
                matcher.find();
                var name = matcher.group(1);
                var coordinates = coordinatesProvider.
                    andThen(x -> x.findFirst().orElse(null)).
                    apply(matcher.group(2));

                return new Place(
                    name,
                    coordinates
                );
            }
        );
        parsingRegex.put(
            polygonPattern,
            str -> {
                var matcher = polygonPattern.matcher(str);
                //noinspection ResultOfMethodCallIgnored
                matcher.find();
                var name = matcher.group(1);
                var vertices = coordinatesProvider.
                    andThen(x -> new ArrayList<>(x.collect(Collectors.toList()))).
                    apply(matcher.group(2));

                return new Place(
                    name,
                    new Polygon(vertices)
                );
            }
        );
    }

    private final List<String> placesList;

    public PlaceBuilder() {
        var placesStream = this.getClass().getResourceAsStream("/places.txt");
        this.placesList = new BufferedReader(new InputStreamReader(
            placesStream,
            StandardCharsets.UTF_8
        )).lines().collect(Collectors.toList());
    }

    public List<Place> buildPlaces() {
        return this.placesList.stream().
            flatMap(x -> parsingRegex.
                entrySet().
                stream().
                filter(y -> y.getKey().matcher(x).find()).
                map(y -> y.getValue().apply(x))
            ).collect(Collectors.toList());
    }
}
