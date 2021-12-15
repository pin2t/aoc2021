import java.io.BufferedReader;
import java.io.InputStreamReader;
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
        var field = new int[(size+2)*(size + 2)];
        for (int i = 0; i < field.length; i++) field[i] = Integer.MAX_VALUE / 2;
        field[(size + 2) + 1] = 0;
        NeighborFunc neighbor = (x, y, dx, dy) -> {
            var r = risk.risk(x, y);
            if (field[(x + dx) + 1 + ((y + dy) + 1) * (size + 2)] + r < field[x + 1 + (y + 1) * (size + 2)]) {
                field[x + 1 + (y + 1) * (size + 2)] = field[(x + dx) + 1 + ((y + dy) + 1) * (size + 2)] + r;
                return true;
            }
            return false;
        };
        boolean shorter;
        do {
            shorter = false;
            for (int x = 0; x < size; x++) {
                for (int y = 0; y < size; y++) {
                    shorter = neighbor.shorter(x, y, -1, 0); if (shorter) break;
                    shorter = neighbor.shorter(x, y, +1, 0); if (shorter) break;
                    shorter = neighbor.shorter(x, y, 0, -1); if (shorter) break;
                    shorter = neighbor.shorter(x, y, 0, +1); if (shorter) break;
                }
            }
        } while (shorter);
        return field[size + (size) * (size + 2)];
    }

    interface RiskFunc {
        int risk(int x, int y);
    }
    interface NeighborFunc {
        boolean shorter(int x, int y, int dx, int dy);
    }
}
