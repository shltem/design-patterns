package il.co.ilrd.designpatterns.observer;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObserverTest {
    @Test
    void test(){
        Observer<Integer> observer = new Observer<>((num)-> System.out.println("thank you for updating from  " + this + "num " + num ), () -> System.out.println("cleaning in progress"));
        Publisher<Integer> publisher = new Publisher<>();
        observer.register(publisher);
        publisher.publish(5);
        publisher.close();
        observer.unRegister();
        publisher.publish(5);
    }
}