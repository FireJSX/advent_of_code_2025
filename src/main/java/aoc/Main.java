package main.java.aoc;

import java.nio.file.Files;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws Exception {
        String input = Files.readString(Path.of("inputs/day06.txt")).trim();

        System.out.println("Day12 Part1: " + Day06.part1(input));
        System.out.println("Day12 Part2: " + Day06.part2(input));
    }
}
