import map.Coordinates;
import xmlSaver.XML;
import xmlSaver.XmlDeserializer;
import xmlSaver.XmlSerializer;

import java.util.Scanner;

@XML
public class Main {
    private static void run_ui(ConsoleUI ui) {
        ui.user2.addFriend(ui.user1);
        ui.user1.setCoordinates(new Coordinates(20, 10));
        var scanner = new Scanner(System.in);
        while (true) {
            switch (scanner.nextLine()) {
                case "1" -> ui.user1.checkIn(ui.map, ui.user1.getCoordinates());
                case "2" -> XmlSerializer.saveXml(ui, "test.xml", ConsoleUI.registry);
                case "3" -> ui = (ConsoleUI) XmlDeserializer.loadXml("test.xml", ConsoleUI.registry);
                case "4" -> {

                }
                case "5" -> {
                    var t = new Thread(ui::live);
                    t.start();
                }
                case "6" -> {
                    ui.users.forEach(System.out::println);
                    var scan2 = new Scanner(System.in);
                    var input2 = scan2.nextInt();
                    ui.users.get(input2).setAlive(false);
                    ui.users.remove(input2);
                }
                case "7" -> ui.users.forEach(x -> {
                    System.out.println(x.getName() + "'s log:");
                    x.getLog().forEach(System.out::println);
                    System.out.println("\n");
                });
                default -> System.out.println("usage:(1|2|3|4|5|6|7)");
            }
        }
    }

    public static void main(String[] args) {
        ConsoleUI c = new ConsoleUI();
        run_ui(c);
    }
}
