import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

public class d12 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var edges = new ArrayList<Edge>();
        reader.lines().forEach(line -> edges.add(new Edge(line)));
        var paths = new AtomicInteger();
        var path = new Path() {
            boolean extend(String to) { return !this.contains(to) || to.charAt(0) >= 'A' && to.charAt(0) <= 'Z'; }
        };
        path.add("start");
        walk(paths, path, edges);
        out.println(paths.get());
        paths.set(0);
        var path2 = new Path() {
            boolean extend(String to) {
                if (to.equals("start")) {
                    return false;
                }
                this.add(to);
                var dup = 0;
                for (String cave : this) {
                    if (cave.charAt(0) >= 'a' && cave.charAt(0) <= 'z' && this.stream().filter(c -> c.equals(cave)).count() >= 2) {
                        dup++;
                    }
                }
                this.remove(this.size() - 1);
                return dup <= 2;
            }
        };
        path2.add("start");
        walk(paths, path2, edges);
        out.println(paths.get());
    }

    static void walk(AtomicInteger paths, Path path, List<Edge> edges) {
        var cave = path.get(path.size() - 1);
        if (cave.equals("end")) {
            paths.incrementAndGet();
            return;
        }
        for (var edge : edges) {
            if (edge.from.equals(cave) && path.extend(edge.to)) {
                path.add(edge.to);
                walk(paths, path, edges);
                path.remove(path.size() - 1);
            }
            if (edge.to.equals(cave) && path.extend(edge.from)) {
                path.add(edge.from);
                walk(paths, path, edges);
                path.remove(path.size() - 1);
            }
        }
    }
    static class Edge {
        final String from, to;

        Edge(String s) {
            var pair = s.split("-");
            this.from = pair[0];
            this.to = pair[1];
        }
    }

    abstract static class Path extends ArrayList<String> {
        abstract boolean extend(String to);
    }
}
