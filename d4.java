import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.out;
import static java.util.Arrays.stream;

import java.io.*;

public class d4 {
    public static void main(String[] args) throws NumberFormatException, IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var numbers = new ArrayList<Integer>();
        for (String s : reader.readLine().split(",")) {
            if (s != null && !s.isEmpty()) {
                numbers.add(Integer.valueOf(s));
            }
        }
        var boards = new ArrayList<Board>();
        while (reader.ready()) {
            reader.readLine();
            boards.add(new Board(reader));
        }
        int first = 0, last = 0, wins = 0;
        for (var n : numbers) {
            for (var b : boards) {
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
        static final int MARK = -1;
        final int size;
        final int[] numbers;

        Board(BufferedReader reader) throws NumberFormatException, IOException {
            var first = reader.readLine().strip().split("\\s+");
            this.size = (int) stream(first).filter(v -> v != null && !v.isEmpty()).count();
            this.numbers = new int[this.size * this.size];
            for (var c = 0; c < this.size; c++) {
                this.numbers[c] = Integer.parseInt(first[c]);
            }
            for (var r = 1; r < this.size; r++) {
                var c = 0;
                for (var s : reader.readLine().strip().split("\\s+")) {
                    this.numbers[r * this.size + c++] = Integer.parseInt(s);
                }
            }
        }

        public String toString() {
            return String.format("%d: [%s]", this.size, stream(this.numbers).mapToObj(Integer::toString).collect(Collectors.joining(", ")));
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
                var marked = true;
                for (int i = 0; i < this.size && marked; i++) {
                    if (this.numbers[r * this.size + i] != MARK) marked = false;
                }
                if (marked) return true;    
            }
            for (int c = 0; c < this.size; c++) {
                var marked = true;
                for (int r = 0; r < this.size && marked; r++) {
                    if (this.numbers[r * this.size + c] != MARK) marked = false;
                }
                if (marked) return true;    
            }
            return false;
        }

        int score(int n) {
            return ((int) stream(this.numbers).filter(v -> v == MARK).count() + (int) stream(this.numbers).sum()) * n;
        }
    }
}
