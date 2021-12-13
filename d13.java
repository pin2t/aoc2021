import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.regex.Pattern;

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
        var matcher = foldPtn.matcher(foldings.get(0));
        if (!matcher.matches()) throw new RuntimeException("not matches " + foldings.get(0));
        var n = Integer.parseInt(matcher.group(2));
        var visible = new HashSet<Dot>();
        for (var d : dots) {
            if (matcher.group(1).equals( "x")) { visible.add(d.foldX(n)); } else { visible.add(d.foldY(n)); }
        }
        out.println(visible.size());
        var result = new HashSet<Dot>(dots);
        for (var f : foldings) {
            matcher = foldPtn.matcher(f);
            if (!matcher.matches()) throw new RuntimeException("not matches " + f);
            n = Integer.parseInt(matcher.group(2));
            var step = new HashSet<Dot>();
            for (var d : result) {
                if (matcher.group(1).equals( "x")) { step.add(d.foldX(n)); } else { step.add(d.foldY(n)); }
            }
            result = step;
        }
        for (int y = 0; y < result.stream().mapToInt(d -> d.y).max().getAsInt() + 1; y++) {
            for (int x = 0; x < result.stream().mapToInt(d -> d.x).max().getAsInt() + 1; x++) {
                if (result.contains(new Dot(x, y))) out.print("#");
                else out.print(".");
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

        Dot foldX(int n) {
            if (x > n) return new Dot(n - (x - n), y); else return this;
        }
        Dot foldY(int n) {
            if (y > n) return new Dot(x, n - (y - n)); else return this;
        }
        public boolean equals(Object other) { return ((Dot)other).x == x && ((Dot)other).y == y; }
        public int hashCode() { return this.x + this.y; }
    }
}
