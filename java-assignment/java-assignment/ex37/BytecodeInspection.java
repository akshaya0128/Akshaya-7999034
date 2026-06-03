/**
 * Exercise 37 — Inspect bytecode with javap
 *
 * Steps:
 *   1. Compile : javac BytecodeInspection.java
 *   2. Inspect : javap -c BytecodeInspection
 *      (-verbose for full constant pool; -p to include private members)
 *
 * The javap output will show opcodes like:
 *   bipush, istore, iload, iadd, imul, ireturn, getstatic, invokevirtual, etc.
 */
public class BytecodeInspection {

    public int add(int a, int b) {
        return a + b;
    }

    public int multiply(int a, int b) {
        return a * b;
    }

    public static int factorial(int n) {
        int result = 1;
        for (int i = 2; i <= n; i++) {
            result *= i;
        }
        return result;
    }

    public static void main(String[] args) {
        BytecodeInspection obj = new BytecodeInspection();
        System.out.println("5 + 3       = " + obj.add(5, 3));
        System.out.println("4 * 6       = " + obj.multiply(4, 6));
        System.out.println("factorial(5)= " + factorial(5));

        System.out.println("\nNow run: javap -c BytecodeInspection");
        System.out.println("to see the JVM bytecode for each method.");
    }
}
