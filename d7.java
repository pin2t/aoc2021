import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d7 {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Pattern num = Pattern.compile("\\d+");
        Matcher matcher = num.matcher(reader.readLine());
        List<Integer> pos = new ArrayList<>();
        while (matcher.find()) {
            pos.add(Integer.valueOf(matcher.group()));
        }
        List<Integer> fuel = new ArrayList<>(), fuel2 = new ArrayList<>();
        for (int p = 0; p <= pos.stream().mapToInt(Integer::intValue).max().getAsInt(); p++) {
            int finalp = p;
            fuel.add(pos.stream().mapToInt(pp -> Math.abs(pp - finalp)).sum());
            fuel2.add(pos.stream().mapToInt(pp -> ((Math.abs(pp - finalp) + 1) * (pp + finalp - 2 * Math.min(pp, finalp))) / 2).sum());
        }
        System.out.println(fuel.stream().mapToInt(Integer::intValue).min().getAsInt());
        System.out.println(fuel2.stream().mapToInt(Integer::intValue).min().getAsInt());
    }
}
