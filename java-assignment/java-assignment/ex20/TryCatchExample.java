import java.util.Scanner;

public class TryCatchExample {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the dividend: ");
        int a = scanner.nextInt();
        System.out.print("Enter the divisor: ");
        int b = scanner.nextInt();

        try {
            int result = a / b;
            System.out.println(a + " / " + b + " = " + result);
        } catch (ArithmeticException e) {
            System.out.println("Error: Cannot divide by zero. " + e.getMessage());
        } finally {
            System.out.println("Division operation complete.");
            scanner.close();
        }
    }
}
