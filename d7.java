import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d7 {
    public static void main(String[] args) throws IOException {
        var scanner = new Scanner(System.in);
        var pos = scanner.readInts();
        var maxpos = Arrays.stream(pos).max().getAsInt();
        var fuel = new int[maxpos + 1];
        var fuel2 = new int[maxpos + 1];
        for (int p = 0; p <= maxpos; p++) {
            var finalp = p;
            fuel[p] = Arrays.stream(pos).map(pp -> Math.abs(pp - finalp)).sum();
            fuel2[p] = Arrays.stream(pos).map(pp -> ((Math.abs(pp - finalp) + 1) * (pp + finalp - 2 * Math.min(pp, finalp))) / 2).sum();
        }
        System.out.println(Arrays.stream(fuel).min().getAsInt());
        System.out.println(Arrays.stream(fuel2).min().getAsInt());
    }
}
