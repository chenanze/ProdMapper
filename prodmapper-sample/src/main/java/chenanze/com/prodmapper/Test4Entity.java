package chenanze.com.prodmapper;

import com.chenanze.prodmapper.BindType;

/**
 * Created by duian on 2016/10/17.
 */
@BindType(Test4.class)
public class Test4Entity {
    public int test4Int;
    public String test4String;
    public Integer test4Integer;
    public long test4Long;
    public boolean test4Boolean;
    public float test4Float;
    public double test4Double;
    public char test4Char;
    public char test4Char2;

    public Test4Entity(int test4Int, String test4String, Integer test4Integer, long test4Long, boolean test4Boolean, float test4Float, double test4Double, char test4Char) {
        this.test4Int = test4Int;
        this.test4String = test4String;
        this.test4Integer = test4Integer;
        this.test4Long = test4Long;
        this.test4Boolean = test4Boolean;
        this.test4Float = test4Float;
        this.test4Double = test4Double;
        this.test4Char = test4Char;
    }
}
