import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.System.out;

//
//  #############
//  #...........#
//  ###B#D#C#A###
//    #C#D#B#A#
//    #########
public class d23 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var lines = reader.lines().collect(Collectors.toList());
        var initial = State.parse(lines);
        out.println("Initial state");
        initial.print(System.out);
        var finished = State.parse(Arrays.asList(
                  "#############",
                  "#...........#",
                  "###A#B#C#D###",
                  "  #A#B#C#D#  ",
                  "  #########  "
        ));
        out.println("Sample finished state " + finished.finished());
        finished.print(out);
        var queue = new PriorityQueue<State>(Comparator.comparingInt(s -> s.energy));
        queue.add(initial);
        var processed = new HashSet<State>();
        while (!queue.isEmpty()) {
            var state = queue.poll();
            out.print("\rcurrent energy " + state.energy + " queue " + queue.size());
            if (state.finished()) {
                out.println("finished state energy " + state.energy);
                state.print(System.out);
                break;
            }
            if (!processed.add(state)) {
                continue;
            }
            queue.addAll(state.step());
        }
    }

    static class Amphipod {
        static int[] units = new int[]{1, 10, 100, 1000};
        static Map<Amphipod, Amphipod> cache = new HashMap<>();
        final char type;
        final int pos;          // 0,1,3,5,7,9,10 - hallway. 2,4,6,8 - room
        final int depth;        // 0 - hallway, 1,2 - depth in the room

        Amphipod(char t, int p, int d) {
            this.type = t; this.pos = p; this.depth = d;
        }

        static Amphipod valueOf(char t, int p, int d) {
            Amphipod a = new Amphipod(t, p, d);
            Amphipod cached = cache.get(a);
            if (cached != null) {
                return cached;
            }
            cache.put(a, a);
            return a;
        }

        int destination() {
            return (type - 'A') * 2 + 2;
        }

        // an energy required to move to specific position and depth
        int energy(int pos, int depth) {
            return units[type - 'A'] * (abs(pos - this.pos) + 1 + abs(depth - this.depth));
        }

        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof Amphipod)) return false;
            Amphipod o = (Amphipod) other;
            return o.type == this.type && o.pos == this.pos && o.depth == this.depth;
        }

        public int hashCode() {
            return Objects.hash(type, pos, depth);
        }
    }

    static class State {
        static final List<String> template = Arrays.asList(
                "#############",
                "#...........#",
                "###.#.#.#.###",
                "  #.#.#.#.#  ",
                "  #########  ");
        static final int[] hallwayDests = new int[]{0,1,3,5,7,9,10};
        final Amphipod[] amphipods;
        final int maxDepth, energy;

        private State(Amphipod[] a, int e) {
            this.amphipods = a;
            this.maxDepth = a.length / 4;
            this.energy = e;
        }

        static State parse(List<String> lines) {
            var a = new Amphipod[(lines.size() - 3) * 4];
            for (int r = 2; r < lines.size() - 1; r++) {
                for (int i = 3; i < 10; i+=2) {
                    a[(r - 2) * 4 + (i - 3) / 2] = new Amphipod(lines.get(r).charAt(i), i - 1, r - 1);
                }
            }
            return new State(a, 0);
        }

        void print(PrintStream output) {
            var render = new ArrayList<>(template);
            for (var a : amphipods) {
                String row = render.get(a.depth + 1);
                render.set(a.depth + 1, row.substring(0, a.pos + 1) + a.type + row.substring(a.pos + 2));
            }
            for (String line  : render) {
                output.println(line);
            }
        }

        State move(Amphipod a, int dest) {
            for (int i = 0; i < this.amphipods.length; i++) {
                if (this.amphipods[i].equals(a)) {
                    var moved = Arrays.copyOf(this.amphipods, this.amphipods.length);
                    var energy = this.energy;
                    if (dest == 2 || dest == 4 || dest == 6 || dest == 8) {
                        var depth = Arrays.stream(this.amphipods).filter(it -> it.pos == dest).mapToInt(it -> it.depth).min().orElse(this.maxDepth + 1) - 1;
                        energy += a.energy(dest, depth);
                        moved[i] = Amphipod.valueOf(a.type, dest, depth);
//                        moved[i] = new Amphipod(a.type, dest, depth);
                    } else {
                        energy += a.energy(dest, 0);
                        moved[i] = Amphipod.valueOf(a.type, dest, 0);
//                        moved[i] = new Amphipod(a.type, dest, 0);
                    }
                    return new State(moved, energy);
                }
            }
            return this;
        }

        //
        //  #############
        //  #...........#
        //  ###B#D#C#A###
        //    #C#D#B#A#
        //    #########
        // rooms 2,4,6,8
        List<State> step() {
            var result = new ArrayList<State>(1000);
            for (var i = 0; i < this.amphipods.length; i++) {
                var amphipod = this.amphipods[i];
                // if amphipod in the hallway and can step into its destination - step in
                if (amphipod.pos != 2 && amphipod.pos != 4 && amphipod.pos != 6 && amphipod.pos != 8) {
                    var dest = amphipod.destination();
                    if (!Arrays.stream(this.amphipods).anyMatch(aa -> {
                                if (dest > amphipod.pos) {
                                    return aa.depth == 0 && aa.pos > amphipod.pos && aa.pos <= dest;
                                } else {
                                    return aa.depth == 0 && aa.pos >= dest && aa.pos < amphipod.pos;
                                }
                            }) &&
                        !Arrays.stream(this.amphipods).anyMatch(aa -> aa.pos == dest && aa.destination() != dest)) {
                        result.add(this.move(amphipod, dest));
                    }
                } else if (amphipod.pos != amphipod.destination() ||
                        Arrays.stream(this.amphipods).anyMatch(it -> it.pos == amphipod.pos && it.destination() != amphipod.destination())) {
                    // if amphipod not in the room of it's destination or there are othe amphipods in the room - step out
                    for (var dest : hallwayDests) {
                        // conditions:
                        // 1. no other amphipods between amphipod.pos and the dest in hallway
                        // 2. no other amphipods in the amphipod's room with lower depth
                        if (!Arrays.stream(this.amphipods).anyMatch(it -> {
                                    if (dest > amphipod.pos) {
                                        return it.pos > amphipod.pos && it.pos <= dest && it.depth == 0;
                                    } else {
                                        return it.pos >= dest && it.depth == 0 && it.pos < amphipod.pos;
                                    }
                                }) &&
                            !Arrays.stream(this.amphipods).anyMatch(it -> it.pos == amphipod.pos && it.depth < amphipod.depth)) {
                            result.add(this.move(amphipod, dest));
                        }
                    }
                }
            }
            return result;
        }

        boolean finished() {
            return Arrays.stream(this.amphipods).allMatch(it -> it.pos == it.destination());
        }

        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof State)) return false;
            State s = (State) other;
            return s.energy == this.energy && Arrays.equals(this.amphipods, s.amphipods, (a1, a2) -> a1.equals(a2) ? 0 : 1);
        }

        public int hashCode() {
            return Objects.hash(this.amphipods, this.energy);
        }
    }
}

