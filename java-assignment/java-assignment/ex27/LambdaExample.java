import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class LambdaExample {
    public static void main(String[] args) {
        List<String> names = Arrays.asList("Charlie", "Alice", "Eve", "Bob", "Diana");

        System.out.println("Original list: " + names);

        // Sort alphabetically using a lambda
        Collections.sort(names, (a, b) -> a.compareToIgnoreCase(b));
        System.out.println("Sorted (A-Z) : " + names);

        // Sort by length using a lambda
        names.sort((a, b) -> Integer.compare(a.length(), b.length()));
        System.out.println("Sorted by len: " + names);

        // Sort in reverse alphabetical order
        names.sort((a, b) -> b.compareToIgnoreCase(a));
        System.out.println("Sorted (Z-A) : " + names);
    }
}
