import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.regex.Pattern;

import static java.lang.System.out;

public class d22 {
    private static Pattern commandPtn = Pattern.compile(".*?x=(-?\\d+)\\.\\.(-?\\d+)\\,y=(-?\\d+)\\.\\.(-?\\d+)\\,z=(-?\\d+)\\.\\.(-?\\d+)");

    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var on = new HashSet<Cube>();
        var cuboids = new ArrayList<Cuboid>();
        while (reader.ready()) {
            var line = reader.readLine();
            out.println(line);
            var m = commandPtn.matcher(line);
            m.matches();
            int x1 = Integer.parseInt(m.group(1)), x2 = Integer.parseInt(m.group(2));
            int y1 = Integer.parseInt(m.group(3)), y2 = Integer.parseInt(m.group(4));
            int z1 = Integer.parseInt(m.group(5)), z2 = Integer.parseInt(m.group(6));
            for (int x = Math.max(Math.min(x1, x2), -50); x <= Math.min(Math.max(x1, x2), 50); x++) {
                for (int y = Math.max(Math.min(y1, y2), -50); y <= Math.min(Math.max(y1, y2), 50); y++) {
                    for (int z = Math.max(Math.min(z1, z2), -50); z <= Math.min(Math.max(z1, z2), 50); z++) {
                        if (x >= -50 && x <= 50 && y >= -50 && y <= 50 && z >= -50 && z <= 50) {
                            if (line.startsWith("on"))
                                on.add(new Cube(x, y, z));
                            else
                                on.remove(new Cube(x, y, z));
                        }
                    }
                }
            }
            var c = Cuboid.parse(line);
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
        out.println(cuboids.size());
        out.println(on.size());
        out.println(cuboids.stream().mapToLong(c -> c.on ? c.cubes() : -c.cubes()).sum());
    }

    static class Cube {
        final int x, y, z;

        Cube(int x, int y, int z) {
            this.x = x; this.y = y; this.z = z;
        }

        public boolean equals(Object other) { return this.x == ((Cube) other).x && this.y == ((Cube) other).y && this.z == ((Cube) other).z; }
        public int hashCode() { return Objects.hash(x, y, z); }
    }

    static class Cuboid {
        final int x1, x2, y1, y2, z1, z2;
        final boolean on;

        Cuboid(int x1, int x2, int y1, int y2, int z1, int z2, boolean on) {
            this.x1 = Math.min(x1, x2); this.x2 = Math.max(x1, x2);
            this.y1 = Math.min(y1, y2); this.y2 = Math.max(y1, y2);
            this.z1 = Math.min(z1, z2); this.z2 = Math.max(z1, z2);
            this.on = on;
        }

        static Cuboid parse(String s) {
            var m = commandPtn.matcher(s);
            m.matches();
            return new Cuboid(Integer.parseInt(m.group(1)), Integer.parseInt(m.group(2)),
                               Integer.parseInt(m.group(3)), Integer.parseInt(m.group(4)),
                               Integer.parseInt(m.group(5)), Integer.parseInt(m.group(6)), s.startsWith("on"));
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

        public boolean equals(Object other) { return this.x1 == ((Cuboid) other).x1 && this.x2 == ((Cuboid) other).x2 &&
                        this.y1 == ((Cuboid) other).y1 && this.y2 == ((Cuboid) other).y2 &&
                        this.z1 == ((Cuboid) other).z1 && this.z2 == ((Cuboid) other).z2; }
        public int hashCode() { return Objects.hash(x1, x2, y1, y2, z1, z2, on); }
    }
}
