import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class d4 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<Integer> numbers = new ArrayList<>();
        for (String s : scanner.nextLine().split(",")) {
            if (s != null && !s.isEmpty()) {
                numbers.add(Integer.valueOf(s));
            }
        }
        List<Board> boards = new ArrayList<>();
        while (scanner.hasNext()) {
            scanner.nextLine();
            boards.add(new Board(scanner));
        }
        int first = 0, last = 0, wins = 0;
        for (Integer n : numbers) {
            for (Board b : boards) {
                if (b.win()) {
                    continue;
                }
                b.mark(n);
                if (b.win()) {
                    if (++wins == 1) {
                        first = b.score(n);
                    } else {
                        last = b.score(n);
                    }
                }
            }
        }
        out.println(first);
        out.println(last);
    }

    static class Board {
        private static final int MARK = -1;
        private final int size;
        private final int[] numbers;

        Board(Scanner scanner) {
            String[] first = scanner.nextLine().strip().split("\\s+");
            this.size = (int)Arrays.stream(first).filter(v -> v != null && !v.isEmpty()).count();
            this.numbers = new int[this.size * this.size];
            for (int c = 0; c < this.size; c++) {
                this.numbers[c] = Integer.parseInt(first[c]);
            }
            for (int r = 1; r < this.size; r++) {
                int c = 0;
                for (String s : scanner.nextLine().strip().split("\\s+")) {
                    this.numbers[r * this.size + c++] = Integer.parseInt(s);
                }
            }
        }

        public String toString() {
            return String.valueOf(this.size) + ": [" + Arrays.stream(this.numbers).mapToObj(Integer::toString).collect(Collectors.joining(", ")) + "] addition ";
        }

        void mark(int n) {
            for (int i = 0; i < this.numbers.length; i++) {
                if (this.numbers[i] == n) {
                    this.numbers[i] = MARK;
                }
            }
        }

        boolean win() {
            for (int r = 0; r < this.size; r++) {
                boolean marked = true;
                for (int i = 0; i < this.size && marked; i++) {
                    if (this.numbers[r * this.size + i] != MARK) marked = false;
                }
                if (marked) return true;    
            }
            for (int c = 0; c < this.size; c++) {
                boolean marked = true;
                for (int r = 0; r < this.size && marked; r++) {
                    if (this.numbers[r * this.size + c] != MARK) marked = false;
                }
                if (marked) return true;    
            }
            return false;
        }

        int score(int n) {
            return ((int)Arrays.stream(this.numbers).filter(v -> v == MARK).count() + (int)Arrays.stream(this.numbers).sum()) * n;
        }
    }
}
