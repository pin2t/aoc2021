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
            if (line.startsWith("fold")) {
                foldings.add(new Folding(line));
            } else if (!line.isBlank()) {
                dots.add(new Dot(line));
            }
        });
        out.println(dots.stream().map(d -> d.fold(foldings.get(0))).collect(Collectors.toSet()).size());
        Set<Dot> result = new HashSet<>(dots);
        for (var f : foldings) {
            result = result.stream().map(d -> d.fold(f)).collect(Collectors.toSet());
        }
        for (int y = 0; y < result.stream().mapToInt(d -> d.y).max().getAsInt() + 1; y++) {
            for (int x = 0; x < result.stream().mapToInt(d -> d.x).max().getAsInt() + 1; x++) {
                if (result.contains(new Dot(x, y))) {
                    out.print("#");
                } else {
                    out.print(".");
                }
            }
            out.println();
        }
    }
}

class Dot {
    final int x, y;

    Dot(String s) {
        var pair = s.split(",");
        this.x = parseInt(pair[0]);
        this.y = parseInt(pair[1]);
    }
    Dot(int x, int y) {
        this.x = x;
        this.y = y;
    }

    Dot fold(Folding f) {
        if (f.axis.equals("x")) {
            if (x > f.pos) {
                return new Dot(f.pos - (x - f.pos), y);
            } else {
                return this;
            }
        } else {
            if (y > f.pos) {
                return new Dot(x, f.pos - (y - f.pos));
            } else {
                return this;
            }
        }
    }
    public boolean equals(Object other) { 
        return ((Dot)other).x == x && ((Dot)other).y == y; 
    }
    public int hashCode() { 
        return this.x + this.y; 
    }
}

class Folding {
    static Pattern foldPtn = Pattern.compile("fold along (x|y)\\=(\\d+)");
    final String axis;
    final int pos;

    Folding(String line) {
        var matcher = foldPtn.matcher(line);
        if (!matcher.matches()) {
            throw new RuntimeException("invalid input " + line);
        }
        this.axis = matcher.group(1);
        this.pos = parseInt(matcher.group(2));
    }
}
