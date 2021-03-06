import static java.lang.System.out;

public class d2 {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        long depth = 0, depth2 = 0, aim = 0, position = 0;
        while (scanner.hasNext()) {
            var cmd = scanner.next();
            var arg = scanner.nextInt();
            switch (cmd) {
                case "forward": position += arg; depth2 += arg * aim; break;
                case "up": depth -= arg; aim -= arg; break;
                case "down": depth += arg; aim += arg; break;
            }
        }
        out.println(position * depth);
        out.println(position * depth2);
    }
}
