package chenanze.com.prodmapper;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import chenanze.com.prodmapper.TabEntity.DatasBean;

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
//        List<Test3> test3List = TestSubBeanTest3.transforms(new Test3Entity("0", 0, 0, testBeanList));

        TabEntity tabEntity = new TabEntity(
                new ArrayList<DatasBean>() {{
                    add(new DatasBean(
                            new ArrayList<DatasBean.ProjTabBean>() {{
                                add(new DatasBean.ProjTabBean("iconFileName", "iconFileUrlLink", "actionMode", "actionParam", "id", false, "TestName", 0, "0", "remark"));
                                add(new DatasBean.ProjTabBean("iconFileName1", "iconFileUrlLink1", "actionMode1", "actionParam1", "id1", false, "TestName1", 1, "1", "remark1"));
                                add(new DatasBean.ProjTabBean("iconFileName2", "iconFileUrlLink2", "actionMode2", "actionParam2", "id2", false, "TestName2", 2, "2", "remark2"));
                                add(new DatasBean.ProjTabBean("iconFileName3", "iconFileUrlLink3", "actionMode3", "actionParam3", "id3", false, "TestName3", 3, "3", "remark3"));
                            }}));
                    add(new DatasBean(
                            new ArrayList<DatasBean.ProjTabBean>() {{
                                add(new DatasBean.ProjTabBean("iconFileName4", "iconFileUrlLink4", "actionMode4", "actionParam4", "id4", false, "TestName4", 4, "4", "remark4"));
                                add(new DatasBean.ProjTabBean("iconFileName5", "iconFileUrlLink5", "actionMode5", "actionParam5", "id5", false, "TestName5", 5, "5", "remark5"));
                                add(new DatasBean.ProjTabBean("iconFileName6", "iconFileUrlLink6", "actionMode6", "actionParam6", "id6", false, "TestName6", 6, "6", "remark6"));
                                add(new DatasBean.ProjTabBean("iconFileName7", "iconFileUrlLink7", "actionMode7", "actionParam7", "id7", false, "TestName7", 7, "7", "remark7"));
                            }}));
                    add(new DatasBean(
                            new ArrayList<DatasBean.ProjTabBean>() {{
                                add(new DatasBean.ProjTabBean("iconFileName8", "iconFileUrlLink8", "actionMode8", "actionParam8", "id8", false, "TestName8", 8, "8", "remark8"));
                                add(new DatasBean.ProjTabBean("iconFileName9", "iconFileUrlLink9", "actionMode9", "actionParam9", "id9", false, "TestName9", 9, "9", "remark9"));
                                add(new DatasBean.ProjTabBean("iconFileName10", "iconFileUrlLink10", "actionMode10", "actionParam10", "id10", false, "TestName10", 10, "10", "remark10"));
                                add(new DatasBean.ProjTabBean("iconFileName11", "iconFileUrlLink11", "actionMode11", "actionParam11", "id11", false, "TestName11", 11, "11", "remark11"));
                            }}));
                }});

        List<Tab> tabList = (List<Tab>) Prodmapper.transfroms(tabEntity, TabEntity.DatasBean.ProjTabBean.class);
//        List<Tab> tabList = ProjTabBean__Tab$$MapperBind.transform(tabEntity);

        Log.d(TAG, "tabList: " + tabList);

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
