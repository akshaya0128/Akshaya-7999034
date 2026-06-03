import java.util.Scanner;

public class RecursiveFibonacci {

    static long fibonacci(int n) {
        if (n <= 1) return n;
        return fibonacci(n - 1) + fibonacci(n - 2);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter a positive integer n: ");
        int n = scanner.nextInt();

        if (n < 0) {
            System.out.println("Please enter a non-negative integer.");
        } else {
            System.out.println("Fibonacci(" + n + ") = " + fibonacci(n));
        }
        scanner.close();
    }
}
