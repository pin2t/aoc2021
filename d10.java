import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.stream.Collectors;

public class d10 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var closing = new HashMap<String, String>() {{put("(", ")"); put("[", "]"); put("{", "}"); put("<", ">");}};
        var scores = new HashMap<String, Long>() {{put(")", 3L); put("]", 57L); put("}", 1197L); put(">", 25137L);}};
        var incScore = new HashMap<String, Long>() {{put("(", 1L); put("[", 2L); put("{", 3L); put("<", 4L);}};
        var error = 0L;
        var incompletes = new ArrayList<Long>();
        for (String line : reader.lines().collect(Collectors.toList())) {
            var i = 0;
            var stack = new LinkedList<String>();
            while (i < line.length()) {
                if (closing.containsKey(line.substring(i, i + 1))) {
                    stack.push(line.substring(i, i + 1));
                } else {
                    if (!closing.get(stack.pop()).equals(line.substring(i, i + 1))) {
                        error += scores.get(line.substring(i, i + 1));
                        break;
                    }
                }
                i++;
            }
            if (i == line.length()) {
                long incomplete = 0L;
                while (!stack.isEmpty()) {
                    incomplete = incomplete * 5L + incScore.get(stack.pop());
                }
                incompletes.add(incomplete);
            }
        }
        incompletes.sort(Long::compareTo);
        System.out.println(error + " " + incompletes.get(incompletes.size() / 2));
    }
}
