package main;

import keypad.KeypadJPanel;
import keypad.KeypadJPanel_ver2;

import javax.swing.JFrame;
import javax.swing.SwingUtilities;
import javax.swing.WindowConstants;

public class Run {

    private static JFrame mainFrame = new JFrame("Virtual keypad v1.2");

    public static void main(String[] args) {
        mainFrame.setResizable(false);
        mainFrame.setBounds(KeypadJPanel.getFrameRect());
        mainFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
        mainFrame.setContentPane(new KeypadJPanel_ver2());
        SwingUtilities.invokeLater(() -> {
            try {
                mainFrame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
