import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class StreamExample {
    public static void main(String[] args) {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10, 11, 12);

        System.out.println("Original list: " + numbers);

        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());

        System.out.println("Even numbers : " + evens);

        // Sum of even numbers using reduce
        int sum = evens.stream().reduce(0, Integer::sum);
        System.out.println("Sum of evens : " + sum);

        // Squares of even numbers
        List<Integer> squares = evens.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());
        System.out.println("Even squares : " + squares);
    }
}
