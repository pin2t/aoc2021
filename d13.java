import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class d13 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var dots = new HashSet<Dot>();
        var foldings = new ArrayList<String>();
        reader.lines().forEach(line -> {
            if (line.startsWith("fold along")) {
                foldings.add(line);
            } else if (!line.isBlank()) {
                dots.add(new Dot(line));
            }
        });
        var foldPtn = Pattern.compile("fold along (x|y)\\=(\\d+)");
        final var matcher = foldPtn.matcher(foldings.get(0));
        if (!matcher.matches()) throw new RuntimeException("invalid input " + foldings.get(0));
        out.println(dots.stream().map(d -> d.fold(matcher.group(1), Integer.parseInt(matcher.group(2)))).collect(Collectors.toSet()).size());
        Set<Dot> result = new HashSet<>(dots);
        for (var f : foldings) {
            final var matcher2 = foldPtn.matcher(f);
            if (!matcher2.matches()) throw new RuntimeException("invalid input " + f);
            result = result.stream().map(d -> d.fold(matcher2.group(1), Integer.parseInt(matcher2.group(2)))).collect(Collectors.toSet());
        }
        for (int y = 0; y < result.stream().mapToInt(d -> d.y).max().getAsInt() + 1; y++) {
            for (int x = 0; x < result.stream().mapToInt(d -> d.x).max().getAsInt() + 1; x++) {
                if (result.contains(new Dot(x, y))) out.print("#"); else out.print(".");
            }
            out.println();
        }
    }

    static class Dot {
        final int x, y;

        Dot(String s) {
            var pair = s.split(",");
            this.x = Integer.parseInt(pair[0]);
            this.y = Integer.parseInt(pair[1]);
        }
        Dot(int x, int y) {
            this.x = x;
            this.y = y;
        }

        Dot fold(String axis, int n) {
            if (axis.equals("x")) {
                if (x > n) return new Dot(n - (x - n), y);
                else return this;
            } else {
                if (y > n) return new Dot(x, n - (y - n));
                else return this;
            }
        }
        public boolean equals(Object other) { return ((Dot)other).x == x && ((Dot)other).y == y; }
        public int hashCode() { return this.x + this.y; }
    }
}
