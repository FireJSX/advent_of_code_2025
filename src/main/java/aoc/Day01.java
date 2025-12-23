package main.java.aoc;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day01 {

    private static final Pattern LINE = Pattern.compile("^([LR])(\\d+)$");

    public static long part1(String input) {
        long pos = 50;
        long count = 0;

        for (String raw : input.split("\\R")) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            Move m = parse(line);

            pos = apply(pos, m.dir, m.steps);

            if (pos == 0) count++;
        }
        return count;
    }

    public static long part2(String input) {
        long pos = 50;
        long count = 0;

        for (String raw : input.split("\\R")) {
            String line = raw.trim();
            if (line.isEmpty()) continue;

            Move m = parse(line);

            count += zerosHitDuringMove(pos, m.dir, m.steps);

            pos = apply(pos, m.dir, m.steps);
        }
        return count;
    }

    private static long apply(long pos, char dir, long steps) {
        long s = steps % 100;
        if (dir == 'R') {
            return (pos + s) % 100;
        } else { // 'L'
            return (pos - s) % 100;
        }
    }

    private static long zerosHitDuringMove(long pos, char dir, long steps) {
        if (steps <= 0) return 0;

        long first;
        if (dir == 'R') {
            long offset = (100 - (pos % 100)) % 100; // 0..99
            first = (offset == 0) ? 100 : offset;    // if already at 0, first hit is after 100 clicks
        } else { // 'L'
            long offset = (pos % 100 + 100) % 100;   // 0..99
            first = (offset == 0) ? 100 : offset;
        }

        if (steps < first) return 0;

        return 1 + (steps - first) / 100;
    }

    private static Move parse(String line) {
        Matcher m = LINE.matcher(line);
        if (!m.matches()) {
            throw new IllegalArgumentException("Bad move line: " + line);
        }
        char dir = m.group(1).charAt(0);
        long steps = Long.parseLong(m.group(2));
        return new Move(dir, steps);
    }

    private record Move(char dir, long steps) {}
}

