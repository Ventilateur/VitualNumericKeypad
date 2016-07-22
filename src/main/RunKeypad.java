package main;

import keypad.Keypad;

import javax.swing.*;

/**
 * Created by Phan Vu Hoang on 7/22/2016.
 */
public class RunKeypad {
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
