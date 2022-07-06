import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.util.*;
import java.util.function.Supplier;
import java.util.stream.Collectors;

import static java.lang.Math.abs;
import static java.lang.System.in;
import static java.lang.System.out;

public class d23 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(in));
        var lines = reader.lines().toList();
        solve(lines);
        lines = new ArrayList<>(lines);
        lines.add(3, "  #D#C#B#A#  ");
        lines.add(4, "  #D#B#A#C#  ");
        solve(lines);
    }

    static void solve(List<String> rooms) {
        var initial = State.parse(rooms);
        var queue = new PriorityQueue<State>(Comparator.comparingInt(s -> s.energy));
        queue.add(initial);
        var processed = new HashSet<State>();
        while (!queue.isEmpty()) {
            var state = queue.poll();
            if (state.isFinal()) { out.println(state.energy); break; }
            if (!processed.add(state))
                continue;
            for (State s : state.moves())
                if (!processed.contains(s))
                    queue.add(s);
        }
    }

    static class State {
        record Amphipod(char type, int pos, int depth) {
            static int[] units = new int[] {1, 10, 100, 1000};

            int destination() {
                return (type - 'A') * 2 + 2;
            }

            // energy used to move amphipod a to dest position and to depth if it is in the room
            int energy(int dest, int depth) {
                return units[type - 'A'] * (abs(dest - pos) + abs(this.depth - depth));
            }
        };

        static final int[] hallwayDests = new int[]{0,1,3,5,7,9,10};
        final List<Amphipod> amphipods;
        final int maxDepth, energy;

        private State(List<Amphipod> a, int e) {
            this.amphipods = a;
            this.maxDepth = a.size() / 4;
            this.energy = e;
            Supplier<String> stateString = () -> a.stream().map(it -> "" + it.pos() + " " + it.depth).collect(Collectors.joining(" "));
            for (Amphipod it : a) {
                if (it.pos() > 10)
                    throw new RuntimeException("invalid state " + stateString.get());
                if ((it.pos() == 2 || it.pos() == 4 || it.pos() == 6 || it.pos() == 8) && (it.depth() < 1 || it.depth() > this.maxDepth))
                    throw new RuntimeException("invalid depth " + it.depth() + " should be between 1 and " + this.maxDepth + " state " + stateString.get());
                if (it.depth() > this.maxDepth)
                    throw new RuntimeException("invalid depth " + it.depth() + " should be between 0 and " + this.maxDepth + " state " + stateString.get());
            }
        }

        static State parse(List<String> lines) {
            var amphipods = new ArrayList<Amphipod>();
            for (int r = 2; r < lines.size() - 1; r++)
                for (int i = 3; i < 10; i += 2)
                    amphipods.add(new Amphipod(lines.get(r).charAt(i), i - 1, r - 1));
            return new State(amphipods, 0);
        }

        State move(Amphipod a, int dest) {
            var moved = new ArrayList<>(amphipods);
            var energy = this.energy;
            var destDepth = 0;
            if (dest == 2 || dest == 4 || dest == 6 || dest == 8) {
                destDepth = this.maxDepth;
                for (Amphipod it : amphipods)
                    if (it.pos() == dest && it.depth() <= destDepth)
                        destDepth = it.depth() - 1;
            }
            energy += a.energy(dest, destDepth);
            for (int i = 0; i < moved.size(); i++)
                if (moved.get(i).equals(a))
                    moved.set(i, new Amphipod(a.type(), dest, destDepth));
            return new State(moved, energy);
        }

        // return the list of states for all possible amphipods moves from this position
        List<State> moves() {
            var result = new ArrayList<State>(100);
            for (Amphipod a : amphipods)  {
                // if amphipod in the hallway and can step into its destination - step in
                if (a.pos() != 2 && a.pos() != 4 && a.pos() != 6 && a.pos() != 8) {
                    if (!amphipods.stream().anyMatch(aa -> {
                                if (a.destination() > a.pos()) {
                                    return aa.depth() == 0 && aa.pos() > a.pos() && aa.pos() <= a.destination();
                                } else {
                                    return aa.depth() == 0 && aa.pos() >= a.destination() && aa.pos() < a.pos();
                                }
                            }) &&
                        !amphipods.stream().anyMatch(aa -> aa.pos() == a.destination() && ((aa.type() - 'A') * 2 + 2) != a.destination())) {
                        result.add(this.move(a, a.destination()));
                    }
                } else if (a.pos() != a.destination() ||
                        amphipods.stream().anyMatch(aa -> aa.pos() == a.pos() && ((aa.type() - 'A') * 2 + 2) != a.destination())) {
                    // if amphipod not in the room of it's destination or there are othe amphipods in the room - step out
                    for (var dest : hallwayDests) {
                        // conditions:
                        // 1. no other amphipods between amphipod.pos and the dest in hallway
                        // 2. no other amphipods in the amphipod's room with lower depth
                        if (!amphipods.stream().anyMatch(aa -> {
                                    if (dest > a.pos()) {
                                        return aa.pos() > a.pos() && aa.pos() <= dest && aa.depth() == 0;
                                    } else {
                                        return aa.pos() >= dest && aa.depth() == 0 && aa.pos() < a.pos();
                                    }
                                }) &&
                            !amphipods.stream().anyMatch(aa -> aa.pos() == a.pos() && aa.depth() < a.depth())) {
                            result.add(this.move(a, dest));
                        }
                    }
                }
            }
            return result;
        }

        boolean isFinal() {
            return !amphipods.stream().anyMatch(it -> it.pos() != ((it.type() - 'A') * 2 + 2));
        }

        public boolean equals(Object other) {
            if (other == this) return true;
            if (!(other instanceof State)) return false;
            return amphipods.equals(((State) other).amphipods);
        }

        public int hashCode() {
            return this.amphipods.hashCode();
        }
    }
}