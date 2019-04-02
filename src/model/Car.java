package model;

import util.Resources;

import javax.swing.*;
import java.awt.*;

public class Car extends JPanel {

    private int[] coordinates, velocity;
    private boolean crashed, finished;
    private int turnOfFinish;

    public Car(int size) {
        coordinates = new int[]{0,0};
        velocity = new int[]{0,0};
        finished = false;
        setSize(size, size);
        setBackground(new Color(0, 0, 0, 0));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Resources.car.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
    }

    public void setCoordinates(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    public void accelerate(int[] a) {
        if (a.length != 2) {
            throw new IllegalArgumentException("method accelerate only accepts an int array with the length of 2 as its argument");
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

    public int[] getVelocity() {
        int[] velocityCopy = new int[2];
        velocityCopy[0] = velocity[0];
        velocityCopy[1] = velocity[1];
        return velocityCopy;
    }

    public void crashed() {
        crashed = true;
    }

    public void finished() {
        finished = true;
    }

    public boolean isCrashed() {
        return crashed;
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

}
