package ru.task;

import java.util.ArrayList;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusStops {
    public static ArrayList<BusStop> busStops=new ArrayList<BusStop>();
    private static int totalCountWalkersAlive;
    public static void loadBusStops(int count){
        for (int i=0;i<count;i++)
            busStops.add(new BusStop(i));
    }

    public static ArrayList<BusStop> getBusStops() {
        return busStops;
    }

    public static void setBusStops(ArrayList<BusStop> busStops) {
        BusStops.busStops = busStops;
    }
}
