package il.co.ilrd.designpatterns.observer;

import java.util.function.Consumer;

public class Callback<T> {

    private Dispatcher<T> dispatcher;
    private T data;
    private Consumer<T> updateMethod;
    private Runnable stopMethod;
    public Callback(Consumer<T> updateMethod, Runnable stopMethod){
        this.updateMethod = updateMethod;
        this.stopMethod = stopMethod;
    }
    public void update(T data){
        this.data = data;
        updateMethod.accept(data);
    }
    public void unRegister(){
        dispatcher.unregister(this);
    }
    public void SetDispatcher(Dispatcher<T> dispatcher){
        this.dispatcher = dispatcher;
    }
    public void stopUpdate() {
        new Thread(stopMethod).start();
    }
    public T getData(){
        return data;
    }
}
