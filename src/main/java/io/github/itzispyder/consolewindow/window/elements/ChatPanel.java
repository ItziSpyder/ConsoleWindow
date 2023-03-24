package io.github.itzispyder.consolewindow.window.elements;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;

public class ChatPanel extends JPanel {

    public final JTextArea chatField = new JTextArea();
    public final JScrollPane chatScroller = new JScrollPane(chatField,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
    public final JTextArea errorField = new JTextArea();
    public final JScrollPane errorScroller = new JScrollPane(errorField,ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS,ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);

    public ChatPanel() {

        setLayout(new GridLayout(2,1));
        setBackground(Color.GRAY);

        chatField.setBackground(Color.BLACK);
        chatField.setForeground(Color.WHITE);
        chatField.setEditable(false);
        chatField.setBorder(new LineBorder(Color.BLACK,5));
        chatField.setLineWrap(true);
        chatField.setColumns(30);
        chatScroller.setBorder(new TitledBorder("Chat Panel"));
        chatScroller.setBackground(Color.GRAY);
        add(chatScroller);

        errorField.setBackground(Color.BLACK);
        errorField.setForeground(Color.RED);
        errorField.setColumns(30);
        errorField.setEditable(false);
        errorField.setLineWrap(false);
        errorField.setBorder(new LineBorder(Color.BLACK,5));
        errorScroller.setBorder(new TitledBorder("Exceptions Caught"));
        errorScroller.setBackground(Color.GRAY);
        add(errorScroller);
    }
}
