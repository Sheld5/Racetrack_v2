package model;

import util.Resources;

import javax.swing.*;
import java.awt.*;

public class Car extends JPanel {

    private int x, y;
    private int velX, velY;
    private boolean finished;

    public Car(int size) {
        x = 0;
        y = 0;
        velX = 0;
        velY = 0;
        finished = false;
        setSize(size, size);
        setBackground(new Color(0, 255, 0, 0));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Resources.car.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
    }

    public void setTileXY(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void accelX(int a) {
        if (a < -1 || a > 1) {
            throw new IllegalArgumentException("method accelX only accepts values of {-1;0;1}");
        } else {
            velX += a;
        }
    }

    public void accelY(int a) {
        if (a < -1 || a > 1) {
            throw new IllegalArgumentException("method accelY only accepts values of {-1;0;1}");
        } else {
            velY += a;
        }
    }

    public void setVelX(int velX) {
        this.velX = velX;
    }

    public void setVelY(int velY) {
        this.velY = velY;
    }

    public int getTileX() {
        return x;
    }

    public int getTileY() {
        return y;
    }

    public int getVelX() {
        return velX;
    }

    public int getVelY() {
        return velY;
    }

    public void finished() {
        finished = true;
    }

    public boolean isFinished() {
        return finished;
    }

}
