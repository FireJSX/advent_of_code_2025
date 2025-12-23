package main.java.aoc;

import java.util.*;

public class Day08 {

    public static long part1(String input) {
        // WICHTIG: kopieren, weil Util.lines(...) ggf. unveränderlich ist
        List<String> lines = new ArrayList<>(Util.lines(input));

        // trim trailing empty lines
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        int n = lines.size();
        if (n == 0) return 0;

        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) throw new IllegalArgumentException("Empty line at " + i);
            String[] parts = s.split(",");
            if (parts.length != 3) throw new IllegalArgumentException("Bad point: " + s);
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            long z = Long.parseLong(parts[2].trim());
            pts[i] = new Point(x, y, z);
        }

        int m = n * (n - 1) / 2; // for n=1000 -> 499,500
        Edge[] edges = new Edge[m];
        int idx = 0;

        for (int i = 0; i < n; i++) {
            Point a = pts[i];
            for (int j = i + 1; j < n; j++) {
                Point b = pts[j];
                long d2 = dist2(a, b);
                edges[idx++] = new Edge(i, j, d2);
            }
        }

        Arrays.sort(edges, Comparator.comparingLong(e -> e.d2));

        UnionFind uf = new UnionFind(n);

        int limit = Math.min(1000, edges.length);
        for (int k = 0; k < limit; k++) {
            Edge e = edges[k];
            uf.union(e.a, e.b);
        }

        int[] compSize = uf.componentSizes();

        int first = 0, second = 0, third = 0;
        for (int sz : compSize) {
            if (sz <= 0) continue;
            if (sz >= first) {
                third = second;
                second = first;
                first = sz;
            } else if (sz >= second) {
                third = second;
                second = sz;
            } else if (sz > third) {
                third = sz;
            }
        }

        return 1L * first * second * third;
    }

    public static long part2(String input) {
        // Einlesen (gleich wie part1, aber als eigene Kopie)
        List<String> lines = new ArrayList<>(Util.lines(input));
        while (!lines.isEmpty() && lines.get(lines.size() - 1).isEmpty()) {
            lines.remove(lines.size() - 1);
        }

        int n = lines.size();
        if (n == 0) return 0;

        Point[] pts = new Point[n];
        for (int i = 0; i < n; i++) {
            String s = lines.get(i).trim();
            if (s.isEmpty()) throw new IllegalArgumentException("Empty line at " + i);
            String[] parts = s.split(",");
            if (parts.length != 3) throw new IllegalArgumentException("Bad point: " + s);
            long x = Long.parseLong(parts[0].trim());
            long y = Long.parseLong(parts[1].trim());
            long z = Long.parseLong(parts[2].trim());
            pts[i] = new Point(x, y, z);
        }

        // Alle Kanten erzeugen und sortieren (wie part1)
        int m = n * (n - 1) / 2;
        Edge[] edges = new Edge[m];
        int idx = 0;
        for (int i = 0; i < n; i++) {
            Point a = pts[i];
            for (int j = i + 1; j < n; j++) {
                Point b = pts[j];
                edges[idx++] = new Edge(i, j, dist2(a, b));
            }
        }
        Arrays.sort(edges, Comparator.comparingLong(e -> e.d2));

        UnionFind uf = new UnionFind(n);

        int lastA = -1, lastB = -1;

        for (Edge e : edges) {
            // Nur wenn diese Kante wirklich zwei Komponenten verbindet:
            if (uf.unionAndReturnIfMerged(e.a, e.b)) {
                lastA = e.a;
                lastB = e.b;

                // Wenn jetzt alles verbunden ist, ist dies die gesuchte letzte Verbindung
                if (uf.components() == 1) {
                    break;
                }
            }
        }

        if (lastA < 0) throw new IllegalStateException("No merge happened (unexpected).");

        return pts[lastA].x * pts[lastB].x;
    }

    private static long dist2(Point a, Point b) {
        long dx = a.x - b.x;
        long dy = a.y - b.y;
        long dz = a.z - b.z;
        return dx * dx + dy * dy + dz * dz;
    }

    private record Point(long x, long y, long z) {}

    private static class Edge {
        final int a, b;
        final long d2;
        Edge(int a, int b, long d2) {
            this.a = a;
            this.b = b;
            this.d2 = d2;
        }
    }

    private static class UnionFind {
        private final int[] parent;
        private final int[] size;
        private int components;

        UnionFind(int n) {
            parent = new int[n];
            size = new int[n];
            components = n;
            for (int i = 0; i < n; i++) {
                parent[i] = i;
                size[i] = 1;
            }
        }

        int components() {
            return components;
        }

        int find(int x) {
            while (parent[x] != x) {
                parent[x] = parent[parent[x]];
                x = parent[x];
            }
            return x;
        }

        void union(int a, int b) {
            unionAndReturnIfMerged(a, b);
        }

        boolean unionAndReturnIfMerged(int a, int b) {
            int ra = find(a);
            int rb = find(b);
            if (ra == rb) return false;
            if (size[ra] < size[rb]) {
                int t = ra; ra = rb; rb = t;
            }
            parent[rb] = ra;
            size[ra] += size[rb];
            components--;
            return true;
        }

        int[] componentSizes() {
            int n = parent.length;
            int[] out = new int[n];
            for (int i = 0; i < n; i++) {
                int r = find(i);
                out[r] = size[r];
            }
            return out;
        }
    }
}
