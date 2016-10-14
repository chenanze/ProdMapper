package chenanze.com.prodmapper;

import com.chenanze.prodmapper.Construction;

/**
 * Created by duian on 2016/10/13.
 */

public class Test3 {

    String test3String;
    int test3Int;
    Integer test3Integer;

    @Construction
    public Test3(String test3String, int test3Int, Integer test3Integer) {
        this.test3String = test3String;
        this.test3Int = test3Int;
        this.test3Integer = test3Integer;
    }

    @Override
    public String toString() {
        return "Test3{" +
                "test3String='" + test3String + '\'' +
                ", test3Int=" + test3Int +
                ", test3Integer=" + test3Integer +
                '}';
    }
}
