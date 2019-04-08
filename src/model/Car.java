package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

public class Car extends JPanel {
    private static int TURNS_SKIPPED_ON_CRASH = 3;
    public enum Color {
        RED, YELLOW, BLUE, GREEN
    }

    private Game game;
    private Color color;
    private DriverAI driver;
    private int[] coordinates, velocity;
    private boolean crashed, sunk, finished;
    private int crashCountdown;
    private int turnOfFinish;

    public Car(Color color, DriverAI driver, Game game) {
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

    public void setCoordinates(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    public void setCoordinates(int[] coordinates) {
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("coordinates have to be an int array containing two Integers");
        } else {
            this.coordinates = coordinates;
        }
    }

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

    public void setVelocity(int[] velocity) {
        if (velocity.length != 2) {
            throw new IllegalArgumentException("method setVelocity only accepts an int array with the length of 2 as its argument");
        } else {
            this.velocity[0] = velocity[0];
            this.velocity[1] = velocity[1];
        }
    }

    public int[] getCoordinates() {
        int[] coordinatesCopy = new int[2];
        coordinatesCopy[0] = coordinates[0];
        coordinatesCopy[1] = coordinates[1];
        return coordinatesCopy;
    }

    public int getTileX() {
        return coordinates[0];
    }

    public int getTileY() {
        return coordinates[1];
    }

    public int[] getVelocity() {
        int[] velocityCopy = new int[2];
        velocityCopy[0] = velocity[0];
        velocityCopy[1] = velocity[1];
        return velocityCopy;
    }

    public int getVelX() {
        return velocity[0];
    }

    public int getVelY() {
        return velocity[1];
    }

    public void crashed() {
        crashed = true;
        crashCountdown = TURNS_SKIPPED_ON_CRASH;
        game.repaint();
    }

    public void sunk() {
        sunk = true;
        game.repaint();
    }

    public void finished() {
        finished = true;
    }

    public boolean isCrashed() {
        return crashed;
    }

    public boolean isSunk() {
        return sunk;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setTurnOfFinish(int t) {
        turnOfFinish = t;
    }

    public int getTurnOfFinish() {
        return turnOfFinish;
    }

    public void countdown() {
        crashCountdown--;
        if (crashCountdown == 0) {
            crashed = false;
        }
        game.repaint();
    }

    public DriverAI getDriver() {
        return driver;
    }

}
