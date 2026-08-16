package com.examsystem;

import javax.swing.*;

/**
 * Main.java
 * ---------------------------------------------
 * Entry point of the whole project. Just opens
 * the LoginScreen. Every project needs exactly
 * ONE main() method to start.
 *
 * Explained by: STUDENT 2 (Login / GUI part)
 * ---------------------------------------------
 */
public class Main {
    public static void main(String[] args) {
        // SwingUtilities.invokeLater makes sure the GUI is built
        // safely on Java's Event Dispatch Thread (best practice for Swing).
        SwingUtilities.invokeLater(() -> new LoginScreen());
    }
}
