// auther: Shlomo Templeman
// Date 7/09/2022
// Approved: Aviya Asadu


package il.co.ilrd.designpatterns.waitablepq;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class WaitablePQ<E> {
    private final Queue<E> priorityQueue; // blank final
    private final Semaphore semaphoreEnqueue; // blank final
    private final Semaphore semaphoreDequeue = new Semaphore(0);

    public WaitablePQ() {
        this(Integer.MAX_VALUE);
    }

    public WaitablePQ(int capacity) {
        priorityQueue = new PriorityQueue<>();
        semaphoreEnqueue = new Semaphore(capacity);
    }

    public WaitablePQ(Comparator<? super E> comparator) {
        this(Integer.MAX_VALUE, comparator);
    }

    public WaitablePQ(int capacity, Comparator<? super E> comparator) {
        priorityQueue = new PriorityQueue<>(comparator);
        semaphoreEnqueue = new Semaphore(capacity);
    }

    public void enqueue(E element) {
        try {
            semaphoreEnqueue.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (priorityQueue) {
                priorityQueue.add(element);
            semaphoreDequeue.release();
        }
    }


    public E dequeue() {
        try {
            semaphoreDequeue.acquire();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        synchronized (priorityQueue) {
            E toReturn = priorityQueue.poll();
            semaphoreEnqueue.release();
            return toReturn;
        }
    }
    
    public E dequeue(long timeout, TimeUnit unit) throws TimeoutException {
        boolean isAcquire;
        try {
            isAcquire = semaphoreDequeue.tryAcquire(timeout, unit);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        E toReturn = null;
        if (isAcquire) {
            synchronized (priorityQueue) {
                toReturn = priorityQueue.poll();
                semaphoreEnqueue.release();
            }
        }
        return toReturn;
    }

    public E peek() {
        synchronized (priorityQueue) {
            return priorityQueue.peek();
        }
    }

    public int size() {
        synchronized (priorityQueue) {
            return priorityQueue.size();
        }
    }

    public boolean remove(E element) {
        boolean isRemove = false;
        synchronized (priorityQueue) {
            if(priorityQueue.remove(element)){
                try {
                    semaphoreDequeue.acquire();
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
               semaphoreEnqueue.release();
                isRemove = true;
            }
        }
        return isRemove;
    }

    public boolean isEmpty() {
        synchronized (priorityQueue) {
            return priorityQueue.isEmpty();
        }
    }
}
