import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Objects;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class d15 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var risks = reader.lines().collect(Collectors.toList());
        out.println(shortest(risks.size(), (x, y) -> risks.get(y).charAt(x) - '0'));
        out.println(shortest(risks.size() * 5, (x, y) -> {
            var r = risks.get(y % risks.size()).charAt(x % risks.size()) - '0' + x / risks.size() + y / risks.size();
            return r > 9 ? r - 9 : r;
        }));
    }

    static int shortest(int size, RiskFunc risk) {
        var field = new HashMap<Position, Integer>();
        field.put(new Position(0, 0), 0);
        NeighborFunc neighbor = (x, y, dx, dy) -> {
            var r = risk.risk(x, y);
            var pn = new Position(x + dx, y + dy);
            var p = new Position(x, y);
            if (field.getOrDefault(pn, Integer.MAX_VALUE / 2) + r < field.getOrDefault(p, Integer.MAX_VALUE / 2)) {
                field.put(p, field.getOrDefault(pn, Integer.MAX_VALUE / 2) + r);
                return true;
            }
            return false;
        };
        boolean shorter;
        do {
            shorter = false;
            for (int x = 0; x < size && !shorter; x++) {
                for (int y = 0; y < size && !shorter; y++) {
                    shorter = neighbor.shorter(x, y, -1, 0) || neighbor.shorter(x, y, +1, 0) || neighbor.shorter(x, y, 0, -1) || neighbor.shorter(x, y, 0, +1);
                }
            }
        } while (shorter);
        return field.getOrDefault(new Position(size - 1, size - 1), Integer.MAX_VALUE / 2);
    }

    static class Position {
        final int x, y;
        Position(int x, int y) { this.x = x; this.y = y; }
        public boolean equals(Object other) { return x == ((Position)other).x && y == ((Position)other).y; }
        public int hashCode() { return Objects.hash(this.x, this.y); }
    }

    interface RiskFunc {
        int risk(int x, int y);
    }
    interface NeighborFunc {
        boolean shorter(int x, int y, int dx, int dy);
    }
}
