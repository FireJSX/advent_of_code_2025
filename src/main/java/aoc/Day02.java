package main.java.aoc;

import java.math.BigInteger;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class Day02 {

    public static BigInteger part1(String input) {
        List<Range> ranges = parseRanges(input);
        BigInteger total = BigInteger.ZERO;

        for (Range r : ranges) {
            for (long x = r.start; x <= r.end; x++) {
                if (isRepeatedPattern(x, 2, 2)) {
                    total = total.add(BigInteger.valueOf(x));
                }
            }
        }
        return total;
    }

    public static BigInteger part2(String input) {
        List<Range> ranges = parseRanges(input);
        BigInteger total = BigInteger.ZERO;

        for (Range r : ranges) {
            for (long x = r.start; x <= r.end; x++) {
                if (isRepeatedPattern(x, 2, Integer.MAX_VALUE)) {
                    total = total.add(BigInteger.valueOf(x));
                }
            }
        }
        return total;
    }

    private static boolean isRepeatedPattern(long x, int minRepeats, int maxRepeats) {
        String s = Long.toString(x);
        int n = s.length();

        // blockLen must divide n, and reps = n/blockLen >= 2
        for (int blockLen = 1; blockLen <= n / 2; blockLen++) {
            if (n % blockLen != 0) continue;

            int reps = n / blockLen;
            if (reps < minRepeats || reps > maxRepeats) continue;

            String block = s.substring(0, blockLen);

            boolean ok = true;
            for (int i = blockLen; i < n; i += blockLen) {
                if (!s.regionMatches(i, block, 0, blockLen)) {
                    ok = false;
                    break;
                }
            }
            if (ok) return true;
        }
        return false;
    }

    private static List<Range> parseRanges(String input) {
        String line = input.trim().replace("\r", "").replace("\n", "");
        if (line.endsWith(",")) line = line.substring(0, line.length() - 1);

        List<Range> ranges = new ArrayList<>();
        for (String part : line.split(",")) {
            part = part.trim();
            if (part.isEmpty()) continue;

            String[] ab = part.split("-");
            if (ab.length != 2) throw new IllegalArgumentException("Bad range: " + part);

            long a = Long.parseLong(ab[0].trim());
            long b = Long.parseLong(ab[1].trim());
            if (b < a) throw new IllegalArgumentException("Range end < start: " + part);

            ranges.add(new Range(a, b));
        }
        return ranges;
    }

    private record Range(long start, long end) {}
}
