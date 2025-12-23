package main.java.aoc;

import java.util.*;

public class Day03 {
    public static long part1(String input) {
        long sum = 0;
        for (String s : Util.lines(input)) {
            if (s.isEmpty()) continue;
            int n = s.length();
            int[] sufMax = new int[n+1];
            sufMax[n] = -1;
            for (int i = n-1; i >= 0; i--) {
                sufMax[i] = Math.max(sufMax[i+1], s.charAt(i) - '0');
            }
            int best = -1;
            for (int i = 0; i < n-1; i++) {
                int a = s.charAt(i) - '0';
                int b = sufMax[i+1];
                best = Math.max(best, 10*a + b);
            }
            sum += best;
        }
        return sum;
    }

    public static String part2(String input) {
        java.math.BigInteger total = java.math.BigInteger.ZERO;

        for (String s : Util.lines(input)) {
            if (s.isEmpty()) continue;
            int n = s.length();
            int keep = 12;
            int remove = n - keep;

            Deque<Character> st = new ArrayDeque<>();
            for (int i = 0; i < n; i++) {
                char c = s.charAt(i);
                while (remove > 0 && !st.isEmpty() && st.peekLast() < c) {
                    st.pollLast();
                    remove--;
                }
                st.addLast(c);
            }
            while (remove > 0) { st.pollLast(); remove--; }

            StringBuilder sb = new StringBuilder();
            int k = 0;
            for (char c : st) {
                if (k == keep) break;
                sb.append(c);
                k++;
            }

            total = total.add(new java.math.BigInteger(sb.toString()));
        }

        return total.toString();
    }
}

