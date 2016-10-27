package ru.task;

import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class WalkerThread extends Thread {
    private Logger log;
    private int indexStart=0, indexEnd=1;

    public WalkerThread(int id, int indexStart, int indexEnd) {
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
        String name="Walker"+id+"("+indexStart+"-"+indexEnd+")";
        this.log=Logger.getLogger(name);
        this.setName(name);
    }

    @Override
    public void run() {
        BusStops.busStops.get(indexStart).waitOnBS(log).waitInBus(log,indexEnd);
    }
}
