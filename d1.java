import static java.lang.System.out;

public class d1 {
    public static void main(String[] args) {
        var scanner = new BeaconsScanner(System.in);
        var prev = new int[] {scanner.nextInt(), scanner.nextInt(), scanner.nextInt()};
        int result1 = 0, result2 = 0;
        if (prev[1] > prev[0]) result1++;
        if (prev[2] > prev[1]) result1++;
        while (scanner.hasNext()) {
            int val = scanner.nextInt();
            if (val > prev[2]) result1++;
            if (val > prev[0]) result2++;
            prev[0] = prev[1]; prev[1] = prev[2]; prev[2] = val;
        }
        out.println(result1);
        out.println(result2);
    }
}