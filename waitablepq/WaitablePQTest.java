package il.co.ilrd.designpatterns.waitablepq;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.assertTrue;

class WaitablePQTest {

    @Test
    void test() throws TimeoutException, InterruptedException {
        WaitablePQ<Integer> waitableCondPq = new WaitablePQ<>(10);
        List<Thread> dequeueArray = new ArrayList<>();
        List<Thread> enqueueArray = new ArrayList<>();

        assertTrue(waitableCondPq.isEmpty());
        waitableCondPq.enqueue((1));
        System.out.println("remove " + waitableCondPq.remove(1));
        System.out.println("size " + waitableCondPq.size());


        for (int i = 1; i < 11; i++) {
            int finalI = i;
            enqueueArray.add(new Thread(() ->{
                System.out.println("enqueue " + Thread.currentThread());
            waitableCondPq.enqueue(new Integer(finalI));
            }));

            dequeueArray.add(new Thread(() -> {
                    try {
                        System.out.println("dequeue " + waitableCondPq.dequeue(5, TimeUnit.SECONDS ) +" "+ Thread.currentThread());
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
            }));
        }

        for (Thread runner : dequeueArray) {
            runner.start();
        }
        for (Thread runner : enqueueArray) {
            runner.start();
        }

        new Thread(()-> {
            try {
                System.out.println(waitableCondPq.dequeue(3, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }).start();
        new Thread(()-> {
            try {
                System.out.println(waitableCondPq.dequeue(3, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                throw new RuntimeException(e);
            }
        }).start();
            try {
            Thread.sleep(1000);
            System.out.println("waiting for enqueue.. .. .. .. .. ");
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new Thread(()->{
            System.out.println("enqued");
             waitableCondPq.enqueue(10);
        }).start();
        Thread.sleep(1000);
        System.out.println("waiting for enqueue to long.. .. .. .. .. ");
            try {
                System.out.println(waitableCondPq.dequeue(2, TimeUnit.SECONDS));
            } catch (TimeoutException e) {
                System.out.println("TimeOutException!");
            }
    }
}