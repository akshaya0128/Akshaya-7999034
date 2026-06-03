import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ExecutorServiceExample {

    // A Callable task that computes the square of a number
    static class SquareTask implements Callable<Integer> {
        private final int number;

        SquareTask(int number) {
            this.number = number;
        }

        @Override
        public Integer call() throws Exception {
            Thread.sleep(100); // simulate some work
            System.out.printf("  [%s] Computing square of %d%n",
                    Thread.currentThread().getName(), number);
            return number * number;
        }
    }

    // A Callable task that computes factorial
    static class FactorialTask implements Callable<Long> {
        private final int number;

        FactorialTask(int number) {
            this.number = number;
        }

        @Override
        public Long call() throws Exception {
            long result = 1;
            for (int i = 2; i <= number; i++) result *= i;
            System.out.printf("  [%s] factorial(%d) = %d%n",
                    Thread.currentThread().getName(), number, result);
            return result;
        }
    }

    public static void main(String[] args) {
        // Fixed thread pool with 4 worker threads
        ExecutorService executor = Executors.newFixedThreadPool(4);

        // Submit SquareTask callables
        System.out.println("=== Square Tasks ===");
        List<Future<Integer>> squareFutures = new ArrayList<>();
        for (int i = 1; i <= 8; i++) {
            squareFutures.add(executor.submit(new SquareTask(i)));
        }

        System.out.println("Collecting square results:");
        for (int i = 0; i < squareFutures.size(); i++) {
            try {
                int result = squareFutures.get(i).get(); // blocks until done
                System.out.println("  square(" + (i + 1) + ") = " + result);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("  Task failed: " + e.getMessage());
            }
        }

        // Submit FactorialTask callables
        System.out.println("\n=== Factorial Tasks ===");
        List<Future<Long>> factFutures = new ArrayList<>();
        int[] inputs = {5, 6, 7, 8, 10, 12};
        for (int n : inputs) {
            factFutures.add(executor.submit(new FactorialTask(n)));
        }

        System.out.println("Collecting factorial results:");
        for (int i = 0; i < inputs.length; i++) {
            try {
                long result = factFutures.get(i).get();
                System.out.println("  factorial(" + inputs[i] + ") = " + result);
            } catch (InterruptedException | ExecutionException e) {
                System.out.println("  Task failed: " + e.getMessage());
            }
        }

        executor.shutdown();
        System.out.println("\nExecutor shut down. All tasks complete.");
    }
}
