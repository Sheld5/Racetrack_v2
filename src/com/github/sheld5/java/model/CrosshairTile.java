package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;

/**
 * Represents one tile of the crosshair used by human players to control their cars.
 */
public class CrosshairTile extends JPanel implements MouseListener {

    private Game game;
    private int[] index;
    private int x, y;
    private boolean mouseOver;
    private boolean isNextAiMove;

    /**
     * The CrosshairTile class constructor.
     * @param index the index of this tile of the crosshair.
     * @param game the game to which is this crosshair going to be added.
     */
    public CrosshairTile(int[] index, Game game) {
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

    /**
     * Determines which texture this tile of crosshair should use
     * depending on wheter it should be highlighted as next AI move and paints it.
     * @param g
     */
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

    /**
     * Returns the index of this crosshair tile.
     * @return the index of this crosshair tile.
     */
    public int[] getIndex() {
        return index;
    }

    /**
     * Sets the coordinates of this crosshair tile.
     * @param x the value to which is the X coordinate to be set.
     * @param y the value to which is the Y coordinate to be set.
     */
    public void setTileXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Returns the X coordinate of this crosshair tile.
     * @return the X coordinate of this crosshair tile.
     */
    public int getTileX() {
        return x;
    }

    /**
     * Returns the Y coordinate of this crosshair tile.
     * @return the Y coordinate of this crosshair tile.
     */
    public int getTileY() {
        return y;
    }

    /**
     * Calls the game.onCHClick() method when this crosshair tile is clicked by the user
     * and passes the index of this crosshair tile to it.
     * @see Game#onCHClick(int[])
     * @param e
     */
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

    /**
     * Changes the value of the mouseOver to true when the mouse enters this crosshair tile
     * and calls game.repaint() to update its texture.
     * @param e
     */
    @Override
    public void mouseEntered(MouseEvent e) {
        mouseOver = true;
        game.repaint();
    }

    /**
     * Changes the value of the mouseOver to false when the mouse leaves this crosshair tile
     * and calls game.repaint() to update its texture.
     * @param e
     */
    @Override
    public void mouseExited(MouseEvent e) {
        mouseOver = false;
        game.repaint();
    }

    /**
     * Sets whether this crosshair tile is the next move of the AI on turn.
     * @param b the value to which is the boolean isNextAiMove to be set.
     */
    public void setIsNextAiMove(boolean b) {
        isNextAiMove = b;
    }
}
