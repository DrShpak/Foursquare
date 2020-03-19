import map.Coordinates;
import map.Map;
import org.javatuples.Pair;
import social.User;
import xmlSaver.XML;
import xmlSaver.XmlDeserializer;
import xmlSaver.XmlSerializer;
import xmlSaver.XmlSerializerRegistry;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.concurrent.TimeUnit;

@XML
public class ConsoleUI {

    @XML
    private Map map = new Map();
    @XML
    private List<User> users = new ArrayList<>();
    @XML
    private ArrayList<String> randomNames;

    {
        try {
            randomNames = new ArrayList<>(Arrays.asList(Files.readAllLines(Paths.get("src/main/java/names.txt")).toString().split(" ")));
        } catch (IOException e) {
            System.out.println("Something went wrong..." + e.getMessage());
        }
    }

    @XML
    private java.util.Map<Thread, User> threads = new HashMap<>();

    //private Thread currThread; //костыль видимо, нужен чтобы запоминать текущий поток, чтоб потом его там сохарнить в список

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
                //currThread = t;
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
        var lifeCycle = System.nanoTime();
        var countOfCheckIn = 0;
        var random = new Random();
        var user = new User(randomNames.get(random.nextInt(randomNames.size())));
        user.setCoordinates(new Coordinates(random.nextInt(3), random.nextInt(3)));
        users.add(user);
        threads.put(Thread.currentThread(), user); //overwrite value
        while (user.isAlive()) {
            if (TimeUnit.SECONDS.convert(System.nanoTime() - lifeCycle, TimeUnit.NANOSECONDS) >= 120) {
                user.setAlive(false);
            }

            var activeThreads = new Thread[Thread.activeCount()];
            Thread.enumerate(activeThreads);
            Thread mainThread = Arrays.stream(activeThreads).
                filter(x -> x.getName().equals("main")).
                findAny().orElse(null);
            assert mainThread != null;
            synchronized (mainThread) {
                try {
                    mainThread.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            System.out.println("hi there" + Thread.currentThread().isAlive());
            wake();
            if (countOfCheckIn >= 3) {
                synchronized (this) {
                    try {
                        System.out.println("я туточки");
                        wait();
                        Thread.sleep(10000); // проснулся и немного ждет, расягиваем во времени жизнь юзера
                    } catch (InterruptedException e) {
                        System.out.println("Че-то с потоками" + e.getMessage());
                    }
                }
                countOfCheckIn = 0;
            }

            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("прошел через огонь и медные трубы");
            user.randomCheckIn(map, user.getCoordinates());
            user.setCoordinates(new Coordinates(random.nextInt(3), random.nextInt(3)));
            countOfCheckIn++;
        }
    }

    private synchronized void wake() {
//        System.out.println(Thread.currentThread().toString());
        Set<Thread> threadSet = Thread.getAllStackTraces().keySet();
        threadSet.forEach(x -> System.out.println(x.toString()));
        var activeThreads = new Thread[Thread.activeCount()];
        Thread.enumerate(activeThreads);
        Thread mainThread = Arrays.stream(activeThreads).
            filter(x -> x.getName().equals("main")).
            findAny().orElse(null);
        assert mainThread != null;
            synchronized (mainThread) {
                mainThread.notifyAll();
            }
    }
}
