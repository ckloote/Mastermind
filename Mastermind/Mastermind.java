package Mastermind;

import java.io.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import java.util.Random;
import java.util.ArrayList;
import java.util.Arrays;

// Mastermind GUI and game logic
public class Mastermind extends JFrame implements ActionListener {
    // store guesses and clues, also UI elements
    InputButton guesses[][];
    OutputButton clues[][];

    // store and display key
    InputButton key[];

    // Submit guess or key
    JButton btnEnter;

    // counter of position in guess list
    int counter;

    // current game mode
    int mode;

    // string representation of key
    String keyVal = new String();

    // needed for random key generation
    Random rand = new Random();

    // game main - create game object
    public static void main(String args[]) {
        new Mastermind();
    }

    // game constructor
    public Mastermind() {
        super("Mastermind");
        setupGUI();
        initValues();
        registerListeners();
    }

    // draw GUI
    public void setupGUI() {
        // create main panels, set color and layout
        JPanel mainPanel = new JPanel();
        mainPanel.setBackground(Color.LIGHT_GRAY);
        mainPanel.setLayout(new GridLayout(11, 2, 0, 15));
        JPanel keyPanel = new JPanel();
        keyPanel.setBackground(Color.LIGHT_GRAY);
        keyPanel.setLayout(new FlowLayout());

        // create buttons to store key, add to panel
        key = new InputButton[4];
        for (int i=0; i<4; i++) {
            key[i] = new InputButton();
            keyPanel.add(key[i]);
        }

        // break between key and guesses
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));
        mainPanel.add(new JSeparator(SwingConstants.HORIZONTAL));

        //create guess panels and corresponding clue panels
        JPanel guessPanels[] = new JPanel[10];
        JPanel cluePanels[] = new JPanel[10];
        guesses = new InputButton[10][4];
        clues = new OutputButton[10][4];
        for (int i=0; i<10; i++) {
            cluePanels[i] = new JPanel();
            cluePanels[i].setBackground(Color.LIGHT_GRAY);
            cluePanels[i].setLayout(new GridLayout(2,2));
            guessPanels[i] = new JPanel();
            guessPanels[i].setBackground(Color.LIGHT_GRAY);
            for (int j=0; j<4; j++) {
                clues[i][j] = new OutputButton();
                cluePanels[i].add(clues[i][j]);
                guesses[i][j] = new InputButton();
                guessPanels[i].add(guesses[i][j]);
            }
            mainPanel.add(cluePanels[i]);
            mainPanel.add(guessPanels[i]);
        }

        // input button
        btnEnter = new JButton("Enter");
        JPanel enterPanel = new JPanel();
        enterPanel.setBackground(Color.LIGHT_GRAY);
        enterPanel.add(btnEnter);

        // main container
        Container window = this.getContentPane();
        window.setLayout(new BorderLayout());
        window.add(keyPanel, BorderLayout.NORTH);
        window.add(mainPanel, BorderLayout.CENTER);
        window.add(enterPanel, BorderLayout.SOUTH);

        // housekeeping
        this.setDefaultCloseOperation(Mastermind.EXIT_ON_CLOSE);
        this.setSize(325, 700);
        this.setVisible(true);
    }

    // reset the board, initalize game
    public void initValues() {
        //ask for and set game mode
        Object[] options = {"Codebreaker", "Codemaker"};
        mode = JOptionPane.showOptionDialog(null,
                                            "Who would you like to play as?",
                                            "Game Mode",
                                            JOptionPane.YES_NO_OPTION,
                                            JOptionPane.QUESTION_MESSAGE,
                                            null,
                                            options,
                                            options[0]);

        // init counter
        counter = 9;

        // init key
        for (int i=0;i<4;i++) {
            // random if in codebreaker mode
            if (mode == 0) {
                key[i].setVis(false);
                key[i].setCurrColor((rand.nextInt(6) + 1));
            } else {
                // user input if in codemaker mode
                key[i].setCurrColor(0);
                key[i].setEdit(true);
            }
        }

        // save key as string if key exists
        if (mode == 0) {
            keyVal = stringValue(key);
        }

        // clear the guesses section of the board, reset for input
        for (int i=0; i<10; i++) {
            for (int j=0; j<4; j++) {
                guesses[i][j].setCurrColor(0);
                if (i==9 && mode == 0) {
                    guesses[i][j].setEdit(true);
                } else {
                    guesses[i][j].setEdit(false);
                }
                clues[i][j].setCurrColor(0);
            }
        }
    }

    // get string value of a set of input buttons
    // this is helpful to have for scoring
    public String stringValue(InputButton[] buttons) {
        String value = new String();

        // Store color as string of form "XXXX"
        // where X = [1,6]
        for (int i=0; i<buttons.length; i++) {
            value += String.format("%d", buttons[i].getCurrColor());
        }

        System.out.println(value);
        return value;
    }

    // register listener for enter button
    public void registerListeners() {
        btnEnter.addActionListener(this);
    }

    // set action for enter button
    public void actionPerformed(ActionEvent e) {
        System.out.println(e.getActionCommand());
        if (e.getSource() == btnEnter && mode == 0) {
            // codebreaker mode
            submitGuess();
        } else if (e.getSource() == btnEnter && mode == 1) {
            // codemaker mode
            playComp();
        } else {
            System.out.println("ERROR");
        }
    }

    // handles submission of input
    public void submitGuess() {
        boolean win = false;
        // check for valid input
        if (validGuess(guesses[counter])) {
            // score the entry and check for winning
            win = returnClue();
            if (win) {
                System.out.println("winner");
                gameOver(true);
                return;
            }

            // decrement counter to the next guess
            counter--;

            // check if out of guesses, player loses
            if (counter < 0) {
                gameOver(false);
                return;
            }

            // set last to no edit, set next guess to edit
            for (int i=0; i<4; i++) {
                guesses[counter][i].setEdit(true);
                guesses[counter+1][i].setEdit(false);
            }
        }
    }

    // checks for valid input (all circles have a color)
    public boolean validGuess(InputButton[] button) {
        for (int i=0; i<4; i++) {
            if (button[i].getCurrColor() == 0) {
                return false;
            }
        }
        return true;
    }

    // handles end of game
    public void gameOver(boolean winner) {
        // reveal the key
        for (int i=0; i<4; i++) {
            key[i].setVis(true);
        }

        // set message to player
        String msg = new String();
        if (winner) {
            msg = "You win!";
        } else {
            msg = "You lose!";
        }

        // display message and ask for new game
        int ans =
            JOptionPane.showConfirmDialog(null,"Do you want to play again?",
                                          msg, JOptionPane.YES_NO_OPTION,
                                          JOptionPane.PLAIN_MESSAGE);

        // reset game, or end
        if (ans == 0) {
            initValues();
        } else if (ans == 1) {
            System.exit(0);
        } else {
            System.out.println("ERROR");
        }
    }

    // plays for the computer
    public void playComp() {
        // make sure user has entered a valid key
        if (validGuess(key)) {
            // holds computer guess and score of guess
            String guess = new String();
            String[] sc = new String[2];

            // holds all possible key
            ArrayList<String> possible = new ArrayList<String>();

            // hold keys that have not been ruled out
            ArrayList<String> sol = new ArrayList<String>();

            // seeds arraylists - to start both hold all 1296 possbile
            // keys. possible values are 1111-6666, using 1-6 only
            for (int i=1111; i<6667; i++) {
                if ( !String.format("%d", i).contains("0") &&
                     !String.format("%d", i).contains("7") &&
                     !String.format("%d", i).contains("8") &&
                     !String.format("%d", i).contains("9") ) {
                    possible.add(String.format("%d", i));
                    sol.add(String.format("%d", i));
                }
            }

            // save string version of user-provided key
            keyVal = stringValue(key);

            // computer's initial guess
            // display guess, check for a win and display clue
            // calculate guess score
            guess = "1122";
            guesses[counter][0].setCurrColor(1);
            guesses[counter][1].setCurrColor(1);
            guesses[counter][2].setCurrColor(2);
            guesses[counter][3].setCurrColor(2);
            submitGuess();
            sc = score(keyVal, guess).split(",");

            // try until out of guesses or we win
            while (counter >= 0 && !sc[0].equals("4")) {
                // rule out impossible guesses from the remaining
                // guesses
                for (int i=0; i<sol.size(); i++) {
                    // score our guess versus a possible key
                    // if the score does not match the actual score,
                    // then this must not be a possible key
                    while (i != sol.size() &&
                           !Arrays.deepEquals(sc,
                                              score(sol.get(i),
                                                    guess).split(","))) {
                        sol.remove(i);
                    }
                }

                // if we only have one possibility left
                // it must be the key. Guess that.
                if (sol.size() == 1) {
                    guess = sol.get(0);
                } else {
                    // otherwise we must calculate the best possible
                    // guess: The guess that minimizes the
                    // maximum number of remaining possibilities
                    int minval = 1296;
                    for (int i=0; i<possible.size(); i++) {
                        // for any possible guess calculate the max number
                        // of possibilies left after that guess
                        int tmp = getMax(possible.get(i), sol);
                        if (tmp < minval) {
                            // then keep the lowest of those
                            minval = tmp;
                            guess = possible.get(i);
                        }
                    }
                }

                // we have our guess, so input it
                for (int i=0; i<4; i++) {
                    guesses[counter][i].setCurrColor(Integer.parseInt(guess.substring(i, i+1)));
                }
                sc = score(keyVal, guess).split(",");
                submitGuess();
            }
        }
    }

    // calculate the maximum possible number of remaining possibilies
    // that could be eliminate by a potential guess
    public int getMax(String attempt, ArrayList<String> set) {
        // possible scores
        String[] scores = {"0,0", "0,1", "0,2", "0,3", "0,4", "1,0", "1,1",
                           "1,2", "1,3", "2,0", "2,1", "2,2", "3,0", "4,0"};
        // counter
        int max = 0;

        // for each possible score
        for (int i=0; i<scores.length; i++) {
            int cnt = 0;
            // compare guess to remaining possible keys
            // keep the maxmimum eliminated by a potential score
            for (int j=0; j<set.size(); j++) {
                if (score(set.get(j), attempt).equals(scores[i])) {
                    cnt++;
                }
            }
            // and keep the biggest out of that set
            if (cnt > max) {
                max = cnt;
            }
        }
        // and now we'll have the true max
        return max;
    }

    // show the clue for the current guess
    public boolean returnClue() {
        // exact matches
        int exactMatch = 0;

        // color matches
        int colorMatch = 0;

        // current button
        int result = 0;

        // string representation of guess
        String guessVal = new String();

        // guess score
        String sc[] = new String[2];

        // get string of guess, then score it
        guessVal = stringValue(guesses[counter]);
        sc = score(keyVal, guessVal).split(",");
        System.out.println(sc[0] + "," + sc[1]);

        // light up the exact matches
        for (int i=Integer.parseInt(sc[0]); i>0; i--) {
            clues[counter][result].setCurrColor(2);
            result++;
        }

        // light up the color matches
        for (int i=Integer.parseInt(sc[1]); i>0; i--) {
            clues[counter][result].setCurrColor(1);
            result++;
        }

        // tell the caller if we've won or not
        if (sc[0].equals("4")) {
            return true;
        } else {
            return false;
        }
    }

    // calculate a score for a guess as a pair of ints
    public String score(String key, String guess) {
        int exactMatch = 0;
        int colorMatch = 0;

        // first find the exact matches
        for (int i=0; i<4; i++) {
            if (key.charAt(i) == guess.charAt(i)) {
                exactMatch++;
            }
        }

        // then find the color matches
        for (int i=1; i<7; i++) {
            colorMatch += Math.min(count(key, Character.forDigit(i,10)),
                                   count(guess, Character.forDigit(i,10)));
        }

        // since these overlap, remove the exact matches from the
        // color matches so we do not duplicate
        colorMatch -= exactMatch;

        // and return the pair of numbers
        return exactMatch + "," + colorMatch;
    }

    // counts the number of a given color in a guess
    // used for scoring
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
