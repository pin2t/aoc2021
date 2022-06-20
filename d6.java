import java.io.*;
import java.util.Arrays;
import static java.lang.System.out;

public class d6 {
    public static void main(String[] args) throws IOException {
        var scanner = new Scanner(System.in);
        var pos = scanner.readInts();
        var counts = new long[9];
        for (var p : pos) {
            counts[p]++;
        }
        out.println(total(counts, 80));
        out.println(total(counts, 256));
    }
    
    static long total(long[] initial, int days) {
        var prev = new long[9];
        for (var d = 1; d <= days; d++) {
            System.arraycopy(initial, 0, prev, 0, 9);
            initial = new long[9];
            initial[6] = prev[0]; 
            initial[8] = prev[0];
            for (var i = 1; i < 9; i++) {
                initial[i - 1] += prev[i];
            }
        }
        return Arrays.stream(initial).sum();
    }
}
