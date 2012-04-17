package Mastermind;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Mastermind extends JFrame {
    InputButton guesses[][];
    OutputButton clues[][];
    InputButton key[];
    JButton btnEnter;

    public static void main(String args[]) {
        new Mastermind();
    }

    public Mastermind() {
        super("Mastermind");
        setupGUI();
    }

    public void setupGUI() {
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new GridLayout(11, 2, 0, 15));
        //mainPanel.add(new JLabel("Key:"));
        JPanel keyPanel = new JPanel();
        keyPanel.setLayout(new FlowLayout());
        key = new InputButton[4];
        for (int i=0; i<4; i++) {
            key[i] = new InputButton();
            keyPanel.add(key[i]);
        }
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        JPanel guessPanels[] = new JPanel[10];
        JPanel cluePanels[] = new JPanel[10];
        guesses = new InputButton[10][4];
        clues = new OutputButton[10][4];
        for (int i=0; i<10; i++) {
            cluePanels[i] = new JPanel();
            cluePanels[i].setLayout(new GridLayout(2,2));
            guessPanels[i] = new JPanel();
            for (int j=0; j<4; j++) {
                clues[i][j] = new OutputButton();
                cluePanels[i].add(clues[i][j]);
                guesses[i][j] = new InputButton();
                guessPanels[i].add(guesses[i][j]);
            }
            mainPanel.add(cluePanels[i]);
            mainPanel.add(guessPanels[i]);
        }
        btnEnter = new JButton("Enter");
        JPanel enterPanel = new JPanel();
        enterPanel.add(btnEnter);
        Container window = this.getContentPane();
        window.setLayout(new BorderLayout());
        window.add(keyPanel, BorderLayout.NORTH);
        window.add(mainPanel, BorderLayout.CENTER);
        window.add(enterPanel, BorderLayout.SOUTH);

        this.setDefaultCloseOperation(Mastermind.EXIT_ON_CLOSE);
        this.setSize(325, 700);
        this.setVisible(true);
    }
}
