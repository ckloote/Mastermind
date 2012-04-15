package Mastermind;

import java.awt.*;
import javax.swing.*;
import java.awt.event.*;

public class InputButton extends JComponent implements MouseListener {
    private int  currColor;
    private Color colors[];
    private boolean mousePressed;

    public InputButton() {
        this.addMouseListener(this);

        currColor = 0;
        mousePressed = false;
        colors = new Color[7];
        colors[0] = Color.BLACK;
        colors[1] = Color.RED;
        colors[2] = Color.BLUE;
        colors[3] = Color.GREEN;
        colors[4] = Color.YELLOW;
        colors[5] = Color.ORANGE;
        colors[6] = Color.MAGENTA;
    }

    public void paint(Graphics g) {
        if (mousePressed) {
            if (currColor == 6) {
                currColor = 1;
            } else {
                currColor++;
            }
        }
        g.setColor(colors[currColor]);
        if (currColor == 0) {
            g.drawOval(0,0,30,30);;
        } else {
            g.fillOval(0,0,30,30);
        }
    }

    public int getCurrColor() {
        return currColor;
    }

    public Dimension getMinimumSize() {
        return new Dimension(30,30);
    }

    public Dimension getPreferredSize() {
        return new Dimension(30,30);
    }

    public void mousePressed(MouseEvent e) {
        mousePressed = true;
        repaint();
    }

    public void mouseReleased(MouseEvent e) {
        mousePressed = false;
    }

    public void mouseClicked(MouseEvent e) {}

    public void mouseEntered(MouseEvent e) {}

    public void mouseExited(MouseEvent e) {}
}
