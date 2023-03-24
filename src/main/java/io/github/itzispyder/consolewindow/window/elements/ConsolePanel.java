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

        consoleField.setLineWrap(true);
        consoleField.setBackground(Color.BLACK);
        consoleField.setForeground(Color.GREEN);
        consoleField.setBorder(new LineBorder(Color.BLACK,14));
        consoleField.setEditable(false);
        consoleField.setColumns(50);
        consoleScroller.setBorder(new TitledBorder("Console Panel"));
        consoleScroller.setBackground(Color.GRAY);
        consoleScroller.setCursor(Cursor.getPredefinedCursor(Cursor.DEFAULT_CURSOR));
        consoleScroller.setAutoscrolls(true);
        add(consoleScroller,BorderLayout.CENTER);

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

        updateConsoleField();
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
                    if (logging && line != null && !consoleField.getText().contains(line)) {
                        consoleField.setText(consoleField.getText() + "\n" + line);
                        consoleField.setCaretPosition(consoleField.getDocument().getLength());

                        if (line.contains(" [CHAT] ")) {
                            JTextArea chat = Main.mainFrame.chatPanel.chatField;
                            line = line.replaceAll("\\[Render thread/INFO\\]","")
                                       .replaceAll("\\[CHAT\\] ","")
                                       .replaceAll("\\[System\\]","");
                            chat.setText(chat.getText() + "\n" + line);
                            chat.setCaretPosition(chat.getDocument().getLength());
                        }
                        if (line.contains("[Render thread/ERROR]") || line.contains("[Render thread/WARN]")) {
                            JTextArea chat = Main.mainFrame.chatPanel.errorField;
                            chat.setText(chat.getText() + "\n" + line);
                            chat.setCaretPosition(chat.getDocument().getLength());
                        }

                        Main.mainFrame.logStats.setText("Logging: " + logging + ", Mem: " + Main.mainFrame.ramUsage());
                    }
                    line = br.readLine();
                    Thread.sleep(50);
                }
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        fileUpdater.start();
    }

    public boolean isLogging() {
        return logging;
    }

    public void setLogging(boolean logging) {
        this.logging = logging;
    }
}
