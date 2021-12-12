import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static java.lang.System.out;

public class d12 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var edges = new ArrayList<Edge>();
        reader.lines().forEach(line -> edges.add(new Edge(line)));
        var paths = new HashSet<List<String>>();
        var path = new ArrayList<String>();
        path.add("start");
        walk(paths, path, edges, (_path, to) -> (!_path.contains(to) || to.charAt(0) >= 'A' && to.charAt(0) <= 'Z'));
        out.println(paths.size());
        paths = new HashSet<List<String>>();
        path = new ArrayList<String>();
        path.add("start");
        walk(paths, path, edges, (_path, to) -> {
            if (to.equals("start")) {
                return false;
            }
            _path.add(to);
            var dup = 0;
            for (String cave : _path) {
                if (cave.charAt(0) >= 'a' && cave.charAt(0) <= 'z' && _path.stream().filter(c -> c.equals(cave)).count() >= 2) {
                    dup++;
                }
            }
            _path.remove(_path.size() - 1);
            return dup <= 2;
        });
        out.println(paths.size());
    }

    static void walk(Set<List<String>> paths, List<String> path, List<Edge> edges, StepFunc can) {
        var cave = path.get(path.size() - 1);
        if (cave.equals("end")) {
            paths.add(new ArrayList<>(path));
            return;
        }
        for (var edge : edges) {
            if (edge.from.equals(cave) && can.canStep(path, edge.to)) {
                path.add(edge.to);
                walk(paths, path, edges, can);
                path.remove(path.size() - 1);
            }
            if (edge.to.equals(cave) && can.canStep(path, edge.from)) {
                path.add(edge.from);
                walk(paths, path, edges, can);
                path.remove(path.size() - 1);
            }
        }
    }

    static class Edge {
        final String from, to;

        Edge(String s) {
            var pair = s.split("\\-");
            this.from = pair[0];
            this.to = pair[1];
        }
    }

    interface StepFunc {
        boolean canStep(List<String> path, String to);
    }
}
