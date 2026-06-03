import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

class MathUtils {
    public int square(int n) {
        return n * n;
    }

    public double circleArea(double radius) {
        return Math.PI * radius * radius;
    }

    private String secret() {
        return "This was a private method, called via Reflection!";
    }
}

public class ReflectionExample {
    public static void main(String[] args) throws Exception {
        // 1. Load the class dynamically
        Class<?> clazz = Class.forName("MathUtils");
        System.out.println("Loaded class: " + clazz.getName());

        // 2. List all declared methods (including private)
        System.out.println("\nAll declared methods:");
        for (Method method : clazz.getDeclaredMethods()) {
            System.out.print("  " + method.getReturnType().getSimpleName() + " " + method.getName() + "(");
            Parameter[] params = method.getParameters();
            for (int i = 0; i < params.length; i++) {
                System.out.print(params[i].getType().getSimpleName() + " " + params[i].getName());
                if (i < params.length - 1) System.out.print(", ");
            }
            System.out.println(")");
        }

        // 3. Create an instance and invoke methods dynamically
        Object instance = clazz.getDeclaredConstructor().newInstance();

        Method squareMethod = clazz.getMethod("square", int.class);
        Object squareResult = squareMethod.invoke(instance, 7);
        System.out.println("\nsquare(7) via Reflection = " + squareResult);

        Method areaMethod = clazz.getMethod("circleArea", double.class);
        Object areaResult = areaMethod.invoke(instance, 5.0);
        System.out.printf("circleArea(5.0) via Reflection = %.4f%n", areaResult);

        // 4. Invoke a private method using setAccessible(true)
        Method secretMethod = clazz.getDeclaredMethod("secret");
        secretMethod.setAccessible(true);
        System.out.println("\nPrivate method result: " + secretMethod.invoke(instance));
    }
}
