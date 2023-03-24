package io.github.itzispyder.consolewindow.window.elements;

import io.github.itzispyder.consolewindow.Main;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConsolePanel extends JPanel {

    public final JTextArea consoleField = new JTextArea();
    public final JScrollPane consoleScroller = new JScrollPane(consoleField,JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
    public final JTextArea inputField = new JTextArea();
    public final JScrollPane inputScroller = new JScrollPane(inputField,JScrollPane.VERTICAL_SCROLLBAR_NEVER,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    public Thread fileUpdater;
    private boolean logging;

    public ConsolePanel() {
        this.logging = true;
        setBackground(Color.GRAY);
        setLayout(new BorderLayout());

        new Thread(() -> {
            startConsoleField();
            startInputField();
        }).start();

        new Thread(() -> {
            try {
                Thread.sleep(3 * 1000);
                updateConsoleField();
            } catch (Exception ignore) {}
        }).start();
    }

    private void startConsoleField() {
        consoleField.setLineWrap(true);
        consoleField.setBackground(Color.BLACK);
        consoleField.setForeground(Color.GREEN);
        consoleField.setBorder(new LineBorder(Color.BLACK,14));
        consoleField.setEditable(false);
        consoleField.setColumns(70);
        consoleScroller.setBorder(new TitledBorder("Console Panel"));
        consoleScroller.setBackground(Color.GRAY);
        consoleScroller.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        consoleScroller.setAutoscrolls(true);
        add(consoleScroller,BorderLayout.CENTER);
    }

    private void startInputField() {
        inputField.setBackground(Color.GRAY);
        inputField.setForeground(Color.WHITE);
        inputField.setLineWrap(false);
        inputField.setRows(1);
        inputField.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        // under dev
        inputField.setEditable(false);
        inputField.setText("THIS FEATURE IS STILL UNDER DEVELOPMENT!");
        // under dev
        inputScroller.setBorder(new TitledBorder("Command Input"));
        inputScroller.setBackground(Color.GRAY);
        add(inputScroller,BorderLayout.SOUTH);
    }

    public void updateConsoleField() {
        if (fileUpdater != null) fileUpdater.stop();
        fileUpdater = new Thread(() -> {
            try {
                File mainLog = Main.mainFrame.minecraftLog;
                FileReader fr = new FileReader(mainLog);
                BufferedReader br = new BufferedReader(fr);
                consoleField.read(br, null);
                consoleField.setCaretPosition(consoleField.getDocument().getLength());

                String line = br.readLine();
                while(true) {
                    Main.mainFrame.logStats.setText("Logging (" + consoleField.getText().length() + "): " + logging + ", Mem: " + Main.ramUsage());
                    logLine(line);
                    line = br.readLine();
                    Thread.sleep(50);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        fileUpdater.start();
    }

    public void logLine(String line) {
        final String lastSeen = consoleField.getText();
        final int maxLength = 50000;
        if (logging && line != null && !lastSeen.contains(line)) {
            consoleField.setText(lastSeen + "\n" + line);
            consoleField.setCaretPosition(consoleField.getDocument().getLength());
            if (line.contains(" [CHAT] ")) logChat(line);
            else if (line.contains("[Render thread/ERROR]") || line.contains("[Render thread/WARN]")) logError(line);
        }
        if (lastSeen.length() >= maxLength) {
            consoleField.setText(lastSeen.substring(lastSeen.length() - (maxLength - 5000)));
            consoleField.setCaretPosition(consoleField.getDocument().getLength());
        }
    }

    public void logError(String line) {
        JTextArea chat = Main.mainFrame.chatPanel.errorField;
        chat.setText(chat.getText() + "\n" + line);
        chat.setCaretPosition(chat.getDocument().getLength());
    }

    public void logChat(String line) {
        JTextArea chat = Main.mainFrame.chatPanel.chatField;
        line = line.replaceAll("\\[Render thread/INFO\\]","")
                .replaceAll("\\[CHAT\\] ","")
                .replaceAll("\\[System\\]","");
        chat.setText(chat.getText() + "\n" + line);
        chat.setCaretPosition(chat.getDocument().getLength());
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }
}
