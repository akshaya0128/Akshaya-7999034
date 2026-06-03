public class TypeCasting {
    public static void main(String[] args) {
        // Narrowing: double -> int (decimal part is truncated)
        double doubleValue = 9.78;
        int intFromDouble = (int) doubleValue;
        System.out.println("double value   : " + doubleValue);
        System.out.println("cast to int    : " + intFromDouble);

        System.out.println();

        // Widening: int -> double (no data loss)
        int intValue = 42;
        double doubleFromInt = (double) intValue;
        System.out.println("int value      : " + intValue);
        System.out.println("cast to double : " + doubleFromInt);
    }
}
