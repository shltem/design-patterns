package il.co.ilrd.designpatterns.waitablepq;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static org.junit.jupiter.api.Assertions.*;

class WaitableCondPQTest2 {
        private WaitableCondPQ<Integer> pq;
        private List<Thread> producers;
        private List<Thread> consumers;
        private List<Thread> timedConsumers;

        @BeforeEach
        void init(){
            pq = new WaitableCondPQ<>(10);
            pq.enqueue(2);
            pq.enqueue(8);
            pq.enqueue(4);
        }

        @Test
        void enqueue() {
            assertEquals(3, pq.size());
            pq.enqueue(1);
            pq.enqueue(2);
            assertEquals(5, pq.size());
        }

        @Test
        void dequeue() {
            assertEquals(3, pq.size());
            assertEquals(2, pq.dequeue());
            assertEquals(4, pq.dequeue());
            assertEquals(1, pq.size());

        }

        @Test
        void timedDequeue() throws TimeoutException {
            pq.dequeue();
            pq.dequeue();
            assertNotNull(pq.dequeue(1, TimeUnit.SECONDS));
            assertNull(pq.dequeue(1, TimeUnit.SECONDS));
        }

        @Test
        void peek() {
            assertEquals(2, pq.peek());
            pq.dequeue();
            assertEquals(4, pq.peek());
        }

        @Test
        void size() {
            assertEquals(3, pq.size());
            pq.enqueue(2);
            pq.enqueue(3);
            pq.enqueue(4);
            assertEquals(6, pq.size());
            pq.dequeue();
            pq.dequeue();
            assertEquals(4, pq.size());
        }

        @Test
        void isEmpty() {
            assertFalse(pq.isEmpty());
            pq.dequeue();
            pq.dequeue();
            pq.dequeue();
            assertTrue(pq.isEmpty());
        }

        @Test
        void remove() {
            pq.remove(5);
            assertEquals(3, pq.size());
            pq.remove(8);
            assertEquals(2, pq.size());
        }

        @Test
        void mtTest1(){
            System.out.println("mtTest1 - more producers than consumers. expected output: NONE");
            initThreads();
            producers.get(0).start();
            producers.get(1).start();
            producers.get(2).start();
            producers.get(3).start();
            consumers.get(0).start();
            consumers.get(1).start();
            timedInterruptThreads();
        }

        @Test
        void mtTest2(){
            System.out.println("mtTest2 - more consumers than producers. expected output: NONE");
            initThreads();
            producers.get(0).start();
            producers.get(1).start();
            consumers.get(0).start();
            consumers.get(1).start();
            consumers.get(2).start();
            consumers.get(3).start();
            timedInterruptThreads();
        }

        @Test
        void mtTest3(){
            System.out.println("mtTest3 - more producers than timed consumers. expected output: NONE (possible output: TIMEOUT)");
            initThreads();
            producers.get(0).start();
            producers.get(1).start();
            producers.get(2).start();
            producers.get(3).start();
            timedConsumers.get(0).start();
            timedConsumers.get(1).start();
            timedInterruptThreads();
        }

        @Test
        void mtTest4(){
            System.out.println("mtTest4 - more timed consumers than producers. expected output: TIMEOUT");
            initThreads();
            producers.get(0).start();
            producers.get(1).start();
            timedConsumers.get(0).start();
            timedConsumers.get(1).start();
            timedConsumers.get(2).start();
            timedConsumers.get(3).start();
            timedInterruptThreads();
        }

        private void initThreads(){
            producers = new ArrayList<>();
            consumers = new ArrayList<>();
            timedConsumers = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                producers.add(new Thread(new Producer(pq)));
                consumers.add(new Thread(new Consumer(pq)));
                timedConsumers.add(new Thread(new TimeConsumer(pq)));
            }
        }

        private void timedInterruptThreads(){
            try {
                Thread.sleep(5000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            for (int i = 0; i < 4; i++) {
                producers.get(i).interrupt();
                consumers.get(i).interrupt();
                timedConsumers.get(i).interrupt();
            }
        }

        static class Producer implements Runnable {

            private final WaitableCondPQ<Integer> q;

            public Producer(WaitableCondPQ<Integer> q) {
                this.q = q;
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    q.enqueue((Math.abs(new Random().nextInt()) % 100) + 1);
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException ignored) {/*IGNORED*/}
                }
            }
        }

        static class Consumer implements Runnable {

            private final WaitableCondPQ<Integer> q;

            public Consumer(WaitableCondPQ<Integer> q) {
                this.q = q;
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    if (null == q.dequeue()) System.out.println("XXXXXXXXXXXXX");
                }
            }
        }

        static class TimeConsumer implements Runnable {

            private final WaitableCondPQ<Integer> q;

            public TimeConsumer(WaitableCondPQ<Integer> q) {
                this.q = q;
            }

            @Override
            public void run() {
                while (!Thread.currentThread().isInterrupted()) {
                    try {
                        if (null == q.dequeue(1, TimeUnit.SECONDS)) System.out.println("TIMEDOUT");
                    } catch (TimeoutException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }



    }