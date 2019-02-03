package main;

import javax.swing.*;
import java.awt.*;

class Menu extends JPanel {

    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 50;
    private final int SELECTION_X = (GameMain.getGameWidth() / 2) - (BUTTON_WIDTH / 2);

    Menu() {
        init();
        initSelection();
        System.out.println("Menu initialized");
    }

    private void init() {
        setSize(GameMain.getGameWidth(), GameMain.getGameHeight());
        setBackground(Color.BLACK);
        setVisible(true);
    }

    private void initSelection() {
        initPlayButton();
        initExitButton();
    }

    private void initPlayButton() {
        JButton playButton = new JButton("Play");
        playButton.setBounds(SELECTION_X, 50, BUTTON_WIDTH, BUTTON_HEIGHT);
        playButton.addActionListener(e -> GameMain.startGame());
        add(playButton);
    }

    private void initExitButton() {
        JButton exitButton = new JButton("Exit");
        exitButton.setBounds(SELECTION_X, 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.addActionListener(e -> System.out.println("Exit button pressed"));
        add(exitButton);
    }

}
