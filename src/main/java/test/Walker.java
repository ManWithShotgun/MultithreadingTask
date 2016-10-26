package test;

/**
 * Created by Lenovo-PC on 25.10.2016.
 */
public class Walker extends Thread {
    private int index;

    public Walker(int index) {
        this.index = index;
    }

    @Override
    public void run() {
        System.out.println("W"+index+": run");
        BusStops.busStop.waitBus();
        System.out.println("W"+index+": end");
    }
}
