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
            builder.append(String.format("%4s",
                    Integer.toBinaryString(Integer.parseInt(line.substring(i, i + 1), 16))).replace(' ', '0'));
        }
        var packet = Packet.parse(builder.toString());
        out.println(packet.versions());
        out.println(packet.calc());
    }
    
    static class Packet {
        final long value;
        final int id, version, len;
        final List<Packet> subpackets;

        private Packet(long val, int id, int ver, int l, List<Packet> subs) {
            this.value = val;
            this.id = id;
            this.version = ver;
            this.len = l;
            this.subpackets = subs;
        }

        static Packet parse(String input) {
            int version = Integer.parseInt(input.substring(0, 3), 2);
            int id = Integer.parseInt(input.substring(3, 6), 2);
            if (id == 4) {
                int pos = 6;
                var val = new StringBuilder();
                do {
                    val.append(input, pos + 1, pos + 5);
                    pos += 5;
                } while (input.startsWith("1", pos - 5));
                return new Packet(Long.parseLong(val.toString(), 2), id, version, pos, new ArrayList<>());
            } else {
                int pos = 6;
                var subs = new ArrayList<Packet>();
                if (input.startsWith("1", pos)) {
                    pos++;
                    var n = Integer.parseInt(input.substring(pos, pos + 11), 2);
                    pos += 11;
                    for (int i = 0; i < n; i++) {
                        var p = parse(input.substring(pos));
                        subs.add(p);
                        pos += p.len;
                    }
                } else {
                    pos++;
                    var len = Integer.parseInt(input.substring(pos, pos + 15), 2);
                    pos += 15;
                    int pp = 0;
                    String s = input.substring(pos, pos + len);
                    while (pp < s.length()) {
                        var p = parse(s.substring(pp));
                        subs.add(p);
                        pp += p.len;
                    }
                    pos += len;
                }
                return new Packet(0, id, version, pos, subs);
            }
        }

        int versions() { return version + subpackets.stream().mapToInt(Packet::versions).sum(); }

        long calc() {
            switch (this.id) {
                case 0: return subpackets.stream().mapToLong(Packet::calc).sum();
                case 1: var vv = 1L; for (var p : subpackets) vv *= p.calc(); return vv;
                case 2: return subpackets.stream().mapToLong(Packet::calc).min().orElseThrow();
                case 3: return subpackets.stream().mapToLong(Packet::calc).max().orElseThrow();
                case 4: return value;
                case 5: return subpackets.get(0).calc() > subpackets.get(1).calc() ? 1 : 0;
                case 6: return subpackets.get(0).calc() < subpackets.get(1).calc() ? 1 : 0;
                case 7: return subpackets.get(0).calc() == subpackets.get(1).calc() ? 1 : 0;
            }
            throw new RuntimeException("invalid packet id " + id);
        }
    }
}