package test;

/**
 * Created by Lenovo-PC on 25.10.2016.
 */
public class BusStop {

    private Object obj=new Object();

    public void waitBus(){
        synchronized (this){
            try {

                System.out.println("before wait");
                this.wait();
                System.out.println("after wait");
                this.notify();


            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public synchronized void busThis(){
        try {
            Thread.sleep(2000);
            System.out.println("enter monitor");
            this.notifyAll();
            this.wait();
            System.out.println("free monitor");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
