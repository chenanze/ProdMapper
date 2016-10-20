package chenanze.com.prodmapper;

/**
 * Created by duian on 2016/10/17.
 */

public class Test4 {
    public int test4Int;
    public String test4String;
    public Integer test4Integer;
    public long test4Long;
    public boolean test4Boolean;
    public float test4Float;
    public double test4Double;
    public char test4Char;

//    @Construction
    public Test4(int test4Int, String test4String, Integer test4Integer, long test4Long, boolean test4Boolean, float test4Float, double test4Double, char test4Char) {
        this.test4Int = test4Int;
        this.test4String = test4String;
        this.test4Integer = test4Integer;
        this.test4Long = test4Long;
        this.test4Boolean = test4Boolean;
        this.test4Float = test4Float;
        this.test4Double = test4Double;
        this.test4Char = test4Char;
    }

    @Override
    public String toString() {
        return "Test4{" +
                "test4Char=" + test4Char +
                ", test4Double=" + test4Double +
                ", test4Float=" + test4Float +
                ", test4Boolean=" + test4Boolean +
                ", test4Long=" + test4Long +
                ", test4Integer=" + test4Integer +
                ", test4String='" + test4String + '\'' +
                ", test4Int=" + test4Int +
                '}';
    }
}
