package test;

/**
 * Created by Lenovo-PC on 25.10.2016.
 */
public class BusStop {
    public void waitBus(){
        synchronized (this){
            try {

                System.out.println("before wait");
                this.wait();
                System.out.println("after wait");


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void busThis(){
        try {
            Thread.sleep(10000);
            System.out.println("enter monitor");
            this.notifyAll();
            System.out.println("free monitor");
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
