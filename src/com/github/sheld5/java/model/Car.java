package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * represents a car in the game
 */
public class Car extends JPanel {
    private static int TURNS_SKIPPED_ON_CRASH = 3;
    public enum Color {
        RED, YELLOW, BLUE, GREEN
    }

    private Game game;
    private String playerName, aiName;
    private Color color;
    private DriverAI driver;
    private int[] coordinates, velocity;
    private boolean crashed, sunk, finished;
    private int crashCountdown;

    public Car(String playerName, String aiName, Color color, DriverAI driver, Game game) {
        this.playerName = playerName;
        this.aiName = aiName;
        this.game = game;
        this.color = color;
        this.driver = driver;
        coordinates = new int[]{0,0};
        velocity = new int[]{0,0};
        crashCountdown = 0;
        finished = false;
        setSize(game.getTileSize(), game.getTileSize());
        setBackground(new java.awt.Color(0, 0, 0, 0));
    }

    /**
     * determines which texture should the car use and paints the car
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setSize(game.getTileSize(), game.getTileSize());
        BufferedImage image;
        switch (color) {
            case RED:
                image = Resources.carRed;
                break;
            case YELLOW:
                image = Resources.carYellow;
                break;
            case BLUE:
                image = Resources.carBlue;
                break;
            case GREEN:
                image = Resources.carGreen;
                break;
            default:
                image = Resources.carRed;
        }
        g.drawImage(image.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
        if (crashed) {
            switch (crashCountdown) {
                case 1:
                    image = Resources.one;
                    break;
                case 2:
                    image = Resources.two;
                    break;
                case 3:
                    image = Resources.three;
                    break;
            }
            if (0 < crashCountdown && crashCountdown < 4) {
                g.drawImage(image.getScaledInstance(game.getTileSize() - 6, game.getTileSize() - 6, Image.SCALE_SMOOTH), 3, 3, null);
            }
        }
    }

    /**
     * sets the coordinates of the car to the given values
     * @param x
     * @param y
     */
    public void setCoordinates(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    /**
     * sets the coordinates of the car to the given values
     * @param coordinates
     */
    public void setCoordinates(int[] coordinates) {
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("coordinates have to be an int array containing two Integers");
        } else {
            this.coordinates = coordinates;
        }
    }

    /**
     * changes the velocity vector of the car accordignly by adding the given acceleration vector to it
     * @param a
     */
    public void accelerate(int[] a) {
        if (a.length != 2) {
            throw new IllegalArgumentException("coordinates have to be an int array containing two Integers");
        } else if (a[0] < -1 || a[0] > 1 || a[1] < -1 || a[1] > 1) {
            throw new IllegalArgumentException("method accelerate only accepts values of {-1;0;1}");
        } else {
            velocity[0] += a[0];
            velocity[1] += a[1];
        }
    }

    /**
     * sets the velocity vector of the car to the given values
     * @param velocity
     */
    public void setVelocity(int[] velocity) {
        if (velocity.length != 2) {
            throw new IllegalArgumentException("method setVelocity only accepts an int array with the length of 2 as its argument");
        } else {
            this.velocity[0] = velocity[0];
            this.velocity[1] = velocity[1];
        }
    }

    /**
     * returns a deep copy of the coordinates of the car
     * @return
     */
    public int[] getCoordinates() {
        int[] coordinatesCopy = new int[2];
        coordinatesCopy[0] = coordinates[0];
        coordinatesCopy[1] = coordinates[1];
        return coordinatesCopy;
    }

    /**
     * returns the X coordinate of the car
     * @return
     */
    public int getTileX() {
        return coordinates[0];
    }

    /**
     * returns the Y coordinate of the car
     * @return
     */
    public int getTileY() {
        return coordinates[1];
    }

    /**
     * returns a deep copy of the velocity vector of the car
     * @return
     */
    public int[] getVelocity() {
        int[] velocityCopy = new int[2];
        velocityCopy[0] = velocity[0];
        velocityCopy[1] = velocity[1];
        return velocityCopy;
    }

    /**
     * returns the X coordinate of the velocity vector of the car
     * @return
     */
    public int getVelX() {
        return velocity[0];
    }

    /**
     * returns the Y coordinate of the velocity vector of the car
     * @return
     */
    public int getVelY() {
        return velocity[1];
    }

    /**
     * sets the car to "crashed" and start the crash-countdown
     */
    public void crashed() {
        crashed = true;
        crashCountdown = TURNS_SKIPPED_ON_CRASH;
        game.repaint();
    }

    /**
     * sets the car to "sunk"
     */
    public void sunk() {
        sunk = true;
        game.repaint();
    }

    /**
     * sets the car to "finished"
     */
    public void finished() {
        finished = true;
    }

    /**
     * returns true if the car is crashed
     * @return
     */
    public boolean isCrashed() {
        return crashed;
    }

    /**
     * returns true if the car is sunk
     * @return
     */
    public boolean isSunk() {
        return sunk;
    }

    /**
     * returns true if the car is finished
     * @return
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * decreases crashCountdown by one and sets the car to "not crashed" if the countdown is finished
     */
    public void countdown() {
        crashCountdown--;
        if (crashCountdown == 0) {
            crashed = false;
        }
        game.repaint();
    }

    /**
     * returns the AI assigned to this car
     * @return
     */
    public DriverAI getDriver() {
        return driver;
    }

    /**
     * returns the name of the player this car was assigned to
     * @return
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * returns the name of the AI assigned to this car
     * @return
     */
    public String getAiName() {
        return aiName;
    }

}
