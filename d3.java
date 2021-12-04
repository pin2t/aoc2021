import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class d3 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        List<String> values = new ArrayList<>();
        while (scanner.hasNext()) values.add(scanner.next().strip());
        int gamma = 0, epsilon = 0;
        for (int bit = 0; bit < values.get(0).length(); bit++) {
            int bb = bit;
            long zeros = values.stream().filter(v -> v.charAt(bb) == '0').count();
            if (zeros > values.size() - zeros) { 
                gamma *= 2; 
                epsilon = epsilon * 2 + 1; 
            } else {
                gamma = gamma * 2 + 1; 
                epsilon *= 2; 
            }
        }
        System.out.println(gamma * epsilon);
        int oxygen = Integer.parseInt(filter(values, (zeros, ones) -> (zeros > ones ? '0' : '1')), 2);
        int co2 = Integer.parseInt(filter(values, (zeros, ones) -> (zeros > ones ? '1' : '0')), 2);
        System.out.println(oxygen * co2);
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
