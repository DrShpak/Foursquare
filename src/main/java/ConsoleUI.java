import map.Coordinates;
import map.Map;
import social.User;
import xmlSaver.XML;
import xmlSaver.XmlDeserializer;
import xmlSaver.XmlSerializer;

import java.util.Scanner;

@XML
public class ConsoleUI {

    @XML
    User user1 = new User("Putin");
    @XML
    User user2 = new User("Medvedev");

    public void run() {
        Map map = new Map();
        user2.addFriend(user1);
        user1.setCoordinates(new Coordinates(20, 10));
        while (true) {
            var scan = new Scanner(System.in);
            var input = scan.nextLine();
            if (input.equals("1")) {
                user1.checkIn(map, user1.getCoordinates());
                System.out.println("\n" + user1.getLog().toString());
                System.out.println(user2.getNotifications().toString());
            }
            if (input.equals("2"))
                XmlSerializer.saveXml(this, "test.xml");
            if (input.equals("3"))
                XmlDeserializer.loadXml("test.xml");
            if (input.equals("4"))
                break;
        }
    }
}
