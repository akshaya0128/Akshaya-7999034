public class OperatorPrecedence {
    public static void main(String[] args) {
        // * has higher precedence than +, so 5*2 is evaluated first
        int result1 = 10 + 5 * 2;
        System.out.println("10 + 5 * 2 = " + result1 + "  (* before +)");

        // Parentheses override precedence
        int result2 = (10 + 5) * 2;
        System.out.println("(10 + 5) * 2 = " + result2 + "  (parentheses first)");

        // Mixed: /, *, % share same precedence and are left-associative
        int result3 = 20 / 4 * 2;
        System.out.println("20 / 4 * 2 = " + result3 + "  (left to right: 5 * 2)");

        // Compound expression
        int result4 = 2 + 3 * 4 - 8 / 2;
        System.out.println("2 + 3 * 4 - 8 / 2 = " + result4 + "  (2 + 12 - 4)");

        // Modulus before addition
        int result5 = 10 % 3 + 2;
        System.out.println("10 % 3 + 2 = " + result5 + "  (1 + 2)");
    }
}
