package main.java.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 {

    private static final Pattern LINE_P = Pattern.compile("^\\s*\\[([^\\]]+)]\\s*(.*)$");
    private static final Pattern BTN_P  = Pattern.compile("\\(([^)]*)\\)");
    private static final Pattern CURLY_P = Pattern.compile("\\{.*}$");
    public static long part1(String input) {
        List<String> lines = new ArrayList<>(Util.lines(input));
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) lines.remove(lines.size() - 1);

        long sum = 0;
        for (String line : lines) {
            line = line.trim();
            if (line.isEmpty()) continue;
            sum += solveOneMachineMinPresses(line);
        }
        return sum;
    }

    private static int solveOneMachineMinPresses(String line) {
        line = CURLY_P.matcher(line).replaceFirst("").trim();

        Matcher m = LINE_P.matcher(line);
        if (!m.matches()) {
            throw new IllegalArgumentException("Bad machine line (missing [..]): " + line);
        }

        String diagram = m.group(1).trim();
        String rest = m.group(2);

        int n = diagram.length();
        int target = 0;
        for (int i = 0; i < n; i++) {
            char c = diagram.charAt(i);
            if (c == '#') target |= (1 << i);
            else if (c != '.') throw new IllegalArgumentException("Bad char in diagram: " + c);
        }

        List<Integer> buttons = new ArrayList<>();
        Matcher bm = BTN_P.matcher(rest);
        while (bm.find()) {
            String inside = bm.group(1).trim();
            int mask = 0;
            if (!inside.isEmpty()) {
                String[] parts = inside.split(",");
                for (String p : parts) {
                    p = p.trim();
                    if (p.isEmpty()) continue;
                    int idx = Integer.parseInt(p);
                    if (idx < 0 || idx >= n) {
                        throw new IllegalArgumentException("Button index out of range: " + idx + " for n=" + n);
                    }
                    mask ^= (1 << idx);
                }
            }
            buttons.add(mask);
        }

        int states = 1 << n;
        int[] dist = new int[states];
        Arrays.fill(dist, -1);

        ArrayDeque<Integer> q = new ArrayDeque<>();
        dist[0] = 0;
        q.add(0);

        while (!q.isEmpty()) {
            int cur = q.poll();
            if (cur == target) return dist[cur];

            int d = dist[cur];
            for (int mask : buttons) {
                int nxt = cur ^ mask;
                if (dist[nxt] == -1) {
                    dist[nxt] = d + 1;
                    q.add(nxt);
                }
            }
        }
        throw new IllegalStateException("No solution for machine line: " + line);
    }
}
