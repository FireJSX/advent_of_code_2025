package main.java.aoc;

import java.util.*;

public class Day09 {

    public static long part1(String input) {
        List<String> lines = new ArrayList<>(Util.lines(input));
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }
        if (lines.isEmpty()) return 0;

        int n = lines.size();
        int[] xs = new int[n];
        int[] ys = new int[n];

        for (int i = 0; i < n; i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) {
                n = i;
                break;
            }
            String[] parts = s.split(",");
            if (parts.length != 2) throw new IllegalArgumentException("Bad coordinate: " + s);
            xs[i] = Integer.parseInt(parts[0].trim());
            ys[i] = Integer.parseInt(parts[1].trim());
        }

        long best = 0;
        for (int i = 0; i < n; i++) {
            int x1 = xs[i], y1 = ys[i];
            for (int j = i + 1; j < n; j++) {
                long dx = Math.abs((long) x1 - xs[j]);
                long dy = Math.abs((long) y1 - ys[j]);
                long area = (dx + 1) * (dy + 1);
                if (area > best) best = area;
            }
        }

        return best;
    }

    public static long part2(String input) {
        throw new UnsupportedOperationException("Day09 part2 not implemented yet.");
    }
}
