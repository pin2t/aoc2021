import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import static java.lang.System.out;

public class d16 {
    public static void main(String[] args) throws IOException {
        var builder = new StringBuilder();
        var reader = new BufferedReader(new InputStreamReader(System.in));
        var line = reader.readLine();
        for (int i = 0; i < line.length(); i++) {
            builder.append(String.format("%4s", Integer.toBinaryString(Integer.parseInt(line.substring(i, i + 1), 16))).replace(' ', '0'));
        }
        var packet = new Packet(builder.toString());
        out.println(packet.versions());
        out.println(packet.calc());
    }
    
    static class Packet {
        final String bits;
        final long value;
        final int id, version;
        final List<Packet> subpackets = new ArrayList<>();

        Packet(String input) {
            this.version = Integer.parseInt(input.substring(0, 3), 2);
            this.id = Integer.parseInt(input.substring(3, 6), 2);
            if (this.id == 4) {
                int pos = 6;
                var builder = new StringBuilder();
                while (input.startsWith("1", pos)) {
                    builder.append(input, pos + 1, pos + 5);
                    pos += 5;
                }
                builder.append(input, pos + 1, pos + 5);
                this.bits = input.substring(0, pos + 5);
                this.value = Long.parseLong(builder.toString(), 2);
            } else {
                this.value = 0;
                int pos = 6;
                if (input.startsWith("1", pos)) {
                    pos++;
                    var n = Integer.parseInt(input.substring(pos, pos + 11), 2);
                    pos += 11;
                    for (int i = 0; i < n; i++) {
                        var p = new Packet(input.substring(pos));
                        this.subpackets.add(p);
                        pos += p.len();
                    }
                } else {
                    pos++;
                    var len = Integer.parseInt(input.substring(pos, pos + 15), 2);
                    pos += 15;
                    int pp = 0;
                    String s = input.substring(pos, pos + len);
                    while (pp < s.length()) {
                        var p = new Packet(s.substring(pp));
                        this.subpackets.add(p);
                        pp += p.len();
                    }
                    pos += len;
                }
                this.bits = input.substring(0, pos);
            }
        }

        int len() { return this.bits.length(); }
        int versions() { return version + subpackets.stream().mapToInt(p -> p.versions()).sum(); }

        long calc() {
            switch (this.id) {
                case 0: return subpackets.stream().mapToLong(p -> p.calc()).sum();
                case 1: var vv = 1L; for (var p : subpackets) vv *= p.calc(); return vv;
                case 2: return subpackets.stream().mapToLong(p -> p.calc()).min().getAsLong();
                case 3: return subpackets.stream().mapToLong(p -> p.calc()).max().getAsLong();
                case 4: return value;
                case 5: return subpackets.get(0).calc() > subpackets.get(1).calc() ? 1 : 0;
                case 6: return subpackets.get(0).calc() < subpackets.get(1).calc() ? 1 : 0;
                case 7: return subpackets.get(0).calc() == subpackets.get(1).calc() ? 1 : 0;
            }
            throw new RuntimeException("invalid packet id " + id);
        }
    }
}
