package main.java.aoc;

import java.util.*;
import java.math.BigInteger;

public class Day06 {

    private static List<String> normalizeLines(String input) {
        List<String> ls = Util.lines(input);
        while (!ls.isEmpty() && ls.get(ls.size()-1).isEmpty()) ls = ls.subList(0, ls.size()-1);

        int maxW = 0;
        for (String s : ls) maxW = Math.max(maxW, s.length());
        List<String> out = new ArrayList<>();
        for (String s : ls) {
            if (s.length() < maxW) s = s + " ".repeat(maxW - s.length());
            out.add(s);
        }
        return out;
    }

    public static String part1(String input) {
        List<String> rows = normalizeLines(input);
        int H = rows.size();
        int W = rows.get(0).length();

        String opRow = rows.get(H - 1);

        BigInteger total = BigInteger.ZERO;

        int x = 0;
        while (x < W) {
            while (x < W && isBlankColumn(rows, x)) x++;
            if (x >= W) break;

            int l = x;
            while (x < W && !isBlankColumn(rows, x)) x++;
            int r = x - 1;

            char op = 0;
            for (int cx = l; cx <= r; cx++) {
                char c = opRow.charAt(cx);
                if (c == '+' || c == '*') { op = c; break; }
            }
            if (op == 0) throw new IllegalStateException("No operator found for block " + l + ".." + r);

            List<BigInteger> nums = new ArrayList<>();
            for (int ry = 0; ry < H - 1; ry++) {
                String line = rows.get(ry).substring(l, r + 1);

                String digits = extractFirstNumber(line);
                if (digits != null) {
                    nums.add(new BigInteger(digits));
                }
            }

            if (nums.isEmpty()) continue;

            BigInteger value;
            if (op == '+') {
                value = BigInteger.ZERO;
                for (BigInteger n : nums) value = value.add(n);
            } else {
                value = BigInteger.ONE;
                for (BigInteger n : nums) value = value.multiply(n);
            }

            total = total.add(value);
        }

        return total.toString();
    }

    public static String part2(String input) {
        List<String> rows = normalizeLines(input);
        int H = rows.size();
        int W = rows.get(0).length();

        String opRow = rows.get(H - 1);

        BigInteger total = BigInteger.ZERO;

        int x = W - 1;
        while (x >= 0) {
            while (x >= 0 && isBlankColumn(rows, x)) x--;
            if (x < 0) break;

            int r = x;
            while (x >= 0 && !isBlankColumn(rows, x)) x--;
            int l = x + 1;

            char op = 0;
            for (int cx = l; cx <= r; cx++) {
                char c = opRow.charAt(cx);
                if (c == '+' || c == '*') { op = c; break; }
            }
            if (op == 0) throw new IllegalStateException("No operator found for block " + l + ".." + r);

            List<BigInteger> nums = new ArrayList<>();
            for (int cx = r; cx >= l; cx--) {
                StringBuilder sb = new StringBuilder();
                for (int ry = 0; ry < H - 1; ry++) {
                    char c = rows.get(ry).charAt(cx);
                    if (c >= '0' && c <= '9') sb.append(c);
                }
                if (sb.length() == 0) continue;
                nums.add(new BigInteger(sb.toString()));
            }

            if (nums.isEmpty()) continue;

            BigInteger value;
            if (op == '+') {
                value = BigInteger.ZERO;
                for (BigInteger n : nums) value = value.add(n);
            } else {
                value = BigInteger.ONE;
                for (BigInteger n : nums) value = value.multiply(n);
            }

            total = total.add(value);
        }

        return total.toString();
    }

    private static boolean isBlankColumn(List<String> rows, int col) {
        for (String row : rows) {
            if (col < row.length() && row.charAt(col) != ' ') return false;
        }
        return true;
    }

    private static String extractFirstNumber(String s) {
        int i = 0;
        while (i < s.length() && !Character.isDigit(s.charAt(i))) i++;
        if (i == s.length()) return null;
        int j = i;
        while (j < s.length() && Character.isDigit(s.charAt(j))) j++;
        return s.substring(i, j);
    }
}