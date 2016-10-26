package test;

/**
 * Created by Lenovo-PC on 25.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        try {
            new Walker(1).start();
            new Walker(2).start();
            new Bus().start();
            Thread.sleep(2000);
            new Walker(3).start();
            new Walker(4).start();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
