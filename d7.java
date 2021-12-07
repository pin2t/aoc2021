import java.io.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d7 {
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        int[] pos = scanner.readInts();
        List<Integer> fuel = new ArrayList<>(), fuel2 = new ArrayList<>();
        for (int p = 0; p <= Arrays.stream(pos).max().getAsInt(); p++) {
            int finalp = p;
            fuel.add(Arrays.stream(pos).map(pp -> Math.abs(pp - finalp)).sum());
            fuel2.add(Arrays.stream(pos).map(pp -> ((Math.abs(pp - finalp) + 1) * (pp + finalp - 2 * Math.min(pp, finalp))) / 2).sum());
        }
        System.out.println(fuel.stream().mapToInt(Integer::intValue).min().getAsInt());
        System.out.println(fuel2.stream().mapToInt(Integer::intValue).min().getAsInt());
    }
}
