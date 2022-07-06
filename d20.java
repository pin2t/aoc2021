import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class d20 {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var algo = reader.readLine();
        reader.readLine();
        var field = Field.parse(reader);
        out.println(field.index(4, 4));
        field = field.enhance(algo);
        field = field.enhance(algo);
        out.println(field.pixels());
    }

    static class Field {
        final List<String> rows = new ArrayList<>();

        private Field() {
        }

        Field(int size) {
            for (int r = 0; r < size; r++) {
                rows.add(".".repeat(size));
            }
        }

        static Field parse(BufferedReader reader) {
            var lines = reader.lines().toList();
            var field = new Field();
            var size = lines.size() + 4;
            for (int i = 0; i < 2; i++)
                field.rows.add(".".repeat(size));
            for (var line : lines)
                field.rows.add(".." + line + "..");
            for (int i = 0; i < 2; i++)
                field.rows.add(".".repeat(size));
            return field;
        }

        int index(int r, int c) {
            int i = 0;
            i = i * 2 + (this.rows.get(r - 1).charAt(c - 1) == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r - 1).charAt(c)     == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r - 1).charAt(c + 1) == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r).charAt(c - 1)     == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r).charAt(c)         == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r).charAt(c + 1)     == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r + 1).charAt(c - 1) == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r + 1).charAt(c)     == '#' ? 1 : 0);
            i = i * 2 + (this.rows.get(r + 1).charAt(c + 1) == '#' ? 1 : 0);
            return i;
        }

        Field enhance(String algo) {
            var result = new Field(this.rows.size() + 4);
            for (int r = 1; r < this.rows.size() - 1; r++) {
                for (int c = 1; c < this.rows.get(r).length() - 1; c++) {
                    String s = result.rows.get(r + 2);
                    result.rows.set(r, s.substring(0, c + 2) + algo.charAt(this.index(c, r)) + s.substring(c + 3));
                }
            }
            return result;
        }

        int pixels() {
            return this.rows.stream()
                    .mapToInt(r -> (int)r.chars().filter(c -> c == '#').count())
                    .sum();
        }

        void print() {
            for (var row : this.rows) out.println(row);
        }
    }
}
