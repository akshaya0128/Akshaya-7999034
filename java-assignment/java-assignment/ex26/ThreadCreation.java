class MessagePrinter implements Runnable {
    private final String message;
    private final int count;

    MessagePrinter(String message, int count) {
        this.message = message;
        this.count = count;
    }

    @Override
    public void run() {
        for (int i = 1; i <= count; i++) {
            System.out.println(Thread.currentThread().getName() + ": " + message + " #" + i);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }
}

public class ThreadCreation {
    public static void main(String[] args) throws InterruptedException {
        Thread t1 = new Thread(new MessagePrinter("Hello from Thread 1", 5), "Thread-1");
        Thread t2 = new Thread(new MessagePrinter("Hello from Thread 2", 5), "Thread-2");

        t1.start();
        t2.start();

        t1.join();
        t2.join();

        System.out.println("Both threads have finished.");
    }
}
