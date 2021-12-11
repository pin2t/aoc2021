import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

import static java.lang.System.out;

public class d11 {
    public static void main(String[] args) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        Board board = new Board(reader);
        int result = 0, s = 1, all = 0;
        while (s <= 100) {
            int flashes = board.step();
            result += flashes;
            if (flashes == board.size * board.size && all == 0) {
                all = s;
            }
            s++;
        }
        out.println(result);
        if (all > 0) {
            out.println(all);
        } else {
            while (board.step() < board.size * board.size) s++;
            out.println(s);
        }
    }

    static class Board {
        static final int MAXSIZE = 1000;
        final int[] energy;
        final int size;

        Board(BufferedReader reader) {
            this.energy = new int[MAXSIZE * MAXSIZE];
            for (int i = 0; i < this.energy.length; i++) {
                this.energy[i] = -1000;
            }
            int i = 1;
            for (String line : reader.lines().collect(Collectors.toList())) {
                for (int j = 1; j <= line.length(); j++) {
                    this.energy[i * MAXSIZE + j] = line.charAt(j - 1) - '0';
                }
                i++;
            }
            this.size = i - 1;
        }

        void print() {
            for (int i = 1; i <= this.size; i++) {
                for (int j = 1; j <= this.size; j++) {
                    out.print(" " + this.energy[i * MAXSIZE + j]);
                }
                out.println();
            }
        }

        int step() {
            for (int i = 1; i <= this.size; i++) {
                for (int j = 1; j <= this.size; j++) {
                    this.energy[i * MAXSIZE + j]++;
                }
            }
            int i = 1, flashes = 0;
            while (i <= this.size) {
                int j = 1;
                while (j <= this.size) {
                    if (this.energy[i * MAXSIZE + j] > 9) {
                        this.energy[(i - 1) * MAXSIZE + j]++;
                        this.energy[(i + 1) * MAXSIZE + j]++;
                        this.energy[(i - 1) * MAXSIZE + j - 1]++;
                        this.energy[(i + 1) * MAXSIZE + j + 1]++;
                        this.energy[i * MAXSIZE + j - 1]++;
                        this.energy[i * MAXSIZE + j + 1]++;
                        this.energy[(i - 1) * MAXSIZE + j + 1]++;
                        this.energy[(i + 1) * MAXSIZE + j - 1]++;
                        this.energy[i * MAXSIZE + j] = -100;
                        i = 0;
                        flashes++;
                        break;
                    }
                    j++;
                }
                i++;
            }
            for (i = 1; i <= this.size; i++) {
                for (int j = 1; j <= this.size; j++) {
                    if (this.energy[i * MAXSIZE + j] < 0) {
                        this.energy[i * MAXSIZE + j] = 0;
                    }
                }
            }
            return flashes;
        }
    }
}
