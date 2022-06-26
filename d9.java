import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class d9 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var field = new Field(reader);
        out.println(field.risk());
        var sizes = field.basins().stream().sorted().collect(Collectors.toList());
        out.println(sizes.get(sizes.size() - 1) * sizes.get(sizes.size() - 2) * sizes.get(sizes.size() - 3));
    }

    static class Position {
        final int x, y;

        Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public boolean equals(Object other) {
            return x == ((Position)other).x && y == ((Position)other).y;
        }
        public int hashCode() {
            return Objects.hash(this.x, this.y);
        }
    }

    static class Field {
        final List<String> heights = new ArrayList<>();

        Field(BufferedReader reader) {
            reader.lines().forEach(line -> heights.add("9" + line + "9"));
            heights.add("9".repeat(heights.get(0).length()));
            heights.add(0, "9".repeat(heights.get(0).length()));
        }

        boolean low(int x, int y) {
            char c = heights.get(y).charAt(x);
            return heights.get(y-1).charAt(x) > c && heights.get(y).charAt(x-1) > c && heights.get(y+1).charAt(x) > c && heights.get(y).charAt(x+1) > c;
        }

        int risk() {
            int result = 0;
            for (int y = 1; y < heights.size() - 1; y++) {
                for (int x = 1; x < heights.get(0).length() - 1; x++) {
                    if (this.low(x, y)) {
                        result += 1 + (int)heights.get(y).charAt(x)-'0';
                    }
                }
            }
            return result;
        }

        List<Integer> basins() {
            var processed = new HashSet<Position>();
            var result = new ArrayList<Integer>();
            var queue = new LinkedList<Position>();
            for (int y = 1; y < heights.size() - 1; y++) {
                for (int x = 1; x < heights.get(0).length() - 1; x++) {
                    if (processed.contains(new Position(x, y)) || heights.get(y).charAt(x) == '9') {
                        continue;
                    }
                    queue.add(new Position(x, y));
                    var basin = new HashSet<Position>();
                    while (!queue.isEmpty()) {
                        var p = queue.poll();
                        if (processed.contains(p) || heights.get(p.y).charAt(p.x) == '9') {
                            continue;
                        }
                        basin.add(p);
                        processed.add(p);
                        queue.add(new Position(p.x - 1, p.y));
                        queue.add(new Position(p.x + 1, p.y));
                        queue.add(new Position(p.x, p.y - 1));
                        queue.add(new Position(p.x, p.y + 1));
                    }
                    result.add(basin.size());
                }
            }
            return result;
        }
    }
}


