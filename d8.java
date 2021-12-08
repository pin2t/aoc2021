import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.regex.Pattern;

public class d8 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var numbers = new AtomicInteger(0);
        var segments = Pattern.compile("[a-z]+");
        var decoded = new ArrayList<Integer>();
        var mappings = new ArrayList<String>();
        fillMappings(mappings, "", "abcdefg");
        var templates = new String[] {"abcefg", "cf", "acdeg", "acdfg", "bcdf", "abdfg", "abdefg", "acf", "abcdefg", "abcdfg"}; 
        reader.lines().forEach(line -> {
            var parts = line.split("\\|");
            var matcher = segments.matcher(parts[1]);
            while (matcher.find()) {
                var l = matcher.group().length();
                if (l == 2 || l == 3 || l == 4 || l == 7) {
                    numbers.incrementAndGet();
                }
            }
            var numSegments = new ArrayList<String>();
            matcher = segments.matcher(parts[0]);
            while (matcher.find()) numSegments.add(matcher.group());
            for (var mapping : mappings) {
                var valid = new HashSet<Integer>();
                for (var numSeg : numSegments) {
                    for (int n = 0; n < templates.length; n++) {
                        if (templates[n].equals(map(mapping, numSeg)))  {
                            valid.add(n);
                        }
                    }
                }
                if (valid.size() == 10) {
                    matcher = segments.matcher(parts[1]);
                    int nn = 0;
                    while (matcher.find()) {
                        for (int n = 0; n < templates.length; n++) {
                            if (templates[n].equals(map(mapping, matcher.group())))  {
                                nn = nn * 10 + n;
                                break;
                            }
                        }
                    }
                    decoded.add(nn);
                    break;
                }
            }
        });
        System.out.println(numbers.get());
        System.out.println(decoded.stream().mapToInt(Integer::intValue).sum());
    }

    static String map(String mapping, String s) {
        char[] result = s.toCharArray();
        for (int i = 0; i < result.length; i++) {
            result[i] = mapping.charAt(result[i] - 'a');
        }
        Arrays.sort(result);
        return new String(result);
    }

    static void fillMappings(List<String> mappings, String prefix, String str) {
        int n = str.length();
        if (n == 0) {
            mappings.add(prefix);
        } else {
            for (int i = 0; i < n; i++) {
                fillMappings(mappings, prefix + str.charAt(i), str.substring(0, i) + str.substring(i+1, n));
            }
        }
    }    
}