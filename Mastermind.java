package Mastermind;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;

public class Mastermind extends JFrame implements ActionListener {
    InputButton guesses[][];
    OutputButton clues[][];
    InputButton key[];
    JButton btnEnter;
    int counter;
    String keyVal = new String();
    Random rand = new Random();

    public static void main(String args[]) {
        new Mastermind();
    }

    public Mastermind() {
        super("Mastermind");
        setupGUI();
        initValues();
        registerListeners();
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

    public void initValues() {
        counter = 9;

        for (int i=0;i<4;i++) {
            key[i].visOff();
            key[i].setCurrColor((rand.nextInt(6) + 1));
        }

        keyVal = stringValue(key);

        for (int i=0; i<10; i++) {
            for (int j=0; j<4; j++) {
                guesses[i][j].setCurrColor(0);
                if (i==9) {
                    guesses[i][j].editOn();
                } else {
                    guesses[i][j].editOff();
                }
                clues[i][j].setCurrColor(0);
            }
        }
    }

    public String stringValue(InputButton[] buttons) {
        String value = new String();

        for (int i=0; i<buttons.length; i++) {
            value += String.format("%d", buttons[i].getCurrColor());
        }

        System.out.println(value);
        return value;
    }

    public void registerListeners() {
        btnEnter.addActionListener(this);
    }

    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getSource() == btnEnter) {
            submitGuess();
        } else {
            System.out.println("ERROR");
        }
    }

    public void submitGuess() {
        boolean win = false;
        if (validGuess()) {
            win = returnClue();
            if (win) {
                gameOver(true);
                return;
            }
            counter--;
            if (counter < 0) {
                gameOver(false);
                return;
            }
            for (int i=0; i<4; i++) {
                guesses[counter][i].editOn();
            }
        }
    }

    public boolean validGuess() {
        for (int i=0; i<4; i++) {
            if (guesses[counter][i].getCurrColor() == 0) {
                return false;
            }
        }
        return true;
    }

    public void gameOver(boolean winner) {
        for (int i=0; i<4; i++) {
            key[i].visOn();
        }
        String msg = new String();
        if (winner) {
            msg = "You win!";
        } else {
            msg = "You lose!";
        }
        int ans =
            JOptionPane.showConfirmDialog(null,"Do you want to play again?",
                                          msg, JOptionPane.YES_NO_OPTION,
                                          JOptionPane.PLAIN_MESSAGE);
        if (ans == 0) {
            initValues();
        } else if (ans == 1) {
            System.exit(0);
        } else {
            System.out.println("ERROR");
        }
    }

    public boolean returnClue() {
        int exactMatch = 0;
        int colorMatch = 0;
        int result = 0;
        String guessVal = new String();
        guessVal = stringValue(guesses[counter]);

        for (int i=0; i<4; i++) {
            if (keyVal.charAt(i) == guessVal.charAt(i)) {
                exactMatch++;
            }
        }
        for (int i=1; i<7; i++) {
            colorMatch += Math.min(count(keyVal, Character.forDigit(i,10)),
                              count(guessVal, Character.forDigit(i,10)));
        }
        colorMatch -= exactMatch;

        System.out.println("[" + exactMatch + ", " + colorMatch + "]");

        for (int i=exactMatch; i>0; i--) {
            clues[counter][result].setCurrColor(2);
            result++;
        }
        for (int i=colorMatch; i>0; i--) {
            clues[counter][result].setCurrColor(1);
            result++;
        }

        if (exactMatch == 4) {
            return true;
        } else {
            return false;
        }
    }

    public int count(String value, char x) {
        int cnt = 0;
        for (int i=0; i<value.length(); i++) {
            if (value.charAt(i) == x) {
                cnt++;
            }
        }
        return cnt;
    }
}
