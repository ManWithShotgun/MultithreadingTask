package ru.task;


import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusStop {
    private BusThread busThread;//current bus in bus stop
    private int number, countWalkers=0, oldCountWalkers,checkWalkers;

    /*Логика пассажиров на остановке*/
    public synchronized BusThread waitOnBS(Logger log, int vector){
        this.countWalkers++;//увел. кол-во людей на остановке
        boolean wait=true;
        try {
            while (wait) {
                log.debug("wait Bus...");
                this.wait();
                log.debug("ooo "+busThread.getName());
                if (busThread.isSeatsFree() && busThread.getVector()==vector) {
                    busThread.increasePassengers();
                    this.countWalkers--;
                    log.debug("I sat");
                    wait=false;
                }
                /*case else bus full*/
                checkWalkers++;
                if(checkWalkers==oldCountWalkers){
                    checkWalkers=0;
                    busThread.wakeUpDriverBus();
//                    if(wait)
//                        busThread=null;
                }
                if (!wait){
                    return busThread;
                }
                else {
                    log.debug("oh, no places");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void notifyAllOnBS(Logger log,BusThread busThread){
        synchronized (this) {
            busThread.setCurrentBusStop(this);
            if (busThread.getPassengers() != 0) {
                log.debug("Wait drop walkers...");
                busThread.wakeUpWalkersInBus();
                busThread.waitDriverBus();//wait till everyone leaves the bus
                log.debug("Wait drop walkers...end");
            }
            log.debug("Loading Walkers..."+this.countWalkers+" "+number);
            this.busThread = busThread;
            this.oldCountWalkers=countWalkers;
            busThread.statusSleep();
            this.notifyAll();
        }
        if(this.countWalkers!=0)
            busThread.waitDriverBus();//wait till everyone enters the bus


    }

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
