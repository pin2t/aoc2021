import java.io.*;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d6 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Pattern num = Pattern.compile("\\d+");
        Matcher matcher = num.matcher(reader.readLine());
        long[] counts = new long[9];
        while (matcher.find()) counts[Integer.parseInt(matcher.group())]++;
        System.out.println(totalAfter(counts, 80));
        System.out.println(totalAfter(counts, 256));
    }
    
    static long totalAfter(long[] initial, int days) {
        long[] prev = new long[9];
        for (int d = 1; d <= days; d++) {
            System.arraycopy(initial, 0, prev, 0, 9);
            initial = new long[9];
            initial[6] += prev[0]; initial[8] += prev[0];
            for (int i = 1; i < 9; i++) initial[i - 1] += prev[i];
        }
        return Arrays.stream(initial).sum();
    }
}
