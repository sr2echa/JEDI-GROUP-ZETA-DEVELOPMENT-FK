package com.flipfit.client;

import java.util.List;

public class CLIUtils {

    // Detailed Colors
    public static final String RESET = "\u001B[0m";
    public static final String BLACK = "\u001B[30m";
    public static final String RED = "\u001B[31m";
    public static final String GREEN = "\u001B[32m";
    public static final String YELLOW = "\u001B[33m";
    public static final String BLUE = "\u001B[34m";
    public static final String PURPLE = "\u001B[35m";
    public static final String CYAN = "\u001B[36m";
    public static final String WHITE = "\u001B[37m";

    public static final String BOLD = "\u001B[1m";

    public static void clear() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    public static void printBox(String title, List<String> options) {
        int width = 60;
        String line = "═".repeat(width);

        System.out.println(CYAN + "╔" + line + "╗" + RESET);

        // Title centered
        int titlePadding = (width - title.length()) / 2;
        System.out.println(CYAN + "║" + RESET + " ".repeat(titlePadding) + BOLD + YELLOW + title + RESET
                + " ".repeat(width - titlePadding - title.length()) + CYAN + "║" + RESET);

        System.out.println(CYAN + "╠" + "═".repeat(width) + "╣" + RESET);

        for (String opt : options) {
            System.out.println(
                    CYAN + "║" + RESET + "  " + opt + " ".repeat(width - opt.length() - 4) + CYAN + "║" + RESET);
        }

        System.out.println(CYAN + "╚" + "═".repeat(width) + "╝" + RESET);
    }

    public static void printTable(List<String> headers, List<List<String>> rows) {
        if (headers == null || rows == null || headers.isEmpty())
            return;

        // Calculate Column Widths
        int[] widths = new int[headers.size()];
        for (int i = 0; i < headers.size(); i++) {
            widths[i] = headers.get(i).length();
        }
        for (List<String> row : rows) {
            for (int i = 0; i < Math.min(row.size(), widths.length); i++) {
                widths[i] = Math.max(widths[i], row.get(i).length());
            }
        }

        // Top Border
        StringBuilder border = new StringBuilder("┌");
        for (int w : widths)
            border.append("─".repeat(w + 2)).append("┬");
        border.setCharAt(border.length() - 1, '┐');
        System.out.println(CYAN + border + RESET);

        // Headers
        System.out.print(CYAN + "│" + RESET);
        for (int i = 0; i < headers.size(); i++) {
            System.out.printf(" " + BOLD + YELLOW + "%-" + widths[i] + "s" + RESET + " │", headers.get(i));
        }
        System.out.println();

        // Separator
        StringBuilder sep = new StringBuilder("├");
        for (int w : widths)
            sep.append("─".repeat(w + 2)).append("┼");
        sep.setCharAt(sep.length() - 1, '┤');
        System.out.println(CYAN + sep + RESET);

        // Rows
        for (List<String> row : rows) {
            System.out.print(CYAN + "│" + RESET);
            for (int i = 0; i < headers.size(); i++) {
                String val = (i < row.size()) ? row.get(i) : "";
                System.out.printf(" %-" + widths[i] + "s │", val);
            }
            System.out.println();
        }

        // Bottom Border
        StringBuilder bottom = new StringBuilder("└");
        for (int w : widths)
            bottom.append("─".repeat(w + 2)).append("┴");
        bottom.setCharAt(bottom.length() - 1, '┘');
        System.out.println(CYAN + bottom + RESET);
    }

    public static void printHeader(String text) {
        System.out.println("\n" + BOLD + PURPLE + ">>> " + text.toUpperCase() + " <<<" + RESET);
    }

    public static void printSuccess(String msg) {
        System.out.println(BOLD + GREEN + "✔ " + msg + RESET);
    }

    public static void printError(String msg) {
        System.out.println(BOLD + RED + "✘ " + msg + RESET);
    }
}
