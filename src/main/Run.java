package main;

import keypad.Keypad;
import javax.swing.SwingUtilities;

public class Run {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                Keypad frame = new Keypad();
                frame.setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }
}
