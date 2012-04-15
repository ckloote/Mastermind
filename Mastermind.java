package Mastermind;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mastermind extends JFrame {
    RoundButton guesses[][];
    RoundButton key[];

    public static void main(String args[]) {
        new Mastermind();
    }

    public Mastermind() {
        super("Mastermind");
        setupGUI();
    }

    public void setupGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(12, 1));
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new FlowLayout());
        key = new RoundButton[4];
        for (int i=0; i<4; i++) {
            key[i] = new RoundButton();
            keyPanel.add(key[i]);
        }
        mainPanel.add(keyPanel);
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel guessPanels[] = new JPanel[10];
        guesses = new RoundButton[10][4];
        for (int i=0; i<10; i++) {
            guessPanels[i] = new JPanel();
            for (int j=0; j<4; j++) {
                guesses[i][j] = new RoundButton();
                guessPanels[i].add(guesses[i][j]);
            }
            mainPanel.add(guessPanels[i]);
        }
        Container window = this.getContentPane();
        window.setLayout(new FlowLayout());
        window.add(mainPanel);

        this.setDefaultCloseOperation(Mastermind.EXIT_ON_CLOSE);
        this.setSize(250, 550);
        this.setVisible(true);
    }
}
