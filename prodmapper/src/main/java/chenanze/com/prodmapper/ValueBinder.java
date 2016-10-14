package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/10/12.
 */

public interface ValueBinder<T,V> {
    T transform(V originObject);
}
