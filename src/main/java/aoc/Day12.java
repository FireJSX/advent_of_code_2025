package main.java.aoc;

import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day12 {

    private static final Pattern REGION = Pattern.compile("^(\\d+)x(\\d+):\\s*(.*)$");

    public static long part1(String input) {
        List<String> lines = Util.lines(input);

        Map<Integer, Integer> shapeCells = new HashMap<>();
        int i = 0;

        while (i < lines.size()) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) { i++; continue; }

            if (REGION.matcher(line).matches()) break;

            if (!line.endsWith(":"))
                throw new IllegalArgumentException("Expected shape header like '0:' but got: " + lines.get(i));

            int idx = Integer.parseInt(line.substring(0, line.length() - 1).trim());
            i++;

            int cells = 0;

            while (i < lines.size()) {
                String s = lines.get(i);
                String t = s.trim();

                if (t.isEmpty()) { i++; break; }
                if (REGION.matcher(t).matches()) break;

                for (int c = 0; c < s.length(); c++) {
                    if (s.charAt(c) == '#') cells++;
                }
                i++;
            }

            shapeCells.put(idx, cells);
        }

        if (shapeCells.isEmpty())
            throw new IllegalStateException("No shapes parsed.");

        int maxShapeIdx = shapeCells.keySet().stream().mapToInt(x -> x).max().orElseThrow();
        int[] cellsPerShape = new int[maxShapeIdx + 1];
        for (int idx = 0; idx <= maxShapeIdx; idx++) {
            Integer v = shapeCells.get(idx);
            if (v == null)
                throw new IllegalStateException("Missing shape index " + idx);
            cellsPerShape[idx] = v;
        }

        long countFits = 0;

        for (; i < lines.size(); i++) {
            String line = lines.get(i).trim();
            if (line.isEmpty()) continue;

            Matcher m = REGION.matcher(line);
            if (!m.matches())
                throw new IllegalArgumentException("Bad region line: " + lines.get(i));

            long w = Long.parseLong(m.group(1));
            long h = Long.parseLong(m.group(2));
            long area = w * h;

            String rest = m.group(3).trim();
            if (rest.isEmpty())
                throw new IllegalArgumentException("Region quantities missing: " + lines.get(i));

            String[] parts = rest.split("\\s+");
            if (parts.length != cellsPerShape.length) {
                throw new IllegalArgumentException(
                        "Expected " + cellsPerShape.length + " quantities, got " + parts.length + " in: " + lines.get(i)
                );
            }

            long need = 0;
            for (int k = 0; k < parts.length; k++) {
                long qty = Long.parseLong(parts[k]);
                need += qty * (long) cellsPerShape[k];
            }

            if (need <= area) countFits++;
        }

        return countFits;
    }

    public static long part2(String input) {
        throw new UnsupportedOperationException("Day12 has no Part 2 provided yet in your prompt.");
    }
}
