import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class d15 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var risks = reader.lines().collect(Collectors.toList());
        out.println(shortest(new Board() {
            public int size()             { return risks.size(); }
            public int risk(int x, int y) { return risks.get(y).charAt(x) - '0'; }
        }));
        out.println(shortest(new Board() {
            public int size()             { return risks.size() * 5; }
            public int risk(int x, int y) {
                var r = risks.get(y % risks.size()).charAt(x % risks.size()) - '0' + x / risks.size() + y / risks.size();
                return r > 9 ? r - 9 : r;
            }
        }));
    }

    static int shortest(Board board) {
        var field = new int[(board.size() + 2)*(board.size() + 2)];
        Arrays.fill(field, Integer.MAX_VALUE / 2);
        field[(board.size() + 2) + 1] = 0;
        Neighbors neighbors = (x, y, dx, dy) -> {
            var r = board.risk(x, y);
            if (field[(x + dx) + 1 + ((y + dy) + 1) * (board.size() + 2)] + r < field[x + 1 + (y + 1) * (board.size() + 2)]) {
                field[x + 1 + (y + 1) * (board.size() + 2)] = field[(x + dx) + 1 + ((y + dy) + 1) * (board.size() + 2)] + r;
                return true;
            }
            return false;
        };
        boolean shorter;
        do {
            shorter = false;
            for (int x = 0; x < board.size(); x++) {
                for (int y = 0; y < board.size(); y++) {
                    if (shorter = neighbors.adjust(x, y, -1, 0)) break;
                    if (shorter = neighbors.adjust(x, y, +1, 0)) break;
                    if (shorter = neighbors.adjust(x, y, 0, -1)) break;
                    if (shorter = neighbors.adjust(x, y, 0, +1)) break;
                }
            }
        } while (shorter);
        return field[board.size() + (board.size()) * (board.size() + 2)];
    }

    interface Board {
        int size();
        int risk(int x, int y);
    }
    interface Neighbors {
        boolean adjust(int x, int y, int dx, int dy);
    }
}
