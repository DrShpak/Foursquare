import map.Coordinates;
import map.Map;
import org.javatuples.Pair;
import social.User;
import xmlSaver.XML;
import xmlSaver.XmlSerializerRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@XML
public class ConsoleUI {

    @XML
    public Map map = new Map();
    @XML
    public List<User> users = new ArrayList<>();
    @XML
    private ArrayList<String> randomNames;

    {
        try {
            randomNames = new ArrayList<>(Arrays.asList(Files.readAllLines(Paths.get("src/main/java/names.txt")).toString().split(" ")));
        } catch (IOException e) {
            System.out.println("Something went wrong..." + e.getMessage());
        }
    }

    public static final XmlSerializerRegistry registry;

    static {
        registry = new XmlSerializerRegistry();
        try {
            registry.addClass(
                Pair.class,
                () -> Pair.with(new Object(), new Object()),
                Pair.class.getDeclaredField("val0"),
                Pair.class.getDeclaredField("val1")
            );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }

        try {
            registry.addClass(
                Date.class,
                Date::new,
                Date.class.getDeclaredField("fastTime")
            );
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        }
    }

    public ConsoleUI() {
    }

    @XML
    User user1 = new User("Putin");
    @XML
    User user2 = new User("Medvedev");

    void live() {
        var lifeCycle = System.nanoTime();
        var countOfCheckIn = 0;
        var random = new Random();
        var user = new User(randomNames.get(random.nextInt(randomNames.size())));
        user.setCoordinates(new Coordinates(random.nextInt(3), random.nextInt(3)));
        users.add(user);
        while (user.isAlive()) {
            if (TimeUnit.SECONDS.convert(System.nanoTime() - lifeCycle, TimeUnit.NANOSECONDS) >= 300) {
                user.setAlive(false);
            }

            wake();
            if (countOfCheckIn >= 3) {
                synchronized (sync) {
                    try {
                        sync.wait();
                        Thread.sleep(3000); // проснулся и немного ждет, расягиваем во времени жизнь юзера
                    } catch (InterruptedException e) {
                        System.out.println("Че-то с потоками" + e.getMessage());
                    }
                }
                countOfCheckIn = 0;
            }

            try {
                Thread.sleep(4000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user.randomCheckIn(map, user.getCoordinates());
            user.setCoordinates(new Coordinates(random.nextInt(3849) + 1, random.nextInt(2160) + 1));
            countOfCheckIn++;
        }
    }

    private static final Object sync = new Object();

    private synchronized void wake() {
            synchronized (sync) {
                sync.notify();
            }
    }

    public Map getMap() {
        return map;
    }
}
