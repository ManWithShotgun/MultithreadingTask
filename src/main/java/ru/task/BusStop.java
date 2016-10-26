package ru.task;


import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusStop {
    private Logger log=Logger.getLogger("BS");
    private BusThread busThread;//current bus in bus stop
    private int number, countWalkers=0;

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

    public synchronized BusThread waitOnBS(Logger log){
        BusStops.increaseTotalCountWalkersAlive();
        this.countWalkers++;
        try {
            while (true) {
                log.debug("wait Bus...");
                this.wait();
                log.debug("ooo Bus!!!");
                if (busThread!=null && busThread.isSeatsFree()) {/*add check number bus*/
                    busThread.increaseSeats();
                    this.countWalkers--;
                    log.debug("I sat");
                    if(this.countWalkers==0) busThread.wakeUpDriverBus();
                    return busThread;
                }
                /*else bus full*/
                /*count checked walkers on bus stop==all -> run*/
                if(busThread!=null) {
                    busThread.wakeUpDriverBus();
                    busThread=null;
                }
                log.debug("oh, no places");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void notifyAllOnBS(Logger log,BusThread busThread){
        synchronized (this) {
            busThread.setCurrentBusStop(this);
            if (busThread.getSeats() != 0) {
                synchronized (busThread) {
                    busThread.wakeUpWalkersInBus();
                }
                busThread.waitDriverBus();//wait till everyone leaves the bus
            }
            log.debug("Loading Walkers...");
            this.busThread = busThread;
            this.notifyAll();
        }
        if(this.countWalkers!=0)
            busThread.waitDriverBus();//wait till everyone enters the bus
        /*add waiting on bus stop || condition run*/
        /*may be: move the logic into BusThread*/
//            while (busThread.getSeatsFree() != 0 && this.countWalkers != 0) {
//                //region waiting
//                try {
//                    log.debug("Wait: " + busThread.getSeatsFree() + " | " + this.countWalkers);
//                    Thread.sleep(40);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                //endregion
//            }

    }
}
