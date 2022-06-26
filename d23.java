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
            if (!processed.add(state))
                continue;
            for (State s : state.step())
                if (!processed.contains(s))
                    queue.add(s);
        }
    }

    static class State {
        static int[] units = new int[]{1, 10, 100, 1000};
        static final List<String> template = Arrays.asList(
                "#############",
                "#...........#",
                "###.#.#.#.###",
                "  #.#.#.#.#  ",
                "  #########  ");
        static final int[] hallwayDests = new int[]{0,1,3,5,7,9,10};
        // flatten amphipods storage a1.pos, a1.depth, a2.pos, a2.depth, b1.pos ...
        final int[] amphipods;
        final int maxDepth, energy;

        private State(int[] a, int e) {
//            out.println("state " + Arrays.stream(a).mapToObj(Integer::toString).collect(Collectors.joining(" ")) + " energy " + e);
            this.amphipods = a;
            this.maxDepth = a.length / 4;
            this.energy = e;
        }

        static State parse(List<String> lines) {
            var amphipods = new int[(lines.size() - 3) * 4 * 2];
            var indicies = new int[]{0, 2, 4, 6};
            for (int r = 2; r < lines.size() - 1; r++) {
                for (int i = 3; i < 10; i += 2) {
                    var type = lines.get(r).charAt(i) - 'A';
                    amphipods[indicies[type] * 2] = i - 1;
                    amphipods[indicies[type] * 2 + 1] = r - 1;
                    indicies[type]++;
                }
            }
            return new State(amphipods, 0);
        }

        void print(PrintStream output) {
            var render = new ArrayList<>(template);
            this.forEach((type, pos, depth) -> {
                String row = render.get(depth + 1);
                render.set(depth + 1, row.substring(0, pos + 1) + type + row.substring(pos + 2));
            });
            for (String line  : render)
                output.println(line);
        }

        int count() {
            return this.amphipods.length / 2;
        }

        char type(int i) {
            return (char) ('A' + i / (this.amphipods.length / 8));
        }

        int depth(int i) {
            return this.amphipods[i * 2 + 1];
        }

        int pos(int i) {
            return this.amphipods[i * 2];
        }

        int destination(int i) {
            return (this.type(i) - 'A') * 2 + 2;
        }

        void forEach(AmphipodConsumer consumer) {
            for (var i = 0; i < count(); i++)
                consumer.consume(this.type(i), this.pos(i), this.depth(i));
        }

        boolean anyMatch(AmphipodPredicate predicate) {
            for (var i = 0; i < count(); i++)
                if (predicate.test(this.type(i), this.pos(i), this.depth(i)))
                    return true;
            return false;
        }

        State move(int a, int dest) {
            var moved = Arrays.copyOf(this.amphipods, this.amphipods.length);
            var energy = this.energy;
            if (dest == 2 || dest == 4 || dest == 6 || dest == 8) {
                var destDepth = new int[]{this.maxDepth + 1};
                this.forEach((type, pos, depth) -> {
                    if (pos == dest && depth < destDepth[0])
                        destDepth[0] = depth;
                });
                energy += units[this.type(a) - 'A'] * (abs(dest - this.pos(a)) + 1 + abs(destDepth[0] - this.depth(a)));
                moved[a * 2] = dest;
                moved[a * 2 + 1] = destDepth[0];
            } else {
                energy += units[this.type(a) - 'A'] * (abs(dest - this.pos(a)) + 1 + this.depth(a));
                moved[a * 2] = dest;
                moved[a * 2 + 1] = 0;
            }
            return new State(moved, energy);
        }

        //
        //  #############
        //  #...........#
        //  ###B#D#C#A###
        //    #C#D#B#A#
        //    #########
        // rooms 2,4,6,8
        List<State> step() {
            var result = new ArrayList<State>(100);
            for (var i = 0; i < this.count(); i++) {
                var ii = i;
                // if amphipod in the hallway and can step into its destination - step in
                if (this.pos(i) != 2 && this.pos(i) != 4 && this.pos(i) != 6 && this.pos(i) != 8) {
                    if (!this.anyMatch((type, pos, depth) -> {
                                if (this.destination(ii) > this.pos(ii)) {
                                    return depth == 0 && pos > this.pos(ii) && pos <= this.destination(ii);
                                } else {
                                    return depth == 0 && pos >= this.destination(ii) && pos < this.pos(ii);
                                }
                            }) &&
                        !this.anyMatch((type, pos, depth) -> pos == this.destination(ii) && ((type - 'A') * 2 + 2) != this.destination(ii))) {
                        result.add(this.move(i, this.destination(i)));
                    }
                } else if (this.pos(i) != this.destination(i) ||
                        this.anyMatch((type, pos, depth) -> pos == this.pos(ii) && ((type - 'A') * 2 + 2) != this.destination(ii))) {
                    // if amphipod not in the room of it's destination or there are othe amphipods in the room - step out
                    for (var dest : hallwayDests) {
                        // conditions:
                        // 1. no other amphipods between amphipod.pos and the dest in hallway
                        // 2. no other amphipods in the amphipod's room with lower depth
                        if (!this.anyMatch((type, pos, depth) -> {
                                    if (dest > this.pos(ii)) {
                                        return pos > this.pos(ii) && pos <= dest && depth == 0;
                                    } else {
                                        return pos >= dest && depth == 0 && pos < this.pos(ii);
                                    }
                                }) &&
                            !this.anyMatch((type, pos, depth) -> pos == this.pos(ii) && depth < this.depth(ii))) {
                            result.add(this.move(i, dest));
                        }
                    }
                }
            }
            return result;
        }

        boolean finished() {
            return !this.anyMatch((type, pos, depth) -> pos != ((type - 'A') * 2 + 2));
        }

        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof State)) return false;
            State s = (State) other;
            return Arrays.equals(this.amphipods, s.amphipods);
        }

        public int hashCode() {
            return Objects.hash(this.amphipods);
        }
    }

    interface AmphipodConsumer {
        void consume(char type, int pos, int depth);
    }

    interface AmphipodPredicate {
        boolean test(char type, int pos, int depth);
    }
}

