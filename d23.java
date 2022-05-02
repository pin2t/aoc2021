import java.io.BufferedReader;
import java.io.InputStreamReader;
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
    static final List<String> template = Arrays.asList(
                        "#############",
                        "#...........#",
                        "###.#.#.#.###",
                        "  #.#.#.#.#  ",
                        "  #########  ");

    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var lines = reader.lines().collect(Collectors.toList());
        var initial = State.parse(lines);
        out.println(String.join("\n", initial.apply(template)));
    }
}

class Amphipod {
    static int[] units = new int[]{1, 10, 100, 1000};
    final char type;
    final int pos;          // 0,1,3,5,7,9,10 - hallway. 2,4,6,8 - room
    final int depth;        // 0 - hallway, 1,2 - depth in the room

    Amphipod(char t, int p, int d) {
        this.type = t; this.pos = p; this.depth = d;
    }

    int destination() { 
        return (type - 'A') * 2 + 2;
    }

    int energy(int destpos, int destdepth) {
        return units[type - 'A'] * (abs(destpos - pos) + 1 + abs(destdepth - depth));
    }
}

class State {
    final Amphipod[] amphipods;
    final int maxDepth, energy;

    State(Amphipod[] a, int e) {
        this.amphipods = a;
        this.maxDepth = a.length / 2;
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

    List<String> apply(List<String> template) {
        var result = new ArrayList<String>(template);
        for (var a : amphipods) {
            String row = result.get(a.depth + 1);
            result.set(a.depth + 1, row.substring(0, a.pos + 1) + a.type + row.substring(a.pos + 2));
        }            
        return result;
    }

    //
    //  #############
    //  #...........#
    //  ###B#D#C#A###
    //    #C#D#B#A#
    //    #########
    // rooms 2,4,6,8
    List<State> step() {
        var result = new ArrayList<State>();
        for (var i = 0; i < this.amphipods.length; i++) {
            var amphipod = this.amphipods[i];
                // if amphipod in the hallway and can step into its destination - step in
            if (amphipod.pos != 2 && amphipod.pos != 4 && amphipod.pos != 6 && amphipod.pos != 8) {
                var dest = amphipod.destination();
                if (dest != amphipod.pos && 
                    !Arrays.stream(this.amphipods)
                        .anyMatch(aa -> aa.depth == 0 && aa.pos >= amphipod.pos && aa.pos <= dest) &&
                    !Arrays.stream(this.amphipods)
                        .anyMatch(aa -> aa.depth == 0 && aa.pos >= amphipod.pos && aa.pos <= dest)) {
                    
                }
            }

            // if amphipod not in the room of it's destination - step out
            if (amphipod.pos != amphipod.destination()) {
                // step out
            }
        }
        return result;
    }
}
