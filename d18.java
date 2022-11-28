import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class d18 {
    static Pattern pairPtn = Pattern.compile("\\[(\\d+),(\\d+)\\].*?");

    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var numbers = reader.lines().toList();
        var result = numbers.get(0);
        for (var i = 1; i < numbers.size(); i++) {
            result = sum(result, numbers.get(i));
        }
        out.println(magnitude(result, 0));
        var maxmag = 0;
        for (var i = 1; i < numbers.size(); i++) {
            for (var j = 1; j < numbers.size(); j++) {
                if (i == j) continue;
                maxmag = Math.max(maxmag, magnitude(sum(numbers.get(i), numbers.get(j)), 0).a);
            }
        }
        out.println(maxmag);
    }

    static String sum(String a, String b) {
        var n = "[" + a + "," + b + "]";
        var r = reduce(n);
        while (!r.equals(n)) {
            n = r;
            r = reduce(n);
        }
        return n;
    }

    static String reduce(String n) {
        int depth = 0;
        for (int pos = 0; pos < n.length(); pos++) {
            var matcher = pairPtn.matcher(n.substring(pos));
            if (depth >= 4 && matcher.matches()) {
                return explode(n, pos, matcher);
            }
            if (n.charAt(pos) == '[') depth++;
            if (n.charAt(pos) == ']') depth--;
        }
        for (int pos = 0; pos < n.length(); pos++) {
            if (isdigit(n, pos) && peekRegular(n, pos) > 9) {
                return split(n, pos);
            }
        }
        return n;
    }

    static String explode(String n, int pos, Matcher matcher) {
        var left = Integer.parseInt(matcher.group(1));
        var right = Integer.parseInt(matcher.group(2));
        var result = n.substring(0, pos) + "0" + n.substring(pos + matcher.end(2) + 1);
        var p = pos + 1;
        while (p < result.length() && !isdigit(result, p)) p++;
        if (p < result.length()) {
            result = replaceRegular(result, p, peekRegular(result, p) + right);
        }
        p = pos - 1;
        while (p > 0 && !isdigit(result, p)) p--;
        if (p > 0) {
            result = replaceRegular(result, p, peekRegular(result, p) + left);
        }
        return result;
    }

    static String split(String n, int pos) {
        var regular = peekRegular(n, pos);
        return n.substring(0, pos) + "[" + regular / 2 + "," + (regular / 2 + regular % 2) + "]" + n.substring(pos + 2);
    }

    static boolean isdigit(String s, int pos) { return s.charAt(pos) >= '0' && s.charAt(pos) <= '9'; }
    static int peekRegular(String s, int pos) {
        while (pos > 0 && isdigit(s, pos - 1)) pos--;
        int result = s.charAt(pos) - '0';
        pos++;
        while (pos < s.length() && isdigit(s, pos)) {
            result = result * 10 + (s.charAt(pos) - '0');
            pos++;
        }
        return result;
    }

    static String replaceRegular(String s, int pos, int n) {
        while (pos > 0 && isdigit(s, pos - 1)) pos--;
        int l = 1;
        while (pos + l < s.length() && isdigit(s, pos + l)) l++;
        return s.substring(0, pos) + n + s.substring(pos + l);
    }

    static IgnorePair magnitude(String n, int pos) {
        if (n.charAt(pos) == '[') {
            var mleft = magnitude(n, pos + 1);
            var mright = magnitude(n, mleft.b);
            return new IgnorePair(mleft.a * 3 + mright.a * 2, mright.b);
        } else {
            return new IgnorePair(n.charAt(pos) - '0', pos + 1);
        }
    }

    record IgnorePair(int a, int b) {
        public String toString() { return Integer.toString(a); }
    }
}
