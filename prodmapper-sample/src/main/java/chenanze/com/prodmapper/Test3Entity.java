package chenanze.com.prodmapper;

import com.chenanze.prodmapper.BindType;

import java.util.List;

/**
 * Created by duian on 2016/10/13.
 */
public class Test3Entity {
    public String test3String;
    public int test3Int;
    Integer test3Integer;

//    private List<TestBean> testBeanList;
    public List<TestBean> testBeanList;

    public List<TestBean> getTestBeanList() {
        return testBeanList;
    }

    public static class TestBean {

        public String testBeanString;
        public int testBeanInt;

        public List<TestSubBean> testSubBeanList;

        public List<TestSubBean> getTestSubBeanList() {
            return testSubBeanList;
        }

        public TestBean(String testBeanString, int testBeanInt, List<TestSubBean> testSubBeanList) {
            this.testBeanString = testBeanString;
            this.testBeanInt = testBeanInt;
            this.testSubBeanList = testSubBeanList;
        }

        @BindType(value = Test3.class, proxyClassName = "$O$T")
        public static class TestSubBean {
            public String testSubBeanString;
            public int testSubBeanInt;

            public TestSubBean(String testSubBeanString, int testSubBeanInt) {
                this.testSubBeanString = testSubBeanString;
                this.testSubBeanInt = testSubBeanInt;
            }

            public String getTestSubBeanString() {
                return testSubBeanString;
            }

            public int getTestSubBeanInt() {
                return testSubBeanInt;
            }

            @Override
            public String toString() {
                return "TestSubBean{" +
                        "testSubBeanString='" + testSubBeanString + '\'' +
                        ", testSubBeanInt=" + testSubBeanInt +
                        '}';
            }
        }

        @Override
        public String toString() {
            return "TestBean{" +
                    "testBeanString='" + testBeanString + '\'' +
                    ", testBeanInt=" + testBeanInt +
                    ", testSubBeanList=" + testSubBeanList +
                    '}';
        }
    }

    public Test3Entity(String test3String, int test3Int, Integer test3Integer, List<TestBean> testBeanList) {
        this.test3String = test3String;
        this.test3Int = test3Int;
        this.test3Integer = test3Integer;
        this.testBeanList = testBeanList;
    }

    @Override
    public String toString() {
        return "Test3Entity{" +
                "test3String='" + test3String + '\'' +
                ", test3Int=" + test3Int +
                ", test3Integer=" + test3Integer +
                ", testBeanList=" + testBeanList +
                '}';
    }
}
