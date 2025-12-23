package main.java.aoc;

import java.util.*;
import java.util.BitSet;
import java.math.BigInteger;

public class Day07 {

    public static long part1(String input) {
        List<String> rows = new ArrayList<>(Util.lines(input));

        while (!rows.isEmpty() && rows.get(rows.size() - 1).isEmpty()) {
            rows.remove(rows.size() - 1);
        }

        int H = rows.size();
        if (H == 0) return 0;

        int W = 0;
        for (String s : rows) W = Math.max(W, s.length());

        int sRow = -1, sCol = -1;
        for (int y = 0; y < H; y++) {
            int idx = rows.get(y).indexOf('S');
            if (idx >= 0) {
                sRow = y;
                sCol = idx;
                break;
            }
        }
        if (sRow < 0) throw new IllegalArgumentException("No 'S' found in input.");

        BitSet active = new BitSet(W);
        active.set(sCol);

        long splits = 0;

        for (int y = sRow + 1; y < H; y++) {
            String row = rows.get(y);
            BitSet next = new BitSet(W);

            for (int x = active.nextSetBit(0); x >= 0; x = active.nextSetBit(x + 1)) {
                char c = (x < row.length()) ? row.charAt(x) : '.';

                if (c == '^') {
                    splits++;
                    if (x - 1 >= 0) next.set(x - 1);
                    if (x + 1 < W) next.set(x + 1);
                } else {
                    next.set(x);
                }
            }

            active = next;
            if (active.isEmpty()) break;
        }

        return splits;
    }

    public static String part2(String input) {
        List<String> rows = new ArrayList<>(Util.lines(input));

        while (!rows.isEmpty() && rows.get(rows.size() - 1).isEmpty()) {
            rows.remove(rows.size() - 1);
        }

        int H = rows.size();
        if (H == 0) return "0";

        int W = 0;
        for (String s : rows) W = Math.max(W, s.length());

        int sRow = -1, sCol = -1;
        for (int y = 0; y < H; y++) {
            int idx = rows.get(y).indexOf('S');
            if (idx >= 0) {
                sRow = y;
                sCol = idx;
                break;
            }
        }
        if (sRow < 0) throw new IllegalArgumentException("No 'S' found in input.");

        HashMap<Integer, BigInteger> cur = new HashMap<>();
        cur.put(sCol, BigInteger.ONE);

        BigInteger finished = BigInteger.ZERO;

        for (int y = sRow + 1; y < H; y++) {
            String row = rows.get(y);
            HashMap<Integer, BigInteger> next = new HashMap<>(cur.size() * 2 + 8);

            for (Map.Entry<Integer, BigInteger> e : cur.entrySet()) {
                int x = e.getKey();
                BigInteger ways = e.getValue();
                if (ways.signum() == 0) continue;

                char c = (x < row.length()) ? row.charAt(x) : '.';

                if (c == '^') {
                    int lx = x - 1;
                    if (lx < 0) {
                        finished = finished.add(ways);
                    } else {
                        next.merge(lx, ways, BigInteger::add);
                    }

                    int rx = x + 1;
                    if (rx >= W) {
                        finished = finished.add(ways);
                    } else {
                        next.merge(rx, ways, BigInteger::add);
                    }
                } else {
                    // continue straight down
                    next.merge(x, ways, BigInteger::add);
                }
            }

            cur = next;
            if (cur.isEmpty()) break;
        }

        for (BigInteger ways : cur.values()) finished = finished.add(ways);

        return finished.toString();
    }
}
