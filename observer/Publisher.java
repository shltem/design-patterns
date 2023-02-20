package il.co.ilrd.designpatterns.observer;

public class Publisher<T> {
    Dispatcher<T> dispatcher = new Dispatcher<>();

    public void register(Callback<T> cAllBack){
        dispatcher.register(cAllBack);
    }

    public void publish(T data){
        dispatcher.updateAll(data);
    }

    public void close(){
        dispatcher.endService();
    }
}
