import java.util.*;

import static java.lang.System.out;

public class d21 {
    public static void main(String[] args) {
        var scanner = new Scanner(System.in);
        var pos1 = scanner.readInts()[1];
        var pos2 = scanner.readInts()[1];
        var positions = new int[] {pos1, pos2};
        var scores = new int[] {0, 0};
        var dice = new Dice(positions);
        for (var player = 0; scores[player % 2] < 1000 && scores[(player + 1) % 2] < 1000; player++) {
            dice.roll(player % 2); dice.roll(player % 2); dice.roll(player % 2);
            scores[player % 2] += positions[player % 2];
        }
        if (scores[0] >= 1000) {
            out.println(scores[1] * dice.rolls);
        } else {
            out.println(scores[0] * dice.rolls);
        }
        out.println(universes(new int[]{pos1, pos2}));
    }

    static long universes(int[] positions) {
        var tuples = new HashMap<Tuple, Long>();
        tuples.put(new Tuple(positions[0], positions[1], 0, 0, 1), 1L);
        var wins = new long[] {0, 0};
        var splits = new int[] {1+1+1, 1+1+2, 1+1+3, 1+2+1, 1+2+2, 1+2+3, 1+3+1, 1+3+2, 1+3+3,
                                2+1+1, 2+1+2, 2+1+3, 2+2+1, 2+2+2, 2+2+3, 2+3+1, 2+3+2, 2+3+3,
                                3+1+1, 3+1+2, 3+1+3, 3+2+1, 3+2+2, 3+2+3, 3+3+1, 3+3+2, 3+3+3};
        while (!tuples.isEmpty()) {
            var e = tuples.entrySet().stream().findFirst().orElseThrow();
            var tuple = e.getKey();
            tuples.remove(tuple);
            for (var split : splits) {
                var player = tuple.player();
                var pos = ((player == 2 ? tuple.pos2() : tuple.pos1()) + split - 1) % 10 + 1;
                if ((player == 2 ? tuple.score2() : tuple.score1()) + pos >= 21) {
                    wins[player - 1] += e.getValue();
                } else {
                    var t = player == 1 ? 
                        new Tuple(pos, tuple.pos2(), tuple.score1() + pos, tuple.score2(), 2) :
                        new Tuple(tuple.pos1(), pos, tuple.score1(), tuple.score2() + pos, 1);
                    tuples.merge(t, e.getValue(), Long::sum);
                }
            }
        }
        return Math.max(wins[0], wins[1]);
    }

    record Tuple (int pos1, int pos2, int score1, int score2, int player) {}

    static class Dice  {
        final int[] positions;
        int rolls = 0;
        int d = 1;

        Dice(int[] positions) {
            this.positions = positions;
        }

        void roll(int player) {
            positions[player] += d++;
            if (d > 100) { d -= 100; }
            positions[player] = (positions[player] - 1) % 10 + 1;
            rolls++;
        }
    }
}
