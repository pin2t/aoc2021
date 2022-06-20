import static java.lang.System.out;

public class d17 {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var ints = scanner.readInts();
        int fromx = ints[0], tox = ints[1], fromy = ints[2], toy = ints[3];
        var highest = 0;
        var velocities = 0;
        for (int vx = 1; vx <= Math.max(fromx, tox); vx++) {
            for (int vy = -100; vy <= 100; vy++) {
                int px = 0, py = 0, high = 0, dx = vx, dy = vy;
                var hit = false;
                while (!hit && px <= Math.max(fromx, tox) && py >= Math.min(fromy, toy)) {
                    px += dx; py += dy;
                    dx = Math.max(dx - 1, 0); dy--;
                    high = Math.max(high, py);
                    if (px >= Math.min(fromx, tox) && px <= Math.max(fromx, tox) && py >= Math.min(fromy, toy) && py <= Math.max(fromy, toy)) {
                        hit = true;
                        highest = Math.max(high, highest);
                        velocities++;
                    }
                }
            }
        }
        out.println(highest);
        out.println(velocities);
    }
}
