/**
 * Requires Java 21 (stable) or Java 20 with preview enabled.
 * Compile: javac --enable-preview --source 20 PatternMatchingSwitch.java
 * Run    : java  --enable-preview -cp . PatternMatchingSwitch
 */
public class PatternMatchingSwitch {

    static String describe(Object obj) {
        return switch (obj) {
            case Integer i -> "Integer with value: " + i;
            case Double d  -> "Double with value: " + d;
            case String s  -> "String with value: \"" + s + "\" (length=" + s.length() + ")";
            case Boolean b -> "Boolean with value: " + b;
            case int[] arr -> "int array of length " + arr.length;
            case null      -> "null value";
            default        -> "Unknown type: " + obj.getClass().getSimpleName();
        };
    }

    public static void main(String[] args) {
        Object[] samples = { 42, 3.14, "Hello", true, new int[]{1, 2, 3}, null, 'X' };

        for (Object sample : samples) {
            System.out.println(describe(sample));
        }
    }
}
