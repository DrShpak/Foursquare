package multiThread;

public class MainApp {

    public static void main(String[] args) {
        System.out.println("Main thread started...");
        Test myThread = new Test();
        Thread t = new Thread(myThread, "MyThread");
        t.start();
        synchronized (t) {
            t.notify();
        }
    }
}
