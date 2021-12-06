import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class d5 {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Pattern num = Pattern.compile("(\\d+),(\\d+)\\s\\->\\s(\\d+),(\\d+)");
        Board board = new Board(), board2 = new Board();
        reader.lines().forEach(line -> {
            Matcher matcher = num.matcher(line);
            if (!matcher.matches())
                throw new RuntimeException("invalid input " + line);
            int x1 = Integer.parseInt(matcher.group(1)), y1 = Integer.parseInt(matcher.group(2)),
                x2 = Integer.parseInt(matcher.group(3)), y2 = Integer.parseInt(matcher.group(4));
            if (x1 == x2 || y1 == y2) {
                board.mark(x1, y1, x2, y2);
                board2.mark(x1, y1, x2, y2);
            }
            if (Math.abs(x1 - x2) == Math.abs(y1 - y2)) {
                board2.mark(x1, y1, x2, y2);
            }
        });
        System.out.println(board.points());
        System.out.println(board2.points());
    }

    static class Board {
        private static final int SIZE = 1000;
        private final int[] numbers;

        Board() {
            this.numbers = new int[SIZE * SIZE];
        }

        void mark(int x1, int y1, int x2, int y2) {
            if (x1 == x2) {
                for (int y = Math.min(y1, y2); y <= Math.max(y1, y2); y++) this.numbers[y * SIZE + x1]++;
            } else if (y1 == y2) {
                for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) this.numbers[y1 * SIZE + x]++;
            } else if (x1 - x2 == y1 - y2) {
                for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) this.numbers[(Math.min(y1, y2) + (x - Math.min(x1, x2))) * SIZE + x]++;
            } else if (x1 - x2 == y2 - y1) {
                for (int x = Math.min(x1, x2); x <= Math.max(x1, x2); x++) this.numbers[(Math.max(y1, y2) - (x - Math.min(x1, x2))) * SIZE + x]++;
            }
        }

        int points() {
            return (int)Arrays.stream(this.numbers).filter(v -> v > 1).count();
        }
    }
}