package model;

import main.Resources;

import javax.swing.*;
import java.awt.*;

public class Car extends JPanel {

    private int x, y;
    private int velX, velY;
    private int size;

    public Car(int size) {
        this.size = size;
        velX = 0;
        velY = 0;
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

}
