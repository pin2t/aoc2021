import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.*;
import java.util.stream.Collectors;

import static java.lang.System.*;

public class d24 {
    public static void main(String[] args) {
        var instructions = new ArrayList<Instruction>();
        var reader = new BufferedReader(new InputStreamReader(in));
        reader.lines().forEach(line -> { if (!line.isBlank()) instructions.add(Instruction.parse(line)); });
        var programs = new ArrayList<List<Instruction>>();
        for (var i : instructions) {
            if ("inp".equals(i.op()))
                programs.add(new ArrayList<>());
            else
                programs.get(programs.size() - 1).add(i);
        }
        var seen = new ArrayList<Set<Integer>>();
        for (int i = 0; i < programs.size(); i++) seen.add(new HashSet<>());
        out.println(inputsToZero(programs, new int[] {9, 8, 7, 6, 5, 4, 3, 2, 1}, new Stack<Integer>(), seen, 0)
                .stream().map(Object::toString).collect(Collectors.joining()));
        for (int i = 0; i < programs.size(); i++) seen.get(i).clear();
        out.println(inputsToZero(programs, new int[] {1, 2, 3, 4, 5, 6, 7, 8, 9}, new Stack<Integer>(), seen, 0)
                .stream().map(Object::toString).collect(Collectors.joining()));
    }

    static int run(List<Instruction> program, int w, int z) {
        var data = new HashMap<String, Integer>();
        data.put("w", w);
        data.put("z", z);
        data.put("x", 0);
        data.put("y", 0);
        for (var i : program) {
            var b = data.containsKey(i.arg2()) ? data.get(i.arg2()) : Integer.parseInt(i.arg2());
            switch (i.op()) {
                case "add": data.put(i.arg1(), data.get(i.arg1()) + b); break;
                case "mul": data.put(i.arg1(), data.get(i.arg1()) * b); break;
                case "div": data.put(i.arg1(), data.get(i.arg1()) / b); break;
                case "mod": data.put(i.arg1(), data.get(i.arg1()) % b); break;
                case "eql": data.put(i.arg1(), data.get(i.arg1()).equals(b) ? 1 : 0); break;
            }
        }
        return data.get("z");
    }

    static List<Integer> inputsToZero(List<List<Instruction>> programs, int[] inputs, Stack<Integer> stack, List<Set<Integer>> seen, int z) {
        var i = stack.size();
        if (i >= programs.size())
            return z == 0 ? (List<Integer>) stack.clone() : Collections.emptyList();
        if (seen.get(i).contains(z)) return Collections.emptyList();
        seen.get(i).add(z);
        for (var w : inputs) {
            var zz = run(programs.get(i), w, z);
            stack.push(w);
            var result = inputsToZero(programs, inputs, stack, seen, zz);
            stack.pop();
            if (!result.isEmpty())
                return result;
        }
        return Collections.emptyList();
    }

    record Instruction (String op, String arg1, String arg2) {
        static Instruction parse(String line) {
            var triple = line.split("\\s");
            return new Instruction(triple[0], triple[1], triple.length > 2 ? triple[2] : "");
        }
    }
}
