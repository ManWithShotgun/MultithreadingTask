package ru.task;

import java.util.Random;

/**
 * Created by ILIA on 23.10.2016.
 */
public class Main {
    public static void main(String[] args) {
        try {

            Random rnd=new Random();
            for (int i=0;i<10;i++){
                new WalkerThread(i, rnd.nextInt(10),rnd.nextInt(10)).start();
            }
//            new WalkerThread(7,9,2).start();

//            Thread.sleep(1000);
            new BusThread(0,1).start();
            Thread.sleep(100);
            new BusThread(5,-1).start();
            Thread.sleep(100);
            new BusThread(2,1).start();


        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
