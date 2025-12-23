package main.java.aoc;

import java.util.*;

public class Day04 {
    static final int[] DX = {-1,-1,-1,0,0,1,1,1};
    static final int[] DY = {-1,0,1,-1,1,-1,0,1};

    public static int part1(String input) {
        List<String> ls = Util.lines(input);
        List<String> gridLines = new ArrayList<>();
        for (String l : ls) if (!l.isEmpty()) gridLines.add(l);

        int h = gridLines.size(), w = gridLines.get(0).length();
        char[][] g = new char[h][w];
        for (int y = 0; y < h; y++) g[y] = gridLines.get(y).toCharArray();

        int count = 0;
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
            if (g[y][x] != '@') continue;
            int adj = 0;
            for (int k = 0; k < 8; k++) {
                int nx = x + DX[k], ny = y + DY[k];
                if (0 <= nx && nx < w && 0 <= ny && ny < h && g[ny][nx] == '@') adj++;
            }
            if (adj < 4) count++;
        }
        return count;
    }

    public static int part2(String input) {
        List<String> ls = Util.lines(input);
        List<String> gridLines = new ArrayList<>();
        for (String l : ls) if (!l.isEmpty()) gridLines.add(l);

        int h = gridLines.size(), w = gridLines.get(0).length();
        char[][] g = new char[h][w];
        for (int y = 0; y < h; y++) g[y] = gridLines.get(y).toCharArray();

        int[][] adj = new int[h][w];
        ArrayDeque<int[]> q = new ArrayDeque<>();
        boolean[][] inQ = new boolean[h][w];

        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
            if (g[y][x] != '@') continue;
            int a = 0;
            for (int k = 0; k < 8; k++) {
                int nx = x + DX[k], ny = y + DY[k];
                if (0 <= nx && nx < w && 0 <= ny && ny < h && g[ny][nx] == '@') a++;
            }
            adj[y][x] = a;
        }
        for (int y = 0; y < h; y++) for (int x = 0; x < w; x++) {
            if (g[y][x] == '@' && adj[y][x] < 4) {
                q.add(new int[]{x,y});
                inQ[y][x] = true;
            }
        }

        int removed = 0;
        while (!q.isEmpty()) {
            int[] p = q.poll();
            int x = p[0], y = p[1];
            inQ[y][x] = false;

            if (g[y][x] != '@') continue;
            if (adj[y][x] >= 4) continue;

            g[y][x] = '.';
            removed++;

            for (int k = 0; k < 8; k++) {
                int nx = x + DX[k], ny = y + DY[k];
                if (0 <= nx && nx < w && 0 <= ny && ny < h && g[ny][nx] == '@') {
                    adj[ny][nx]--;
                    if (adj[ny][nx] < 4 && !inQ[ny][nx]) {
                        q.add(new int[]{nx, ny});
                        inQ[ny][nx] = true;
                    }
                }
            }
        }
        return removed;
    }
}

