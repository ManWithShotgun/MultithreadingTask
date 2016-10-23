package ru.task;

import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusThread extends Thread {
    private Logger log;
    private int seats=2, startIndex, startMove;
    private BusStop currentBusStop;

    public BusThread(int startIndex, int startMove) {
        this.startIndex=startIndex;
        this.startMove=startMove;
        log=Logger.getLogger("Bus");
        this.setName("Bus");
    }

    @Override
    public void run() {
        try {
            log.debug("Start");
            int countBusStop=BusStops.busStops.size();
            for (int step=0, index=startIndex, j=startMove;step<30;step++){
                //region calc step
                if(index==countBusStop-1)
                    j=-1;
                if(index==0)
                    j=1;
                //endregion
                move();
                step(index);
                index+=j;
            }
            log.debug("End");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void move() throws InterruptedException {
        Thread.sleep(1000);
    }

    private void step(int i){
        log.debug("going to "+i);
        BusStop busStop= BusStops.busStops.get(i);
        synchronized (busStop){
            this.currentBusStop=busStop;
            synchronized (this){
                this.notifyAll();
            }
            busStop.setBus(this);
            busStop.notifyAll();
        }
        log.debug("running");
    }

    public int getSeatsFree(){
        return seats;
    }

    public void increaseSeats(){
        this.seats++;
    }

    public void decreaseSeats(){
        this.seats--;
    }

    public BusStop getCurrentBusStop(){
        return this.currentBusStop;
    }
}
