import java.io.*;
import java.util.Arrays;
import static java.lang.System.out;

public class d6 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int[] pos = scanner.readInts();
        long[] counts = new long[9];
        for (int p : pos) counts[p]++;
        out.println(totalAfter(counts, 80));
        out.println(totalAfter(counts, 256));
    }
    
    static long totalAfter(long[] initial, int days) {
        long[] prev = new long[9];
        for (int d = 1; d <= days; d++) {
            System.arraycopy(initial, 0, prev, 0, 9);
            initial = new long[9];
            initial[6] = prev[0]; initial[8] = prev[0];
            for (int i = 1; i < 9; i++) initial[i - 1] += prev[i];
        }
        return Arrays.stream(initial).sum();
    }
}
