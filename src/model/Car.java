package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;

public class Car extends JPanel {

    private static int TURNS_SKIPPED_ON_CRASH = 3;

    private Game game;
    private int[] coordinates, velocity;
    private boolean crashed, sunk, finished;
    private int crashCountdown;
    private int turnOfFinish;

    public Car(Game game) {
        this.game = game;
        coordinates = new int[]{0,0};
        velocity = new int[]{0,0};
        crashCountdown = 0;
        finished = false;
        setSize(game.getTileSize(), game.getTileSize());
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setSize(game.getTileSize(), game.getTileSize());
        g.drawImage(Resources.car.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
        if (crashed) {
            switch (crashCountdown) {
                case 1:
                    g.drawImage(Resources.one.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
                case 2:
                    g.drawImage(Resources.two.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
                case 3:
                    g.drawImage(Resources.three.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
                default:
                    // nada
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

}
