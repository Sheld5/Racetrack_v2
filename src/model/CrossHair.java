package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

public class CrossHair extends JPanel implements MouseListener {

    private Game game;
    private int index[];
    private int x, y;
    private boolean mouseOver;
    private boolean isNextAiMove;

    public CrossHair(int index[], Game game) {
        this.game = game;
        this.index = index;
        x = 0;
        y = 0;
        mouseOver = false;
        isNextAiMove = false;
        setSize(game.getTileSize(), game.getTileSize());
        setBackground(new Color(0, 0, 0, 0));
        addMouseListener(this);
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setSize(game.getTileSize(), game.getTileSize());
        BufferedImage image;
        if (game.humanOnTurn()) {
            if (mouseOver) {
                image = Resources.crosshairRed;
            } else {
                image = Resources.crosshair;
            }
        } else {
            if (isNextAiMove) {
                image = Resources.crosshairRed;
            } else {
                image = Resources.crosshair;
            }
        }
        g.drawImage(image.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
    }

    public int[] getIndex() {
        return index;
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
        mouseOver = true;
        game.repaint();
    }

    @Override
    public void mouseExited(MouseEvent e) {
        mouseOver = false;
        game.repaint();
    }

    public void setIsNextAiMove(boolean b) {
        isNextAiMove = b;
    }
}
