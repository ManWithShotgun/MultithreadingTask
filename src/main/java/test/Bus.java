package test;

/**
 * Created by Lenovo-PC on 25.10.2016.
 */
public class Bus extends Thread {

    @Override
    public void run() {
        try {
            Thread.sleep(0);
            BusStops.busStop.busThis();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
