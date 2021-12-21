import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

import static java.lang.System.out;

public class d21 {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var pos1 = scanner.readInts()[1];
        var pos2 = scanner.readInts()[1];
        var positions = new int[] {pos1, pos2};
        var scores = new int[] {0, 0};
        var d = new AtomicInteger(1);
        var rolls = new AtomicInteger(0);
        Dice dice = (i) -> {
            positions[i] += d.getAndIncrement(); if (d.get() > 100) d.addAndGet(-100);
            positions[i] = (positions[i] - 1) % 10 + 1;
            rolls.incrementAndGet();
        };
        while (true) {
            dice.roll(0); dice.roll(0); dice.roll(0); scores[0] += positions[0];
            if (scores[0] >= 1000) break;
            dice.roll(1); dice.roll(1); dice.roll(1); scores[1] += positions[1];
            if (scores[1] >= 1000) break;
        }
        if (scores[0] >= 1000) {
            out.println(scores[1] * rolls.get());
        } else {
            out.println(scores[0] * rolls.get());
        }
        out.println(universes(new int[]{pos1, pos2}));
    }

    static long universes(int[] positions) {
        var states = new HashMap<Tuple, Long>();
        // tuple [pos1, pos2, score1, score2, player]
        states.put(new Tuple(positions[0], positions[1], 0, 0, 1), 1L);
        var wins = new long[] {0, 0};
        var splits = new int[] {1+1+1, 1+1+2, 1+1+3, 1+2+1, 1+2+2, 1+2+3, 1+3+1, 1+3+2, 1+3+3,
                                2+1+1, 2+1+2, 2+1+3, 2+2+1, 2+2+2, 2+2+3, 2+3+1, 2+3+2, 2+3+3,
                                3+1+1, 3+1+2, 3+1+3, 3+2+1, 3+2+2, 3+2+3, 3+3+1, 3+3+2, 3+3+3};
        while (!states.isEmpty()) {
            var e = states.entrySet().stream().findFirst().orElseThrow();
            var state = e.getKey();
            states.remove(state);
            for (var split : splits) {
                var player = state.values[4];
                var pos = (state.values[player - 1] + split - 1) % 10 + 1;
                if (state.values[2 + player - 1] + pos >= 21) {
                    wins[player - 1] += e.getValue();
                } else {
                    var t = player == 1 ? new Tuple(pos, state.values[1], state.values[2] + pos, state.values[3], 2):
                                        new Tuple(state.values[0], pos, state.values[2], state.values[3] + pos, 1);
                    states.merge(t, e.getValue(), Long::sum);
                }
            }
        }
        return Math.max(wins[0], wins[1]);
    }

    interface Dice {
        void roll(int player);
    }

    static class Tuple {
        final int[] values;

        Tuple(int... values) {
            this.values = values;
        }

        public boolean equals(Object o) { return Arrays.equals(this.values, ((Tuple) o).values); }
        public int hashCode() { return Arrays.hashCode(this.values); }
    }
}
