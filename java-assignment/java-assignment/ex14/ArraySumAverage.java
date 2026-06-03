import java.util.Scanner;

public class ArraySumAverage {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of elements: ");
        int count = scanner.nextInt();

        double[] numbers = new double[count];
        System.out.println("Enter " + count + " elements:");
        for (int i = 0; i < count; i++) {
            System.out.print("Element " + (i + 1) + ": ");
            numbers[i] = scanner.nextDouble();
        }

        double sum = 0;
        for (double num : numbers) {
            sum += num;
        }
        double average = sum / count;

        System.out.printf("Sum     = %.2f%n", sum);
        System.out.printf("Average = %.2f%n", average);
        scanner.close();
    }
}
