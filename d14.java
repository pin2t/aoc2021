import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.function.Supplier;

import static java.lang.System.out;

public class d14 {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var pairs = new HashMap<String, Long>();
        var counts = new HashMap<Character, Long>();
        var line = reader.readLine();
        for (int i = 0; i < line.length() - 1; i++) {
            pairs.merge(line.substring(i, i+2), 1L, Long::sum);
            counts.merge(line.charAt(i), 1L, Long::sum);
        }
        counts.merge(line.charAt(line.length() - 1), 1L, Long::sum);
        reader.readLine();
        var rules = new HashMap<String, Character>();
        while (reader.ready()) {
            line = reader.readLine();
            rules.put(line.substring(0, 2), line.charAt(6));
        }
        Supplier<Long> difference = () -> counts.values().stream().max(Long::compareTo).get() - counts.values().stream().min(Long::compareTo).get();
        for (int step = 1; step <= 40; step++) {
            var newpairs = new HashMap<String, Long>();
            for (var pair : pairs.entrySet()) {
                if (rules.containsKey(pair.getKey())) {
                    newpairs.merge(pair.getKey().substring(0, 1) + rules.get(pair.getKey()), pair.getValue(), Long::sum);
                    newpairs.merge(rules.get(pair.getKey()) + pair.getKey().substring(1, 2), pair.getValue(), Long::sum);
                    counts.merge(rules.get(pair.getKey()), pair.getValue(), Long::sum);
                }
            }
            if (step == 10) {
                out.println(difference.get());
            }
            pairs = newpairs;
        }
        out.println(difference.get());
    }
}
