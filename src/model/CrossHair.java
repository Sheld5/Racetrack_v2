package model;

import main.Game;
import main.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

public class CrossHair extends JPanel implements MouseListener {

    private int index;
    private int x, y;
    private Game game;

    public CrossHair(int index, int size, Game game) {
        this.game = game;
        this.index = index;
        setSize(size, size);
        setBackground(new Color(0, 255, 0, 0));
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Resources.crosshair.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
    }

    public void setTileXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getTileX() {
        return x;
    }

    public int getTileY() {
        return y;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        game.onCHClick(index);
    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }
}
