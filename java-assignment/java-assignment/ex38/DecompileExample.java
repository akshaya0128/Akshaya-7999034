/**
 * Exercise 38 — Decompile a class file
 *
 * This class is intentionally simple so the decompiled output is easy to compare
 * with the original source.
 *
 * Steps:
 *   1. Compile  : javac DecompileExample.java
 *   2. Decompile: Option A — CFR (command-line, no GUI required)
 *                   java -jar cfr-<version>.jar DecompileExample.class
 *                 Option B — JD-GUI (graphical)
 *                   Open DecompileExample.class in JD-GUI
 *                 Option C — Procyon
 *                   java -jar procyon-decompiler-*.jar DecompileExample.class
 *
 * What to look for in the decompiled output:
 *   - Field declarations and their types
 *   - Constructor initialisation order
 *   - How the compiler implements the for-each loop (iterator vs array index)
 *   - String concatenation: modern javac may use invokedynamic / StringConcatFactory
 */
public class DecompileExample {

    private final String name;
    private final int[]  scores;

    public DecompileExample(String name, int[] scores) {
        this.name   = name;
        this.scores = scores;
    }

    public double average() {
        int sum = 0;
        for (int score : scores) {
            sum += score;
        }
        return scores.length == 0 ? 0 : (double) sum / scores.length;
    }

    @Override
    public String toString() {
        return name + " -> avg=" + String.format("%.2f", average());
    }

    public static void main(String[] args) {
        int[] scores = {85, 92, 78, 95, 88};
        DecompileExample student = new DecompileExample("Alice", scores);
        System.out.println(student);

        System.out.println("\nCompile this file, then open DecompileExample.class");
        System.out.println("in CFR, JD-GUI, or Procyon to view the decompiled source.");
    }
}
