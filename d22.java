import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class d22 {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var on = new HashSet<Point>();
        var cuboids = new ArrayList<Cuboid>();
        var part1 = new Cuboid(-50, 50, -50, 50, -50, 50, false);
        while (reader.ready()) {
            var line = reader.readLine();
            var c = new Cuboid(line);
            // part 1
            if (c.intersect(part1)) {
                var p1c = c.intersection(part1);
                for (var x = p1c.x1; x <= p1c.x2; x++) {
                    for (var y = p1c.y1; y <= p1c.y2; y++) {
                        for (var z = p1c.z1; z <= p1c.z2; z++) {
                            if (line.startsWith("on"))
                                on.add(new Point(x, y, z));
                            else
                                on.remove(new Point(x, y, z));
                        }
                    }
                }
            }
            // part 2
            var intersections = new ArrayList<Cuboid>();
            for (var cc : cuboids) {
                if (c.intersect(cc)) {
                    intersections.add(c.intersection(cc));
                }
            }
            cuboids.addAll(intersections);
            if (c.on) {
                cuboids.add(c);
            }
        }
        out.println(on.size());
        out.println(cuboids.stream().mapToLong(c -> c.on ? c.cubes() : -c.cubes()).sum());
    }

    record Point(int x, int y, int z) {}

    static class Cuboid {
        static final Pattern commandPtn = Pattern.compile(".*?x=(-?\\d+)\\.\\.(-?\\d+),y=(-?\\d+)\\.\\.(-?\\d+),z=(-?\\d+)\\.\\.(-?\\d+)");

        final int x1, x2, y1, y2, z1, z2;
        final boolean on;

        Cuboid(int x1, int x2, int y1, int y2, int z1, int z2, boolean on) {
            this.x1 = Math.min(x1, x2);
            this.x2 = Math.max(x1, x2);
            this.y1 = Math.min(y1, y2);
            this.y2 = Math.max(y1, y2);
            this.z1 = Math.min(z1, z2);
            this.z2 = Math.max(z1, z2);
            this.on = on;
        }

        Cuboid(String s) {
            var m = commandPtn.matcher(s);
            if (!m.matches()) throw new RuntimeException("invalid cuboid format " + s);
            this.x1 = Integer.parseInt(m.group(1));
            this.x2 = Integer.parseInt(m.group(2));
            this.y1 = Integer.parseInt(m.group(3));
            this.y2 = Integer.parseInt(m.group(4));
            this.z1 = Integer.parseInt(m.group(5));
            this.z2 = Integer.parseInt(m.group(6));
            this.on = s.startsWith("on");
        }

        Cuboid intersection(Cuboid other) {
            return new Cuboid(Math.max(this.x1, other.x1), Math.min(this.x2, other.x2),
                    Math.max(this.y1, other.y1), Math.min(this.y2, other.y2),
                    Math.max(this.z1, other.z1), Math.min(this.z2, other.z2), !other.on);
        }

        boolean intersect(Cuboid other) {
            return Math.min(this.x2, other.x2) >= Math.max(this.x1, other.x1) &&
                    Math.min(this.y2, other.y2) >= Math.max(this.y1, other.y1) &&
                    Math.min(this.z2, other.z2) >= Math.max(this.z1, other.z1);
        }

        long cubes() {
            return ((long) (x2 - x1 + 1)) * (y2 - y1 + 1) * (z2 - z1 + 1);
        }
    }
}
