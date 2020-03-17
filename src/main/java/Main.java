import map.Coordinates;
import map.Map;
import social.User;
import xmlSaver.XML;
import xmlSaver.XmlSerializer;


import java.io.Console;
import java.util.Date;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

@XML
public class Main {

    @XML
    static User user1 = new User("Putin");
    @XML
    static User user2 = new User("Medvedev");

    public static void main(String[] args) {
        ConsoleUI c = new ConsoleUI();
        c.run();

//        System.out.println("\n" + user1.getLog().toString());
//        System.out.println(user2.getNotifications().toString());
    }

    private static long kek(int n) {
        long output;
        if (n == 1 || n == 0) {
            return 1;
        }
        //Recursion: Function calling itself!!
        output = kek(n - 1) * n;
        return output;
    }
}
