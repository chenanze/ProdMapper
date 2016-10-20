package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/10/11.
 */

public class Test {

    String mTest1;
    int mTest2;

//    @Construction
    public Test(String test1,int test2) {
        mTest1 = test1;
        mTest2 = test2;
    }

    @Override
    public String toString() {
        return "Test{" +
                "mTest1='" + mTest1 + '\'' +
                ", mTest2=" + mTest2 +
                '}';
    }
}
