import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import static java.lang.Integer.parseInt;
import static java.lang.System.out;

public class d19 {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var scanners = new ArrayList<BeaconsScanner>();
        while (reader.ready()) {
            scanners.add(BeaconsScanner.parse(reader));
        }
        var unique = new HashSet<>(scanners.remove(0).beacons);
        var positions = new Position[scanners.size()];
        var processed = new int[scanners.size()];
        Arrays.fill(processed, 0);
        while (Arrays.stream(processed).filter(p -> p > 0).count() < scanners.size()) {
            for (var s : scanners) {
                if (processed[s.n] > 0) {
                    continue;
                }
                Optional<Position> pos = matchAll(unique, s);
                if (pos.isPresent()) {
                    processed[s.n] = 1;
                    positions[s.n] = pos.get();
                }
            }
            out.println("" + Arrays.stream(processed).filter(p -> p > 0).count() + " " + scanners.size());
        }
        out.println(unique.size());
        var distances = new ArrayList<Integer>();
        for (int i = 0; i < scanners.size(); ++i) {
            for (int j = i+1; j < scanners.size(); ++j) {
                distances.add(positions[i].manhattan(positions[j]));
            }
        }
        out.println(distances.stream().max(Integer::compareTo).get());
    }

    static Optional<Position> matchAll(Set<Position> unique, BeaconsScanner scanner) {
        for (int rot = 0; rot < 4; rot++) {
            for (int dir = 0; dir < 6; dir++) {
                int _rot = rot;
                int _dir = dir;
                var s = scanner.transform(p -> p.rotate(_rot).direct(_dir));
                Optional<Position> pos = match(unique, s);
                if (pos.isPresent()) {
                    return pos;
                }
            }
        }
        return Optional.empty();
    }

    static Optional<Position> match(Set<Position> beacons, BeaconsScanner scanner) {
        for (Position beacon : beacons) {
            for (Position p : scanner.beacons) {
                var mapped = scanner.beacons.stream().map(b -> b.move(beacon.x - p.x, beacon.y - p.y, beacon.z - p.z)).collect(Collectors.toList());
                if (mapped.stream().filter(beacons::contains).count() >= 12) {
                    beacons.addAll(mapped);
                    return Optional.of(new Position(beacon.x - p.x, beacon.y - p.y, beacon.z - p.z));
                }
            }
        }
        return Optional.empty();
    }
}

class BeaconsScanner {
    private static final Pattern num = Pattern.compile("\\d+");
    final int n;
    final List<Position> beacons;

    BeaconsScanner(int n, List<Position> beacons) {
        this.n = n; 
        this.beacons = beacons;
    }

    static BeaconsScanner parse(BufferedReader reader) throws IOException {
        var s = reader.readLine();
        if (s.startsWith("---")) {
            Matcher m = num.matcher(s);
            m.find();
            var n = parseInt(m.group());
            var beacons = new ArrayList<Position>();
            s = reader.readLine();
            while (s != null && !s.isBlank()) {
                beacons.add(Position.parse(s));
                s = reader.readLine();
            }
            return new BeaconsScanner(n, beacons);
        }
        throw new RuntimeException("invalid input \"" + s + "\"");
    }

    BeaconsScanner transform(Function<Position, Position> f) {
        return new BeaconsScanner(this.n, this.beacons.stream().map(f).collect(Collectors.toList()));
    }
}

class Position {
    final int x, y, z;

    Position(int x, int y, int z) {
        this.x = x; this.y = y; this.z = z;
    }

    static Position parse(String s) {
        var triple = s.split(",");
        return new Position(parseInt(triple[0]), parseInt(triple[1]), parseInt(triple[2]));
    }

    Position move(int dx, int dy, int dz) {
        return new Position(this.x + dx, this.y + dy, this.z + dz);
    }

    Position rotate(int times) {
        switch (times) {
            case 0: return this;
            case 1: return new Position(-this.y, this.x, this.z);
            case 2: return new Position(-this.x, -this.y, this.z);
            case 3: return new Position(this.y, -this.x, this.z);
        }
        throw new RuntimeException("incorrect rotation " + times);
    }

    Position direct(int to) {
        switch (to) {
            case 0: return this;
            case 1: return new Position(this.x, -this.y, -this.z);
            case 2: return new Position(this.x, -this.z, this.y);
            case 3: return new Position(-this.y, -this.z, this.x);
            case 4: return new Position(-this.x, -this.z, -this.y);
            case 5: return new Position(this.y, -this.z, -this.x);
        }
        throw new RuntimeException("incorrect direction " + to);
    }

    int manhattan(Position other) {
        return Math.abs(this.x - other.x) + Math.abs(this.y - other.y) + Math.abs(this.z - other.z);
    }

    public boolean equals(Object other) { return this.x == ((Position) other).x && this.y == ((Position) other).y && this.z == ((Position) other).z; }
    public int hashCode() { return Objects.hash(x, y, z); }
}
