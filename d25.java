import static java.lang.System.out;

import java.io.*;
import java.util.*;

public class d25 {
    public static void main(String[] args) throws IOException {
        var reader = new BufferedReader(new InputStreamReader(System.in));
        Set<Pos> west = new HashSet<Pos>(), south = new HashSet<Pos>();
        int wedge = 0, sedge = 0;
        for (int y = 0; reader.ready(); y++, sedge++) {
            var line = reader.readLine();
            wedge = Math.max(wedge, line.length());
            for (int x = 0; x < line.length(); x++) {
                switch (line.charAt(x)) {
                    case '>': west.add(new Pos(x, y)); break;
                    case 'v': south.add(new Pos(x, y)); break;
                }
            }
        }
        int step = 0, moved = 0;        
        do {
            moved = 0; step++;
            Set<Pos> wstep = new HashSet<Pos>(), sstep = new HashSet<Pos>();
            for (var c : west) {
                var cc = new Pos(c.x + 1 >= wedge ? 0 : c.x + 1, c.y); 
                if (west.contains(cc) || south.contains(cc)) 
                    wstep.add(c); 
                else { 
                    wstep.add(cc); 
                    moved++; 
                }
            }
            for (var c : south) {
                var cc = new Pos(c.x, c.y + 1 >= sedge ? 0 : c.y + 1); 
                if (wstep.contains(cc) || south.contains(cc)) 
                    sstep.add(c); 
                else { 
                    sstep.add(cc); 
                    moved++; 
                }
            }
            west = wstep; south = sstep;
        } while (moved > 0);
        out.println(step);
    }   
}

class Pos {
    final int x, y;

    Pos(int x, int y) {
        this.x = x; this.y = y;
    }

    public boolean equals(Object o) { 
        return this.x == ((Pos)o).x && this.y == ((Pos)o).y; 
    }
    public int hashCode() { 
        return Objects.hash(this.x, this.y); 
    }
}
