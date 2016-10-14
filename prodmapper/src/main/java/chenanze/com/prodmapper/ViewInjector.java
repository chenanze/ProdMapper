package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/10/10.
 */

public interface ViewInjector<T>{
    void inject(T t , Object object);
}