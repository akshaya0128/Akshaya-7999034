import java.util.ArrayList;
import java.util.Scanner;

public class ArrayListExample {
    public static void main(String[] args) {
        ArrayList<String> students = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter student names (type 'done' to stop):");
        while (true) {
            System.out.print("Name: ");
            String name = scanner.nextLine().trim();
            if (name.equalsIgnoreCase("done")) break;
            if (!name.isEmpty()) {
                students.add(name);
                System.out.println("Added: " + name);
            }
        }

        System.out.println("\nAll students (" + students.size() + "):");
        for (int i = 0; i < students.size(); i++) {
            System.out.println((i + 1) + ". " + students.get(i));
        }
        scanner.close();
    }
}
