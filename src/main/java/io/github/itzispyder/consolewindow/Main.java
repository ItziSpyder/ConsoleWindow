package io.github.itzispyder.consolewindow;

import io.github.itzispyder.consolewindow.window.ConsoleWindow;

public final class Main {

    public static ConsoleWindow mainFrame;

    public static void main(String[] args) {
        new Thread(() -> {
            mainFrame = new ConsoleWindow();
        }).start();
        System.out.println("Loaded and ready!");
    }

    public static String ramUsage() {
        Runtime r = Runtime.getRuntime();
        return Math.floor(((r.maxMemory() - r.freeMemory()) / (double) r.maxMemory()) * 1000) / 10 + "%";
    }
}
