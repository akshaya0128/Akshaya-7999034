import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

record Person(String name, int age) {}

public class RecordExample {
    public static void main(String[] args) {
        Person p1 = new Person("Alice", 30);
        Person p2 = new Person("Bob", 17);
        Person p3 = new Person("Charlie", 25);
        Person p4 = new Person("Diana", 15);
        Person p5 = new Person("Eve", 22);

        System.out.println("All people:");
        List<Person> people = Arrays.asList(p1, p2, p3, p4, p5);
        people.forEach(System.out::println);

        System.out.println("\nAdults (age >= 18):");
        List<Person> adults = people.stream()
                .filter(p -> p.age() >= 18)
                .collect(Collectors.toList());
        adults.forEach(System.out::println);

        System.out.println("\nSorted by age:");
        people.stream()
                .sorted((a, b) -> Integer.compare(a.age(), b.age()))
                .forEach(System.out::println);
    }
}
