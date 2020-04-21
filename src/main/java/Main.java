import map.Coordinates;
import xmlSaver.XML;
import xmlSaver.XmlDeserializer;
import xmlSaver.XmlSerializer;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@XML
public class Main {
    private static void run_ui(ConsoleUI ui) {
        ui.users.add(ui.user1); // создаем юзера
        ui.user2.addFriend(ui.user1);
        ui.user1.setCoordinates(new Coordinates(20, 10)); // начальные координаты
        var scanner = new Scanner(System.in);
        while (true) {
            switch (scanner.nextLine()) {
                case "1" -> ui.user1.checkIn(ui.map, ui.user1.getCoordinates());
                case "2" -> XmlSerializer.saveXml(ui, "test.xml", ConsoleUI.registry);
                case "3" -> ui = (ConsoleUI) XmlDeserializer.loadXml("test.xml", ConsoleUI.registry);
                case "5" -> {
                    var t = new Thread(ui::live); // создаем юзера, как поток
                    t.start(); // запускаем поток
                }
                case "6" -> {
                    ui.users.forEach(System.out::println); // убить юзера (для консольной версии)
                    var scan2 = new Scanner(System.in);
                    var input2 = scan2.nextInt();
                    ui.users.get(input2).setAlive(false);
                    ui.users.remove(input2);
                }
                case "7" -> ui.users.forEach(x -> {
                    System.out.println(x.getName() + "'s log:"); // печатаем лог (только для консоли)
                    x.getLog().forEach(y -> System.out.println(y.getValue0() + " " + y.getValue1()));
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
