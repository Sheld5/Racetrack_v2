package model;

import main.Resources;

import javax.swing.*;
import java.awt.*;

public class Car extends JPanel {

    public Car(int width, int height) {
        setSize(width, height);
        setBackground(new Color(0, 255, 0, 0));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Resources.car.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
    }

}
