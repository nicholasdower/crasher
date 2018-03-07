package foo.crasher;

public class Crasher {
    static {
        System.loadLibrary("crasher");
    }

    public native String nativeCrash();

    public String crash() {
        throw new RuntimeException();
    }
}
