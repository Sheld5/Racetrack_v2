package model;

import main.Resources;

import javax.swing.*;
import java.awt.*;

public class Crosshair extends JPanel {

    public Crosshair(int size) {
        setSize(size, size);
        setBackground(new Color(0, 255, 0, 0));
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.drawImage(Resources.crosshair.getScaledInstance(getWidth(), getHeight(), Image.SCALE_SMOOTH), 0, 0, null);
    }

}
