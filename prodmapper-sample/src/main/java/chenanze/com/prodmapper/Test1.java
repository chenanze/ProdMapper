package chenanze.com.prodmapper;

import com.chenanze.prodmapper.Construction;

/**
 * Created by duian on 2016/10/11.
 */

public class Test1 {

    String mTest3;
    int mTest4;


    @Construction
    public Test1(String test3, int test4) {
        mTest3 = test3;
        mTest4 = test4;
    }

    @Override
    public String toString() {
        return "Test1{" +
                "mTest3='" + mTest3 + '\'' +
                ", mTest4=" + mTest4 +
                '}';
    }
}
