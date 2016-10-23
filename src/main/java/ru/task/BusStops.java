package ru.task;

import java.util.ArrayList;

/**
 * Created by ILIA on 23.10.2016.
 */
public class BusStops {
    //public static Object[] busStop=new Object[]{new Object(), new Object()};
    public static ArrayList<BusStop> busStops=new ArrayList<BusStop>();
    static {
        //FIX: index and number (number isn't index)
        busStops.add(new BusStop(0));
        busStops.add(new BusStop(1));
        busStops.add(new BusStop(2));
        busStops.add(new BusStop(3));
        busStops.add(new BusStop(4));
        busStops.add(new BusStop(5));
        busStops.add(new BusStop(6));
        busStops.add(new BusStop(7));
        busStops.add(new BusStop(8));
        busStops.add(new BusStop(9));
        busStops.add(new BusStop(10));
    }

}
