import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Pattern;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;
import static java.util.Arrays.stream;

public class d5 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var num = Pattern.compile("(\\d+),(\\d+)\\s\\->\\s(\\d+),(\\d+)");
        var board = new Board();
        var board2 = new Board();
        reader.lines().forEach(line -> {
            var matcher = num.matcher(line);
            if (!matcher.matches()) {
                throw new RuntimeException("invalid input " + line);
            }
            int x1 = parseInt(matcher.group(1)), y1 = parseInt(matcher.group(2)),
                x2 = parseInt(matcher.group(3)), y2 = parseInt(matcher.group(4));
            if (x1 == x2 || y1 == y2) {
                board.mark(x1, y1, x2, y2);
                board2.mark(x1, y1, x2, y2);
            }
            if (Math.abs(x1 - x2) == Math.abs(y1 - y2)) {
                board2.mark(x1, y1, x2, y2);
            }
        });
        out.println(board.points());
        out.println(board2.points());
    }
}

class Board {
    static final int CAPACITY = 1000;
    final int[] numbers;

    Board() {
        this.numbers = new int[CAPACITY * CAPACITY];
    }

    void mark(int x1, int y1, int x2, int y2) {
        if (x1 == x2) {
            for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) {
                this.numbers[y * CAPACITY + x1]++;
            }
        } else if (y1 == y2) {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                this.numbers[y1 * CAPACITY + x]++;
            }
        } else if (x1 - x2 == y1 - y2) {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                this.numbers[(Math.min(y1, y2) + (x - Math.min(x1, x2))) * CAPACITY + x]++;
            }
        } else if (x1 - x2 == y2 - y1) {
            for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) {
                this.numbers[(Math.max(y1, y2) - (x - Math.min(x1, x2))) * CAPACITY + x]++;
            }
        }
    }

    int points() {
        return (int) stream(this.numbers).filter(v -> v > 1).count();
    }
}
