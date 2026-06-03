import java.util.HashMap;
import java.util.Scanner;

public class HashMapExample {
    public static void main(String[] args) {
        HashMap<Integer, String> studentMap = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        System.out.println("Add student records (enter 0 as ID to stop):");
        while (true) {
            System.out.print("Student ID: ");
            int id = scanner.nextInt();
            if (id == 0) break;
            scanner.nextLine();
            System.out.print("Student Name: ");
            String name = scanner.nextLine();
            studentMap.put(id, name);
            System.out.println("Saved: [" + id + "] -> " + name);
        }

        System.out.print("\nEnter ID to look up: ");
        int lookupId = scanner.nextInt();
        String found = studentMap.get(lookupId);
        if (found != null) {
            System.out.println("Student found: " + found);
        } else {
            System.out.println("No student found with ID " + lookupId);
        }
        scanner.close();
    }
}
