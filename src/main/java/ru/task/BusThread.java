package ru.task;

import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusThread extends Thread {

    enum Status{RUN,STOP,SLEEP};

    class RoutePoint{
        int index, countExit=0;

        public RoutePoint(int index) {
            this.index = index;
        }

        public int getIndex() {
            return index;
        }

        public int getCountExit() {
            return countExit;
        }

        public void increaseCountExit(){
            this.countExit++;
        }

        public void decreaseCountExit(){
            this.countExit--;
        }
    }

    /**
     * maxIdleCount - кол-во остановок без пассажиров.
     * Если это значение больше полного марштура +2 остановки, то атобус завершает свою работу.
     *
     * sleepObj - объект на котором ждет автобус во время выгрузки и загрузки пассажиров
     * */
    private Logger log;
    private int maxSeats =2, passengers =0, checkCount =0, oldCountPassengers =0,
            startIndex, vector, busNumber, idleCount, maxIdleCount, speed;
    private BusStop currentBusStop;
    private Object sleepObj=new Object();
    private Status status=Status.RUN;

    public BusThread(int busNumber, int maxSeats, int startIndex, int vector, int speed) {
        this.maxSeats=maxSeats;
        this.busNumber=busNumber;
        this.startIndex=startIndex;
        this.vector = vector;
        this.speed=speed;
        log=Logger.getLogger("Bus"+busNumber);
        this.setName("Bus"+busNumber);
    }

    @Override
    public void run() {
        try {
            log.debug("Start");
            int countBusStop=BusStops.busStops.size();
            maxIdleCount=countBusStop*2+2;//макс. пустых остановок= полный маршрут +2 ост.
            boolean move=true;
            for (int index = startIndex, j = vector; move;){

                //region Условие движения по кругу
                if(index==countBusStop){
                    index=0;
                }
                if(index==-1){
                    index=countBusStop-1;
                }
                //endregion

                if(passengers==0)
                    idleCount++;
                else
                    idleCount=0;
                if(idleCount>=maxIdleCount)
                    move=false;
                move();
                step(index);
                index+=j;
            }
            log.debug("End");

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*Симмулирует дижение автобуса между ост.*/
    private void move() throws InterruptedException {
        Thread.sleep(speed);//simulation moving from bus stop i to bus stop i+1
    }

    private void step(int i) throws InterruptedException {
        log.debug("going to "+i);
        BusStops.busStops.get(i).notifyAllOnBS(log,this);
        log.debug("running");
    }

    public void waitDriverBus(){
        synchronized (this.sleepObj) {
            try {
                if(status==Status.SLEEP) this.sleepObj.wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void wakeUpDriverBus(){
        synchronized (this.sleepObj) {
            this.status=Status.STOP;
            this.sleepObj.notifyAll();//or .notify
        }
    }

    /*Логика пассажиров в автобусе*/
    public synchronized void waitInBus(Logger log, int indexEnd){
        try {
            log.debug(this.log.getName()+" | sat in Bus");
            boolean move=true;
            while (move) {
                this.wait();//ожидвет прибыстия на остановку
                if (this.getCurrentBusStop().getNumber() == indexEnd) {//сверяет номер ост.
                    /*Если это нужная остановка -> уменьшет кол-во пассажиров
                     и завершает последний цикл*/
                    this.decreasePassengers();
                    log.debug(this.log.getName()+" | end point");
                    move=false;
                }
                this.checkCount++;
                log.debug(this.log.getName()+" | check walker");
                if(checkCount == oldCountPassengers){//если это последний опрошенный пассажир в авто.
                    this.wakeUpDriverBus();//notify водителя авто. на запус пассажиров с ост.
                    this.checkCount =0;
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public int getVector() {
        return vector;
    }

    public synchronized void wakeUpWalkersInBus(){
        this.oldCountPassengers = passengers;
        this.status=Status.SLEEP;
        this.notifyAll();
    }

    public void statusSleep(){
        this.status=Status.SLEEP;
    }

    public void statusRun(){
        this.status=Status.RUN;
    }

    public int getPassengers(){
        return passengers;
    }

    public boolean isSeatsFree(){
        return (maxSeats - passengers)!=0 ;
    }

    public void increasePassengers(){
        this.passengers++;
    }

    public void decreasePassengers(){
        this.passengers--;
    }

    public BusStop getCurrentBusStop(){
        return this.currentBusStop;
    }

    public void setCurrentBusStop(BusStop currentBusStop) {
        this.currentBusStop = currentBusStop;
    }
}
