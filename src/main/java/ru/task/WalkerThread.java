package ru.task;

import org.apache.log4j.Logger;

/**
 * Created by ILIA on 23.10.2016.
 */
public class WalkerThread extends Thread {
    private Logger log;
    private int indexStart=0, indexEnd=1;

    /***
     * @param indexStart - индес начальной остановки
     * @param indexEnd - индес конечной остановки
     */
    public WalkerThread(int id, int indexStart, int indexEnd) {
        this.indexStart = indexStart;
        this.indexEnd = indexEnd;
        String name="Walker"+id+"("+indexStart+"-"+indexEnd+")";
        this.log=Logger.getLogger(name);
        this.setName(name);
    }

    @Override
    public void run() {
        int vector;
        /*Определяет вектор направления.
        Используется для выбора нужного автобуса*/
        if(indexStart-indexEnd>0)
            vector=-1;
        else
            vector=1;
        /**
         * 1. получает нужную остановку из статик коллекции
         * 2. вызывает метод waitOnBS на остановке; метод возращает
         * поток автобуса (BusThread), когда место в авто. зарезервировано
         * и авто. подходит по напрвалению (vector).
         * 3. на авто. вызывается метод waitInBus, который является логикой
         * пассажира -> проверяет остановки на котрых тормозит авто. и выходит
         * из авто., если остановка = indexEnd.
         * 4. пассажир с радостью, что добрался до нужной остановки самоликвидируется
         * (пассажир-поток завершает работу).
         * */
        BusStops.busStops.get(indexStart).waitOnBS(log, vector).waitInBus(log,indexEnd);
    }
}
