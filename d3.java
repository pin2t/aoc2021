import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class d3 {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var values = new ArrayList<String>();
        while (scanner.hasNext()) {
            values.add(scanner.next().strip());
        }
        int gamma = 0, epsilon = 0;
        for (int bit = 0; bit < values.get(0).length(); bit++) {
            var bb = bit;
            var zeros = values.stream().filter(v -> v.charAt(bb) == '0').count();
            if (zeros > values.size() - zeros) { 
                gamma *= 2; 
                epsilon = epsilon * 2 + 1; 
            } else {
                gamma = gamma * 2 + 1; 
                epsilon *= 2; 
            }
        }
        out.println(gamma * epsilon);
        int oxygen = parseInt(filter(values, (zeros, ones) -> (zeros > ones ? '0' : '1')), 2);
        int co2 = parseInt(filter(values, (zeros, ones) -> (zeros > ones ? '1' : '0')), 2);
        out.println(oxygen * co2);
    }

    static String filter(List<String> values, BitFunc f) {
        List<String> left = new ArrayList<>(values);
        for (int bit = 0; bit < values.get(0).length() && left.size() > 1; bit++) {
            int bb = bit;
            long zeros = left.stream().filter(v -> v.charAt(bb) == '0').count();
            long ones = left.size() - zeros;
            left = left.stream().filter(v -> v.charAt(bb) == f.bit((int)zeros, (int)ones)).collect(Collectors.toList());
        }   
        return left.get(0);
    }

    interface BitFunc {
        char bit(int zeros, int ones);
    }
}
