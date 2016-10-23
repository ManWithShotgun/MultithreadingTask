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
        String name="Walker"+id+":"+indexStart+"-"+indexEnd;
        this.log=Logger.getLogger(name);
        this.setName(name);
    }

    @Override
    public void run() {
        try {
            BusThread busThread;
            synchronized (BusStops.busStops.get(indexStart)) {
                while (true) {
                    log.debug("wait Bus...");
                    BusStops.busStops.get(indexStart).wait();
                    log.debug("ooo Bus!!!");
                    busThread = BusStops.busStops.get(indexStart).getBus();
                    if (busThread.getSeatsFree() != 0) {
                        busThread.decreaseSeats();
                        log.debug("I'm sat");
                        break;
                    }
                    log.debug("oh, no places");
                }
            }
            synchronized (busThread) {
                while (true) {
                    log.debug("sat in Bus");
                    busThread.wait();
                    if (busThread.getCurrentBusStop().getNumber() == indexEnd) {
                        busThread.increaseSeats();
                        log.debug("end point");
                        break;
                    }
                    log.debug("not my stop");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
