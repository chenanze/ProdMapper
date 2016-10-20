package chenanze.com.prodmapper;

import com.chenanze.prodmapper.Construction;

/**
 * Created by duian on 2016/10/13.
 */
public class Test3 {

    public String testSubBeanString;
    public int testSubBeanInt;

    @Construction
    public Test3(String testSubBeanString, int testSubBeanInt) {
        this.testSubBeanString = testSubBeanString;
        this.testSubBeanInt = testSubBeanInt;
    }

    @Override
    public String toString() {
        return "Test3{" +
                "testSubBeanString='" + testSubBeanString + '\'' +
                ", testSubBeanInt=" + testSubBeanInt +
                '}';
    }
}
