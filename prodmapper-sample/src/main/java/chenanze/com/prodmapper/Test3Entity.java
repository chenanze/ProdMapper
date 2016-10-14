package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/10/13.
 */
//@BindType(value = Test3.class)
public class Test3Entity {
    String test3String;
    int test3Int;
    Integer test3Integer;

    public Test3Entity(String test3String, int test3Int, Integer test3Integer) {
        this.test3String = test3String;
        this.test3Int = test3Int;
        this.test3Integer = test3Integer;
    }

    public String getTest3String() {
        return test3String;
    }

    public int getTest3Int() {
        return test3Int;
    }

    public Integer getTest3Integer() {
        return test3Integer;
    }
}
