package server;

public class ABC {
    static Object man = new Object();
    static volatile char currLit = 'C';

    public static void main (String[] args) {
        new Thread(() ->{
            try {
                for (int i = 0; i < 5; i++) {
                    synchronized (man) {
                        while (currLit !='A') {
                            man.wait();
                }
                        System.out.println("A");
                        currLit = 'B';
                        man.notifyAll();
                }
            }
        } catch (InterruptedException e) {
                e.printStackTrace();
            }
    }).start();

        new Thread(() ->{
            try {
                for (int i = 0; i < 5; i++) {
                    synchronized (man) {
                        while (currLit !='C') {
                            man.wait();
                        }
                        System.out.println("C");
                        currLit = 'A';
                        man.notifyAll();
                    }
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();

         }
}

