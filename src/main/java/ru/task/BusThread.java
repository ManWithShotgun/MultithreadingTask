package ru.task;

import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusThread extends Thread {
    private Logger log;
    private int maxSeats =2, seats=0, checked=0, oldSets=0, startIndex, startMove;
    private BusStop currentBusStop;
    private Object sleepObj=new Object();

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
            for (int index=startIndex, j=startMove;BusStops.getTotalCountWalkersAlive()!=0;){
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
//        Thread.sleep(1000);//simulation moving from bus stop i to bus stop i+1
    }

    private void step(int i){
        log.debug("going to "+i);
        BusStops.busStops.get(i).notifyAllOnBS(log,this);
//        BusStop busStop= BusStops.busStops.get(i);
//        synchronized (busStop){
//            this.currentBusStop=busStop;
//            synchronized (this){
//                this.notifyAll();
//            }
//            busStop.setBus(this);
//            busStop.notifyAll();
//            /*add waiting on bus stop*/
//        }
        log.debug("running");
    }

    public void waitDriverBus(){
        synchronized (this.sleepObj) {
            try {
                this.sleepObj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void wakeUpDriverBus(){
        synchronized (this.sleepObj) {
            this.sleepObj.notifyAll();//or .notify
        }
    }

    public synchronized void waitInBus(Logger log, int indexEnd){
        try {
            boolean move=true;
            while (move) {
                log.debug("sat in Bus");
                this.wait();
                if (this.getCurrentBusStop().getNumber() == indexEnd) {
                    this.decreaseSeats();
                    log.debug("end point");
                    BusStops.decreaseTotalCountWalkersAlive();
                    move=false;
                }
                /*count checked walkers && bus is full -> run*/
                this.checked++;
                log.debug("checked walker");
                if(checked==oldSets){
                    this.wakeUpDriverBus();
                    this.checked=0;
                }
                log.debug("not my stop");
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public synchronized void wakeUpWalkersInBus(){
        this.oldSets=seats;//not using now
        this.notifyAll();
    }

    public int  getSeats(){
        return seats;
    }

    public boolean isSeatsFree(){
        return (maxSeats - seats)!=0 ;
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

    public void setCurrentBusStop(BusStop currentBusStop) {
        this.currentBusStop = currentBusStop;
    }
}
