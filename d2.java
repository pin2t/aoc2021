public class d2 {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        long depth = 0, depth2 = 0, aim = 0, position = 0;
        while (scanner.hasNext()) {
            String cmd = scanner.next();
            long arg = scanner.nextInt();
            switch (cmd) {
                case "forward": position += arg; depth2 += arg * aim; break;
                case "up": depth -= arg; aim -= arg; break;
                case "down": depth += arg; aim += arg; break;
            }
        }
        System.out.println(position * depth);
        System.out.println(position * depth2);
    }
}
