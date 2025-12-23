package main.java.aoc;

import java.io.*;
import java.nio.file.*;
import java.util.*;

public class Util {
    public static String readAll(String path) throws IOException {
        return Files.readString(Path.of(path));
    }

    public static List<String> lines(String input) {
        return Arrays.asList(input.split("\\R", -1));
    }
}
