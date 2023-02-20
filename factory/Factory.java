
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class Factory<K ,T, D> {

    public Map<K, Function<D,T>> hashMap = new HashMap<>();
    
    public void add(K key, Function<D,T> function){
        hashMap.put(key, function);
    }

    public T create(K key, D data){
        return hashMap.get(key).apply(data);
    }

}