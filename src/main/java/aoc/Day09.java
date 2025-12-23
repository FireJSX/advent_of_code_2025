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
        List<String> lines = new ArrayList<>(Util.lines(input));
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) lines.remove(lines.size() - 1);
        if (lines.isEmpty()) return 0;

        // Parse red vertices (in order)
        int n = lines.size();
        int[] rx = new int[n];
        int[] ry = new int[n];
        for (int i = 0; i < n; i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) { n = i; break; }
            String[] parts = s.split(",");
            if (parts.length != 2) throw new IllegalArgumentException("Bad coordinate: " + s);
            rx[i] = Integer.parseInt(parts[0].trim());
            ry[i] = Integer.parseInt(parts[1].trim());
        }

        HashSet<Long> boundary = new HashSet<>(n * 16);
        int minX = Integer.MAX_VALUE, minY = Integer.MAX_VALUE;
        int maxX = Integer.MIN_VALUE, maxY = Integer.MIN_VALUE;

        for (int i = 0; i < n; i++) {
            minX = Math.min(minX, rx[i]);
            maxX = Math.max(maxX, rx[i]);
            minY = Math.min(minY, ry[i]);
            maxY = Math.max(maxY, ry[i]);
        }

        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            int x1 = rx[i], y1 = ry[i];
            int x2 = rx[j], y2 = ry[j];
            if (x1 != x2 && y1 != y2) {
                throw new IllegalArgumentException("Adjacent vertices must share row/col, but got: " +
                        x1 + "," + y1 + " -> " + x2 + "," + y2);
            }
            if (x1 == x2) {
                int step = Integer.compare(y2, y1);
                for (int y = y1; y != y2 + step; y += step) boundary.add(pack(x1, y));
            } else {
                int step = Integer.compare(x2, x1);
                for (int x = x1; x != x2 + step; x += step) boundary.add(pack(x, y1));
            }
        }

        TreeSet<Integer> xsSet = new TreeSet<>();
        TreeSet<Integer> ysSet = new TreeSet<>();

        xsSet.add(minX - 1); xsSet.add(maxX + 2);
        ysSet.add(minY - 1); ysSet.add(maxY + 2);

        for (int i = 0; i < n; i++) {
            xsSet.add(rx[i]); xsSet.add(rx[i] + 1);
            ysSet.add(ry[i]); ysSet.add(ry[i] + 1);
        }

        for (int i = 0; i < n; i++) {
            xsSet.add(rx[i] - 1); xsSet.add(rx[i] + 2);
            ysSet.add(ry[i] - 1); ysSet.add(ry[i] + 2);
        }

        int[] xVals = toIntArray(xsSet);
        int[] yVals = toIntArray(ysSet);

        int W = xVals.length - 1;
        int H = yVals.length - 1;

        boolean[][] inside = new boolean[H][W];

        for (long p : boundary) {
            int x = unpackX(p), y = unpackY(p);
            int cx = lowerBound(xVals, x);
            int cy = lowerBound(yVals, y);
            if (cx >= 0 && cx < W && xVals[cx] == x && cy >= 0 && cy < H && yVals[cy] == y) {
                inside[cy][cx] = true;
            }
        }

        List<VerticalEdge> vEdges = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            int j = (i + 1) % n;
            int x1 = rx[i], y1 = ry[i];
            int x2 = rx[j], y2 = ry[j];
            if (x1 == x2 && y1 != y2) {
                int ya = Math.min(y1, y2);
                int yb = Math.max(y1, y2);
                vEdges.add(new VerticalEdge(x1, ya, yb));
            }
        }

        for (int y = minY; y <= maxY; y++) {
            ArrayList<Integer> xs = new ArrayList<>();
            for (VerticalEdge e : vEdges) {
                if (y >= e.y1 && y < e.y2) xs.add(e.x);
            }
            if (xs.isEmpty()) continue;
            Collections.sort(xs);

            for (int k = 0; k + 1 < xs.size(); k += 2) {
                int xL = xs.get(k);
                int xR = xs.get(k + 1);
                int cxL = lowerBound(xVals, xL);
                int cxR = lowerBound(xVals, xR);
                int cy = lowerBound(yVals, y);
                if (cy < 0 || cy >= H) continue;

                for (int cx = cxL; cx <= cxR && cx < W; cx++) {
                    int tx = xVals[cx];
                    if (tx >= xL && tx <= xR && yVals[cy] == y) {
                        inside[cy][cx] = true;
                    }
                }
            }
        }

        long[][] pref = new long[H + 1][W + 1];
        for (int cy = 0; cy < H; cy++) {
            long rowSum = 0;
            long cellH = (long) (yVals[cy + 1] - yVals[cy]);
            for (int cx = 0; cx < W; cx++) {
                long cellW = (long) (xVals[cx + 1] - xVals[cx]);
                long add = inside[cy][cx] ? cellW * cellH : 0;
                rowSum += add;
                pref[cy + 1][cx + 1] = pref[cy][cx + 1] + rowSum;
            }
        }

        long best = 0;
        for (int i = 0; i < n; i++) {
            int x1 = rx[i], y1 = ry[i];
            for (int j = i + 1; j < n; j++) {
                int x2 = rx[j], y2 = ry[j];
                if (x1 == x2 || y1 == y2) continue;

                int xMin = Math.min(x1, x2);
                int xMax = Math.max(x1, x2);
                int yMin = Math.min(y1, y2);
                int yMax = Math.max(y1, y2);

                long area = (long) (xMax - xMin + 1) * (long) (yMax - yMin + 1);
                if (area <= best) continue;

                int cx1 = lowerBound(xVals, xMin);
                int cx2 = lowerBound(xVals, xMax + 1) - 1;
                int cy1 = lowerBound(yVals, yMin);
                int cy2 = lowerBound(yVals, yMax + 1) - 1;

                if (cx1 < 0 || cy1 < 0 || cx2 >= W || cy2 >= H) continue;

                long insideArea = rectSum(pref, cx1, cy1, cx2, cy2);
                if (insideArea == area) {
                    best = area;
                }
            }
        }

        return best;
    }

    private static long rectSum(long[][] pref, int x1, int y1, int x2, int y2) {
        int A = y1, B = y2 + 1;
        int L = x1, R = x2 + 1;
        return pref[B][R] - pref[A][R] - pref[B][L] + pref[A][L];
    }

    private static int[] toIntArray(TreeSet<Integer> s) {
        int[] a = new int[s.size()];
        int i = 0;
        for (int v : s) a[i++] = v;
        return a;
    }

    private static int lowerBound(int[] a, int x) {
        int l = 0, r = a.length;
        while (l < r) {
            int m = (l + r) >>> 1;
            if (a[m] < x) l = m + 1;
            else r = m;
        }
        return l;
    }

    private static long pack(int x, int y) {
        return (((long) x) << 32) ^ (y & 0xffffffffL);
    }
    private static int unpackX(long p) { return (int) (p >> 32); }
    private static int unpackY(long p) { return (int) p; }

    private static class VerticalEdge {
        final int x;
        final int y1, y2;
        VerticalEdge(int x, int y1, int y2) { this.x = x; this.y1 = y1; this.y2 = y2; }
    }
}
