package main;

import keypad.KeypadJPanel;

import javax.swing.*;

public class Run {

    private static JFrame mainFrame = new JFrame("Virtual keypad v1.2");

    public static void main(String[] args) {
        mainFrame.setResizable(false);
        mainFrame.setBounds(KeypadJPanel.getFrameSize());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setContentPane(new KeypadJPanel());
        SwingUtilities.invokeLater(() -> {
            try {
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
