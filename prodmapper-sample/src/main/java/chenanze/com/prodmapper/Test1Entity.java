package chenanze.com.prodmapper;

import com.chenanze.prodmapper.BindType;

/**
 * Created by duian on 2016/10/12.
 */
@BindType(Test1.class)
public class Test1Entity {
    String test3;
    int test4;

    public Test1Entity(String test3, int test4) {
        this.test3 = test3;
        this.test4 = test4;
    }

    public String getTest3() {
        return test3;
    }

    public void setTest3(String test3) {
        this.test3 = test3;
    }

    public int getTest4() {
        return test4;
    }

    public void setTest4(int test4) {
        this.test4 = test4;
    }

    @Override
    public String toString() {
        return "Test1Entity{" +
                "test3='" + test3 + '\'' +
                ", test4=" + test4 +
                '}';
    }
}
