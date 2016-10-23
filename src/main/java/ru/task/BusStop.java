package ru.task;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusStop {
    private BusThread busThread;//current bus in bus stop
    private int number;

    public BusStop(int number) {
        this.number = number;
    }

    public BusThread getBus() {
        return busThread;
    }

    public void setBus(BusThread busThread) {
        this.busThread = busThread;
    }

    public int getNumber() {
        return number;
    }
}
