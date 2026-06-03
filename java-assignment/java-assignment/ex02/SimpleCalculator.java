import java.util.Scanner;

public class SimpleCalculator {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.print("Enter first number: ");
        double a = scanner.nextDouble();

        System.out.print("Enter second number: ");
        double b = scanner.nextDouble();

        System.out.println("Choose an operation: +, -, *, /");
        System.out.print("Operation: ");
        char op = scanner.next().charAt(0);

        double result;
        switch (op) {
            case '+' -> result = a + b;
            case '-' -> result = a - b;
            case '*' -> result = a * b;
            case '/' -> {
                if (b == 0) {
                    System.out.println("Error: Division by zero.");
                    return;
                }
                result = a / b;
            }
            default -> {
                System.out.println("Invalid operator.");
                return;
            }
        }

        System.out.printf("Result: %.2f %c %.2f = %.2f%n", a, op, b, result);
        scanner.close();
    }
}
