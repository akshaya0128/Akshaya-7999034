import java.util.Random;
import java.util.Scanner;

public class NumberGuessingGame {
    public static void main(String[] args) {
        Random random = new Random();
        int target = random.nextInt(100) + 1;
        Scanner scanner = new Scanner(System.in);
        int attempts = 0;

        System.out.println("Guess the number between 1 and 100!");

        while (true) {
            System.out.print("Your guess: ");
            int guess = scanner.nextInt();
            attempts++;

            if (guess < target) {
                System.out.println("Too low! Try again.");
            } else if (guess > target) {
                System.out.println("Too high! Try again.");
            } else {
                System.out.println("Correct! You guessed it in " + attempts + " attempt(s).");
                break;
            }
        }
        scanner.close();
    }
}
