import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class d13 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var dots = new HashSet<Dot>();
        var foldings = new ArrayList<Folding>();
        reader.lines().forEach(line -> {
            if (line.startsWith("fold"))
                foldings.add(new Folding(line));
            else if (!line.isBlank())
                dots.add(Dot.parse(line));
        });
        out.println(dots.stream().map(d -> d.fold(foldings.get(0))).collect(Collectors.toSet()).size());
        Set<Dot> result = new HashSet<>(dots);
        for (var f : foldings)
            result = result.stream().map(d -> d.fold(f)).collect(Collectors.toSet());
        for (int y = 0; y < result.stream().mapToInt(d -> d.y).max().getAsInt() + 1; y++) {
            for (int x = 0; x < result.stream().mapToInt(d -> d.x).max().getAsInt() + 1; x++)
                out.print(result.contains(new Dot(x, y)) ? "#" : ".");
            out.println();
        }
    }

    record Dot (int x, int y) {
        static Dot parse(String s) {
            var pair = s.split(",");
            return new Dot(parseInt(pair[0]), parseInt(pair[1]));
        }

        Dot fold(Folding f) {
            if (f.axis.equals("x"))
                return x > f.pos ? new Dot(f.pos - (x - f.pos), y) : this;
            else
                return y > f.pos ? new Dot(x, f.pos - (y - f.pos)) : this;
        }
    }

    static class Folding {
        static Pattern foldPtn = Pattern.compile("fold along (x|y)\\=(\\d+)");
        final String axis;
        final int pos;

        Folding(String line) {
            var matcher = foldPtn.matcher(line);
            if (!matcher.matches())
                throw new RuntimeException("invalid input " + line);
            this.axis = matcher.group(1);
            this.pos = parseInt(matcher.group(2));
        }
    }
}