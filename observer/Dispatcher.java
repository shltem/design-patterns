package il.co.ilrd.designpatterns.observer;

import java.util.LinkedList;
import java.util.List;

public class Dispatcher<T> {

    private final List<Callback<T>> list = new LinkedList<>();

    public void register(Callback<T> callback){
        list.add(callback);
        callback.SetDispatcher(this);
    }

    public void unregister(Callback callback){
        callback.SetDispatcher(null);
        list.remove(callback);
    }

    public void updateAll(T data){
        for (Callback<T> runner : list) {
            runner.update(data);
        }
    }
    public void endService(){
        for (Callback runner : list) {
            runner.stopUpdate();
        }
    }
}
