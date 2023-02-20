package il.co.ilrd.designpatterns.observer;

import java.util.function.Consumer;

public class Observer<T> {
    private final Callback<T> callback;

    public Observer(Consumer<T> updateMethod, Runnable stopMethod) {
       callback = new Callback<>(updateMethod, stopMethod);
    }

    public void register(Publisher<T> publisher){
        publisher.register(callback);
    }

    public void unRegister(){
        callback.unRegister();
    }

    public T getData(){
        return callback.getData();
    }
}
