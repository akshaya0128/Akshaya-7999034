import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Exercise 40 — Virtual Threads (Java 21)
 * Requires Java 21 (stable) or Java 20 with preview enabled.
 * Compile: javac --enable-preview --source 20 VirtualThreads.java
 * Run    : java  --enable-preview VirtualThreads
 */
public class VirtualThreads {

    private static final int THREAD_COUNT = 100_000;

    public static void main(String[] args) throws InterruptedException {
        System.out.println("Launching " + THREAD_COUNT + " virtual threads...\n");

        AtomicInteger completed = new AtomicInteger(0);

        long start = System.currentTimeMillis();

        List<Thread> threads = new ArrayList<>(THREAD_COUNT);
        for (int i = 0; i < THREAD_COUNT; i++) {
            Thread vt = Thread.ofVirtual().start(() -> {
                // Simulate a small unit of work
                completed.incrementAndGet();
            });
            threads.add(vt);
        }

        // Wait for all virtual threads to finish
        for (Thread t : threads) {
            t.join();
        }

        long elapsed = System.currentTimeMillis() - start;
        System.out.println("All " + completed.get() + " virtual threads completed.");
        System.out.println("Time taken: " + elapsed + " ms");

        // Compare with platform threads
        System.out.println("\nNow launching " + 1000 + " platform threads for comparison...");
        AtomicInteger platformCompleted = new AtomicInteger(0);
        long platformStart = System.currentTimeMillis();

        List<Thread> platformThreads = new ArrayList<>(1000);
        for (int i = 0; i < 1000; i++) {
            Thread pt = new Thread(() -> platformCompleted.incrementAndGet());
            pt.start();
            platformThreads.add(pt);
        }
        for (Thread t : platformThreads) {
            t.join();
        }
        long platformElapsed = System.currentTimeMillis() - platformStart;
        System.out.println("1000 platform threads completed in: " + platformElapsed + " ms");
        System.out.println("\nVirtual threads handle 100x more concurrency with minimal overhead.");
    }
}
