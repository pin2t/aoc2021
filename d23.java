import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.stream.Collectors;

//  #############
//  #...........#
//  ###B#D#C#A###
//    #C#D#B#A#
//    #########
public class d23 {
    public static void main(String[] args) {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var lines = reader.lines().collect(Collectors.toList());

    }

    static class State {
        final char[] hallway;
        final char[][] rooms;
        final int energy;

        
    }
}
