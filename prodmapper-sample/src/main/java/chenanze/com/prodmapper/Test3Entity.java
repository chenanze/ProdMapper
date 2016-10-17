package chenanze.com.prodmapper;

import com.chenanze.prodmapper.BindType;

/**
 * Created by duian on 2016/10/13.
 */
@BindType(value = Test3.class,proxyClassName = "$O$T")
public class Test3Entity {
    public String test3String;
    public int test3Int;
    Integer test3Integer;

    public Test3Entity(String test3String, int test3Int, Integer test3Integer) {
        this.test3String = test3String;
        this.test3Int = test3Int;
        this.test3Integer = test3Integer;
    }

//    public String getTest3String() {
//        return test3String;
//    }

//    public int getTest3Int() {
//        return test3Int;
//    }

    public Integer getTest3Integer() {
        return test3Integer;
    }

    @Override
    public String toString() {
        return "Test3Entity{" +
                "test3String='" + test3String + '\'' +
                ", test3Int=" + test3Int +
                ", test3Integer=" + test3Integer +
                '}';
    }
}
