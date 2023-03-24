package io.github.itzispyder.consolewindow.window;

import io.github.itzispyder.consolewindow.window.elements.ChatPanel;
import io.github.itzispyder.consolewindow.window.elements.ConsolePanel;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;

public class ConsoleWindow extends JFrame {

    public final JPanel mainPanel = new JPanel();

    public String minecraftLogPath = "../../AppData/Roaming/.minecraft/logs/latest.log";
    public File minecraftLog = new File(minecraftLogPath);
    public final JTextField logPathInput = new JTextField();
    public final JButton logPathButton = new JButton("Update Log Path and Reset Panes");
    public final JTextField logStats = new JTextField();
    public final JButton logToggleButton = new JButton("Pause Logging");

    public final GraphicsEnvironment graphicsEnv = GraphicsEnvironment.getLocalGraphicsEnvironment();
    public final GraphicsDevice device = graphicsEnv.getDefaultScreenDevice();
    public final ConsolePanel consolePanel = new ConsolePanel();
    public final ChatPanel chatPanel = new ChatPanel();
    public final JPanel bannerPanel = new JPanel();
    public final JTextArea title = new JTextArea();

    public ConsoleWindow() {

        try {
            setIconImage(ImageIO.read(new File("./src/main/resources/assets/images/icon.png")));
        } catch (Exception ignore) {}

        setLayout(new BorderLayout());
        setResizable(false);

        new Thread(() -> {
            this.startBannerPanel();
            this.startMainPanel();
        }).start();

        setTitle("Console Window - By ImproperIssues");
        setBounds(0,0, (int) (device.getDisplayMode().getWidth() * 0.75), (int) (device.getDisplayMode().getHeight() * 0.75));
        setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

    private void startBannerPanel() {
        bannerPanel.setBackground(Color.GRAY);
        bannerPanel.setLayout(new GridLayout(1,10));
        add(bannerPanel,BorderLayout.NORTH);

        title.setBackground(Color.GRAY);
        title.setForeground(Color.WHITE);
        title.setBorder(new LineBorder(Color.GRAY,10));
        title.setText("Welcome to the Console Window");
        title.setEditable(false);
        bannerPanel.add(title);

        logPathInput.setBackground(Color.DARK_GRAY);
        logPathInput.setForeground(Color.WHITE);
        logPathInput.setText(minecraftLogPath);
        logPathInput.setBorder(new TitledBorder(new LineBorder(Color.BLACK,1),"Log Path:",0,0,Font.getFont(Font.SANS_SERIF),Color.WHITE));
        bannerPanel.add(logPathInput);

        logPathButton.setBackground(Color.LIGHT_GRAY);
        logPathButton.addActionListener(e -> {
            logPathInput.setText(logPathInput.getText().replaceAll("[\"]",""));
            minecraftLogPath = logPathInput.getText();
            minecraftLog = new File(minecraftLogPath);
            this.resetPanes(minecraftLog);
        });
        bannerPanel.add(logPathButton);

        logStats.setBackground(Color.DARK_GRAY);
        logStats.setForeground(Color.YELLOW);
        logStats.setBorder(new TitledBorder(new LineBorder(Color.BLACK,1),"Log Statistics:",0,0,Font.getFont(Font.SANS_SERIF),Color.WHITE));
        bannerPanel.add(logStats);

        logToggleButton.setBackground(Color.PINK);
        logToggleButton.addActionListener(e -> {
            consolePanel.setLogging(!consolePanel.isLogging());
            if (consolePanel.isLogging()) {
                logToggleButton.setBackground(Color.PINK);
                logToggleButton.setText("Pause Logging");
            }
            else {
                logToggleButton.setBackground(Color.GREEN);
                logToggleButton.setText("Resume Logging");
            }
        });
        bannerPanel.add(logToggleButton);
    }

    private void startMainPanel() {
        mainPanel.setLayout(new BorderLayout());
        mainPanel.setBackground(Color.DARK_GRAY);
        mainPanel.setBorder(new LineBorder(Color.DARK_GRAY,20));
        add(mainPanel, BorderLayout.CENTER);

        consolePanel.setBorder(new LineBorder(Color.GRAY,20));
        mainPanel.add(consolePanel, BorderLayout.CENTER);

        chatPanel.setBorder(new LineBorder(Color.GRAY,20));
        mainPanel.add(chatPanel, BorderLayout.WEST);
    }

    public void resetPanes(File log) {
        try {
            this.chatPanel.errorField.setText(null);
            this.chatPanel.errorField.setCaretPosition(this.chatPanel.errorField.getDocument().getLength());
            this.chatPanel.chatField.setText(null);
            this.chatPanel.chatField.setCaretPosition(this.chatPanel.chatField.getDocument().getLength());
            this.consolePanel.consoleField.read(new BufferedReader(new FileReader(log)),null);
            this.consolePanel.consoleField.setCaretPosition(this.consolePanel.consoleField.getDocument().getLength());

            this.consolePanel.updateConsoleField();
        } catch (Exception ignore) {}
    }
}
