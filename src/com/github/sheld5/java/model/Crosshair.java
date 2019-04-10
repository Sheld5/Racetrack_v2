package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

// represents one tile of the crosshair
public class Crosshair extends JPanel implements MouseListener {

    private Game game;
    private int[] index;
    private int x, y;
    private boolean mouseOver;
    private boolean isNextAiMove;

    public Crosshair(int[] index, Game game) {
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

    // determines which texture this tile of crosshair should use depending on wheter it should be highlighted as next AI move and paints it
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

    // returns the index of this crosshair tile
    public int[] getIndex() {
        return index;
    }

    // sets the coordinates of this crosshair tile
    public void setTileXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // returns the X coordinate of this crosshair tile
    public int getTileX() {
        return x;
    }

    // returns the Y coordinate of this crosshair tile
    public int getTileY() {
        return y;
    }

    // calls the game.onCHClick() method when this crosshair tile is clicked by the user and passes the index of this crosshair tile to it
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

    // changes the value of the mouseOver to true when the mouse enters this crosshair tile and updates its texture accordingly
    @Override
    public void mouseEntered(MouseEvent e) {
        mouseOver = true;
        game.repaint();
    }

    // changes the value of the mouseOver to false when the mouse leaves this crosshair tile and updates its texture accordingly
    @Override
    public void mouseExited(MouseEvent e) {
        mouseOver = false;
        game.repaint();
    }

    // sets wheter this crosshair tile is the next move of the AI on turn
    public void setIsNextAiMove(boolean b) {
        isNextAiMove = b;
    }
}
