package chenanze.com.prodmapper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.prodmapper.TestSubBeanTest3;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private final String TAG = getClass().getSimpleName();
//    @BindView(R.id.test_1)
//    TextView textView;

    TestEntity testEntity;
    Test1Entity test1Entity;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        testRunningTime();

        Test3 test3;
//        test3 = Test3EntityTest3.transform(new Test3Entity("test3", 3, 2));
//        Log.d(TAG, "test3: " + test3);
//        test3 = (Test3) Prodmapper.transfrom(new Test3Entity("test5", 3, 2));
//        Log.d(TAG, "test3: " + test3);

//        Test4 test4 = (Test4) Prodmapper.transfrom(new Test4Entity(1, "1", 1, 1, false, 0.0f, 0.0d, '1'));

        List<Test3Entity.TestBean> testBeanList = new ArrayList<>();
        List<Test3Entity.TestBean.TestSubBean> testSubBeanList = new ArrayList<>();
        testSubBeanList.add(new Test3Entity.TestBean.TestSubBean("3", 3));
        testSubBeanList.add(new Test3Entity.TestBean.TestSubBean("4", 4));
        List<Test3Entity.TestBean.TestSubBean> testSubBeanList1 = new ArrayList<>();
        testSubBeanList1.add(new Test3Entity.TestBean.TestSubBean("5", 5));
        testSubBeanList1.add(new Test3Entity.TestBean.TestSubBean("6", 6));
        testBeanList.add(new Test3Entity.TestBean("1", 1, testSubBeanList));
        testBeanList.add(new Test3Entity.TestBean("2", 2, testSubBeanList1));
//        List<Test3> test3List = Test3EntityTest3Mapper.transform(new Test3Entity("0", 0, 0, testBeanList));
        List<Test3> test3List = TestSubBeanTest3.transforms(new Test3Entity("0", 0, 0, testBeanList));
//        List<Test3> test3List = (List<Test3>) Prodmapper.transfroms(new Test3Entity("0", 0, 0, testBeanList), Test3Entity.TestBean.TestSubBean.class);
        Log.d(TAG, "test3: " + test3List);

//        for (Test3 tes3 : test3List) {
//            Log.d(TAG, "test3: " + tes3.toString());
//        }
    }

    private void testRunningTime() {
        long t1;
        long t2;
        long count = 10000;
        t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
//            Test hello3 = TestEntity__Test$$MapperBind.transform(new TestEntity("hello1", 1));
        }
        t2 = System.currentTimeMillis();
        Log.d(TAG, "1 running time : " + (t2 - t1));

        t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Test hello1 = (Test) Prodmapper.transfrom(new TestEntity("hello1", 1));
        }
        t2 = System.currentTimeMillis();
        Log.d(TAG, "2 running time : " + (t2 - t1));

        t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Test hello1 = (Test) Prodmapper.transfromWithClazz(new TestEntity("hello1", 1), TestEntity.class, Test.class);
        }
        t2 = System.currentTimeMillis();
        Log.d(TAG, "3 running time : " + (t2 - t1));

        t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Test1 hello1 = (Test1) Prodmapper.transfromWithClazz(new Test1Entity("hello1", 1), Test1.class);
        }
        t2 = System.currentTimeMillis();
        Log.d(TAG, "4 running time : " + (t2 - t1));

        Test1 hello2 = (Test1) Prodmapper.transfrom(new Test1Entity("hello2", 2));
        Log.d(TAG, "hello2: " + hello2);
    }
}
