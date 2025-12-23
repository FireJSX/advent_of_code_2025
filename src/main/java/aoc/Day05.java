package main.java.aoc;

import java.util.*;

public class Day05 {
    static class Range {
        long a, b;
        Range(long a, long b) { this.a=a; this.b=b; }
    }

    private static List<Range> parseRanges(List<String> lines) {
        List<Range> ranges = new ArrayList<>();
        for (String s : lines) {
            if (s.isEmpty()) break;
            String[] p = s.split("-");
            ranges.add(new Range(Long.parseLong(p[0]), Long.parseLong(p[1])));
        }
        return ranges;
    }

    public static long part1(String input) {
        List<String> ls = Util.lines(input);
        int blank = ls.indexOf("");
        List<Range> ranges = parseRanges(ls);

        long count = 0;
        for (int i = blank+1; i < ls.size(); i++) {
            String s = ls.get(i);
            if (s.isEmpty()) continue;
            long id = Long.parseLong(s);
            boolean ok = false;
            for (Range r : ranges) {
                if (r.a <= id && id <= r.b) { ok = true; break; }
            }
            if (ok) count++;
        }
        return count;
    }

    public static long part2(String input) {
        List<Range> ranges = parseRanges(Util.lines(input));
        ranges.sort(Comparator.comparingLong(r -> r.a));

        long total = 0;
        long curA = Long.MIN_VALUE, curB = Long.MIN_VALUE;

        for (Range r : ranges) {
            if (curA == Long.MIN_VALUE) {
                curA = r.a; curB = r.b;
            } else if (r.a <= curB + 1) {
                curB = Math.max(curB, r.b);
            } else {
                total += (curB - curA + 1);
                curA = r.a; curB = r.b;
            }
        }
        if (curA != Long.MIN_VALUE) total += (curB - curA + 1);
        return total;
    }
}
