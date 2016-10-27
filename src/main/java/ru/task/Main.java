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
//            new WalkerThread(1,4,2).start();
//            new WalkerThread(2,4,2).start();
//            new WalkerThread(3,4,2).start();
//            new WalkerThread(4,4,2).start();

//            Thread.sleep(20000);
            Thread.sleep(2000);
            new BusThread(1,0,1).start();
            new BusThread(2,8,-1).start();
            new BusThread(3,2,1).start();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
