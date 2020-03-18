import map.Coordinates;
import map.Map;
import org.javatuples.Pair;
import social.User;
import xmlSaver.XML;
import xmlSaver.XmlDeserializer;
import xmlSaver.XmlSerializer;
import xmlSaver.XmlSerializerRegistry;

import java.util.*;

@XML
public class ConsoleUI {

    @XML
    private Map map = new Map();
    @XML
    private List<User> users = new ArrayList<>();
    @XML
    private List<String> randomNames = new ArrayList<>(Arrays.asList("Jim", "Lola", "Kent"));
    @XML
    private java.util.Map<Thread, User> threads = new HashMap<>();

    private Thread currThread;

    private static final XmlSerializerRegistry registry;

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

    public void run() {
        user2.addFriend(user1);
        user1.setCoordinates(new Coordinates(20, 10));
        while (true) {
            var scan = new Scanner(System.in);
            var input = scan.nextLine();
            if (input.equals("1")) {
                user1.checkIn(map, user1.getCoordinates());
//                System.out.println("\n" + user1.getLog().toString());
//                System.out.println(user2.getNotifications().toString());
            }
            if (input.equals("2"))
                XmlSerializer.saveXml(this, "test.xml", registry);
            if (input.equals("3"))
                XmlDeserializer.loadXml("test.xml", registry);
            if (input.equals("4"))
                break;
            if (input.equals("5")) {
                var t = new Thread(this::live);
                t.start();
                threads.put(t, null);
                currThread = t;
            }
            if (input.equals("6")) {
                users.forEach(System.out::println);
                var scan2 = new Scanner(System.in);
                var input2 = scan2.nextInt();
                users.get(input2).setAlive(false);
                users.remove(input2);
            }
            if (input.equals("7")) {
                users.forEach(x -> {
                    System.out.println(x.getName() + "'s log:");
                    x.getLog().forEach(System.out::println);
                    System.out.println("\n");
                });
            }

        }
    }

    private void live() {
        var random = new Random();
        var user = new User(randomNames.get(random.nextInt(randomNames.size())));
        user.setCoordinates(new Coordinates(random.nextInt(3), random.nextInt(3)));
        users.add(user);
        threads.put(currThread, user); //overwrite value
        while (user.isAlive()) {
            try {
                Thread.sleep(7000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            user.randomCheckIn(map, user.getCoordinates());
            user.setCoordinates(new Coordinates(random.nextInt(3), random.nextInt(3)));
        }
    }
}
