package ru.task;

import java.util.Random;
import java.util.ResourceBundle;

/**
 * Created by ILIA on 23.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        try {
            init();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void init() throws InterruptedException {
        ResourceBundle config=ResourceBundle.getBundle("config");
        int walkers=Integer.parseInt(config.getString("walkers"));
        int busStops=Integer.parseInt(config.getString("busStops"));
        busStops*=2;
        int buses=Integer.parseInt(config.getString("buses"));
        Random rnd=new Random();

        /*Инициализация остановок*/
        BusStops.loadBusStops(busStops);

        /*Инициализация пассажиров*/
        for (int i=0;i<walkers;i++){
            new WalkerThread(i, rnd.nextInt(busStops),rnd.nextInt(busStops)).start();
        }

        /*Ждем, чтобы все пассажиры "зашли" на остановки.
        *  Можно и не ждать, тогда пассажиры будут доходить
        *  до остановок во ремя работы автобусов
        * */
        Thread.sleep(1000);

        /*Инициализация автобусов*/
        for (int i = 1; i <= buses; i++) {
            Thread.sleep(Integer.parseInt(config.getString("bus." + i + ".interval")));
            int maxSeats = Integer.parseInt(config.getString("bus." + i + ".size"));
            int vector = Integer.parseInt(config.getString("bus." + i + ".vector"));
            int startIndex = Integer.parseInt(config.getString("bus." + i + ".startIndex"));
            int speed = Integer.parseInt(config.getString("bus." + i + ".speed"));
            new BusThread(i, maxSeats, startIndex, vector, speed).start();
        }

    }
}
