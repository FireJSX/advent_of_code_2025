package main.java.aoc;

import java.util.*;

public class Day11 {

    public static long part1(String input) {
        Map<String, List<String>> g = parse(input);

        Map<String, Long> memo = new HashMap<>();
        Set<String> visiting = new HashSet<>();

        return ways("you", g, memo, visiting);
    }

    public static long part2(String input) {
        Map<String, List<String>> g = parse(input);

        Map<State, Long> memo = new HashMap<>();
        Set<State> visiting = new HashSet<>();

        int startMask = 0;
        startMask = applyMask("svr", startMask);

        return waysWithRequirements("svr", startMask, g, memo, visiting);
    }

    private static long ways(String node,
                             Map<String, List<String>> g,
                             Map<String, Long> memo,
                             Set<String> visiting) {

        if ("out".equals(node)) return 1L;

        Long cached = memo.get(node);
        if (cached != null) return cached;

        if (!visiting.add(node)) {
            throw new IllegalStateException("Cycle detected involving node: " + node);
        }

        long sum = 0L;
        for (String nxt : g.getOrDefault(node, List.of())) {
            sum += ways(nxt, g, memo, visiting);
        }

        visiting.remove(node);
        memo.put(node, sum);
        return sum;
    }

    private static long waysWithRequirements(String node,
                                             int mask,
                                             Map<String, List<String>> g,
                                             Map<State, Long> memo,
                                             Set<State> visiting) {

        if ("out".equals(node)) {
            return (mask == 3) ? 1L : 0L;
        }

        State st = new State(node, mask);
        Long cached = memo.get(st);
        if (cached != null) return cached;

        if (!visiting.add(st)) {
            throw new IllegalStateException("Cycle detected involving state: " + node + " mask=" + mask);
        }

        long sum = 0L;
        for (String nxt : g.getOrDefault(node, List.of())) {
            int nextMask = applyMask(nxt, mask);
            sum += waysWithRequirements(nxt, nextMask, g, memo, visiting);
        }

        visiting.remove(st);
        memo.put(st, sum);
        return sum;
    }

    private static int applyMask(String node, int mask) {
        if ("dac".equals(node)) mask |= 1;
        if ("fft".equals(node)) mask |= 2;
        return mask;
    }

    private static Map<String, List<String>> parse(String input) {
        List<String> lines = new ArrayList<>(Util.lines(input));
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        Map<String, List<String>> g = new HashMap<>();

        for (String raw : lines) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            int idx = line.indexOf(':');
            if (idx < 0) throw new IllegalArgumentException("Bad line (missing ':'): " + raw);

            String from = line.substring(0, idx).trim();
            String rest = line.substring(idx + 1).trim();

            List<String> outs = new ArrayList<>();
            if (!rest.isEmpty()) {
                for (String t : rest.split("\\s+")) {
                    if (!t.isEmpty()) outs.add(t);
                }
            }

            g.put(from, outs);
        }

        Set<String> targets = new HashSet<>();
        for (List<String> outs : g.values()) {
            targets.addAll(outs);
        }
        for (String v : targets) {
            g.putIfAbsent(v, List.of());
        }

        return g;
    }

    private record State(String node, int mask) {}
}