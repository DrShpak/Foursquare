package social;

import map.Coordinates;
import map.Map;
import map.Place;
import map.buildings.Point;
import org.javatuples.Pair;
import xmlSaver.XML;

import java.util.*;
import java.util.stream.Collectors;

@XML
public class User {
    @XML
    private List<User> friends;
    @XML
    private List<Pair<Place, String>> log;
    @XML
    private List<String> notifications;
    @XML
    private Coordinates coordinates;

    private boolean isAlive = true;

    @XML
    @SuppressWarnings("FieldCanBeLocal")
    private String name;

    @XML
    private int id;

    @SuppressWarnings("unused")
    public User() {
    }

    public User(String name) {
        friends = new ArrayList<>();
        log = new ArrayList<>();
        notifications = new ArrayList<>();
        this.name = name;
        this.id = new Random().nextInt(Integer.MAX_VALUE);
    }

    public void checkIn(Map map, Coordinates coordinates) {
        var currPlaces = map.getPlaces().stream().
            filter(x -> x.getType().isInside(new Point(coordinates.getX(), coordinates.getY()))).
            collect(Collectors.toList());


        if (!currPlaces.isEmpty()) {
            System.out.println("Выберите, где вы хотите зачикиниться: ");
            int[] i = {1};
            currPlaces.forEach(x -> System.out.println(i[0]++ + ". " + x.getName()));
            var scanner = new Scanner(System.in);
            var input = scanner.nextInt();
            var currPlace = currPlaces.get(input - 1);
            var checkIn = new CheckIn(currPlaces.get(input - 1), this, new Date());
            map.getCheckins().add(checkIn);
            sentNotification(checkIn);
            addInLog(currPlace);
            System.out.println("Check-in has done successfully");
        } else
            System.out.println("Here is no place to check-in!");
    }

    //для юзеров, которые в роли отдельных поток сами делают чекины в фоне
    public void randomCheckIn(Map map, Coordinates coordinates) {
        var currPlaces = map.getPlaces().stream().
            filter(x -> x.getType().isInside(new Point(coordinates.getX(), coordinates.getY()))).
            collect(Collectors.toList());

        if (!currPlaces.isEmpty()) {
            var input = new Random().nextInt(currPlaces.size());
            var currPlace = currPlaces.get(input);
            var checkIn = new CheckIn(currPlaces.get(input), this, new Date());
            map.getCheckins().add(checkIn);
            sentNotification(checkIn);
            addInLog(currPlace);
        }
    }

    private void sentNotification(CheckIn checkIn) {
        this.friends.forEach(x ->
            x.notifications.add(checkIn.getUser().name + " is at "
                + checkIn.getPlace().getName() + ", " + checkIn.getDate().toString()));
    }

    private void addInLog(Place place) {
        log.add(new Pair<>(place, "at " + new Date().toString()));
    }

    public void addFriend(User user) {
        if (!this.friends.contains(user)) {
            this.friends.add(user);
            user.friends.add(this);
        }
    }

    public Coordinates getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(Coordinates coordinates) {
        this.coordinates = coordinates;
    }

    public List<Pair<Place, String>> getLog() {
        return log;
    }

    public List<String> getNotifications() {
        return notifications;
    }

    public List<User> getFriends() {
        return friends;
    }

    public void setAlive(boolean flag) {
        isAlive = flag;
    }

    public boolean isAlive() {
        return isAlive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof User)) return false;
        User user = (User) o;
        return id == user.id &&
            Objects.equals(name, user.name);
    }

    @Override
    public String toString() {
        return name;
    }

    public String getName() {
        return name;
    }
}
