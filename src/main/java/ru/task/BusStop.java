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
            while (wait) {//пока не зайдет в автобус
                log.debug("wait Bus...");
                this.wait();//ожидает автобус
                log.debug("ooo "+busThread.getName());
                if (busThread.isSeatsFree() && busThread.getVector()==vector) {//провека есть ли места и направление
                    busThread.increasePassengers();//увеличивает кол-во пассажиров в авто.
                    this.countWalkers--;//уменьше кол-во на остановке
                    log.debug("I sat");
                    wait=false;
                }
                /*case else bus full*/
                /*счет всех кто проверил авто.*/
                checkWalkers++;
                if(checkWalkers==oldCountWalkers){//последний кто проверяет
                    checkWalkers=0;
                    busThread.wakeUpDriverBus();//отпускает автобус с остановки
                }
                if (!wait){
                    return busThread;
                }
                else {
                    log.debug("no places or no vector");
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }

    /*Логика автобуса, который пришел на остановку*/
    public void notifyAllOnBS(Logger log,BusThread busThread){
        synchronized (this) {//занимает остановку
            busThread.setCurrentBusStop(this);
            if (busThread.getPassengers() != 0) {
                log.debug("Wait drop walkers...");
                busThread.wakeUpWalkersInBus();//notifyAll всех пассажиров в астобусе
                busThread.waitDriverBus();//wait till everyone leaves the bus
                log.debug("Wait drop walkers...end");
            }
            log.debug("Loading Walkers...countW: "+this.countWalkers+" BS:"+number);
            this.busThread = busThread;//указывает на остановке, какой автобус на ней
            /*сохраняет кол-во ожидающих на ост. для проверки количества чекнутых. countWalkers может менятся в это время oldCountWalkers - нет*/
            this.oldCountWalkers=countWalkers;
            busThread.statusSleep();//указывает статус автобуса (см. далее)
            this.notifyAll();
        }
        /**
         * BUG: В то время как автобус notifyAll на остановке и вышел из монитора остановки
         *      пробужденные на ост. могут вызвать busThread.wakeUpDriverBus (notify - пробуждение авто. на отправление)
         *      до момента, когда авто. вызовет busThread.waitDriverBus (wait - сон авто.).
         *      Следовательно авто. будет в wait'е и никто не сожет его разбудить.
         *
         * Эту проблему от части решает статус автобуса busThread.statusSleep,
         * но не гарантирует 100% защиту от deadlock.
         * */
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
