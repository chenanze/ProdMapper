package chenanze.com.prodmapper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import com.prodmapper.TestEntity__Test$$MapperBind;

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
        testRunningTime();

//        Test3 test3 = new Test3Test3Entity().transform(new Test3Entity("test3", 1, 1));
        Test3 test3 = (Test3) Prodmapper.transfrom(new Test3Entity("test5", 3, 2));
        Log.d(TAG, "test3: " + test3);
    }

    private void testRunningTime() {
        long t1;
        long t2;
        long count = 10000;
        t1 = System.currentTimeMillis();
        for (int i = 0; i < count; i++) {
            Test hello3 = new TestEntity__Test$$MapperBind().transform(new TestEntity("hello1", 1));
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
