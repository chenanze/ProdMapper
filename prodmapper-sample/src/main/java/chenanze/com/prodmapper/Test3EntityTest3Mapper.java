package chenanze.com.prodmapper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by duian on 2016/10/17.
 */

public class Test3EntityTest3Mapper {
    public static List<Test3> transform(Test3Entity test3Entity) {
        List<Test3> test3s = new ArrayList<>();
        if (test3Entity != null) {
            for (Test3Entity.TestBean testBean : test3Entity.getTestBeanList()) {
                for (Test3Entity.TestBean.TestSubBean testSubBean : testBean.getTestSubBeanList()) {
                    test3s.add(new Test3(testSubBean.getTestSubBeanString(),testSubBean.getTestSubBeanInt()));
                }
            }
        }

        return test3s;
    }
}
