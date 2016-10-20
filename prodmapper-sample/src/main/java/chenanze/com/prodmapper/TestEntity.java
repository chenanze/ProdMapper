package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/10/11.
 */
//@BindType(Test.class)
public class TestEntity {
    public String test1;
    public int test2;

    public TestEntity(String test1, int test2) {
        this.test1 = test1;
        this.test2 = test2;
    }

    public String getTest1() {
        return test1;
    }

    public void setTest1(String test1) {
        this.test1 = test1;
    }

    public int getTest2() {
        return test2;
    }

    public void setTest2(int test2) {
        this.test2 = test2;
    }
}
