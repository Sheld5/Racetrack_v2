package main;

import javax.swing.*;
import java.awt.*;

class Menu extends JPanel {

    private static final int BUTTON_WIDTH = 100;
    private static final int BUTTON_HEIGHT = 50;

    private JButton playButton, exitButton;
    private JLabel gameModeSelection;
    private JButton startButton, backButton;

    Menu(int width, int height) {
        init(width, height);
        initMenuSelection();
        initGameModeSelection();
        System.out.println("Menu initialized");
    }

    private void init(int width, int height) {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initMenuSelection() {
        int buttonX = getWidth() / 2 - BUTTON_WIDTH / 2;

        playButton = new JButton("Play");
        playButton.setBounds(buttonX , 50, BUTTON_WIDTH, BUTTON_HEIGHT);
        playButton.addActionListener(e -> goToGameModeSelection());
        playButton.setVisible(true);
        add(playButton);

        exitButton = new JButton("Exit");
        exitButton.setBounds(buttonX , 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setVisible(true);
        add(exitButton);
    }

    private void initGameModeSelection() {
        gameModeSelection = new JLabel("Game Mode Selection");
        gameModeSelection.setBounds(getWidth() / 2 - 125, getHeight() / 2 - 225, 250, 50);
        gameModeSelection.setVisible(false);
        add(gameModeSelection);

        int buttonX = getWidth() / 2 - 50;

        startButton = new JButton("Start Game");
        startButton.setBounds(buttonX, getHeight() / 2 + 25, BUTTON_WIDTH, BUTTON_HEIGHT);
        startButton.addActionListener(e -> GameMain.startGame());
        startButton.setVisible(false);
        add(startButton);

        backButton = new JButton("Back");
        backButton.setBounds(buttonX, getHeight() / 2 + 125, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.addActionListener(e -> goToMainMenu());
        backButton.setVisible(false);
        add(backButton);
    }

    private void goToGameModeSelection() {
        setVisibleMainMenu(false);
        setVisibleGameModeSelection(true);
    }

    private void goToMainMenu() {
        setVisibleGameModeSelection(false);
        setVisibleMainMenu(true);
    }

    private void setVisibleMainMenu(boolean b) {
        playButton.setVisible(b);
        exitButton.setVisible(b);
    }

    private void setVisibleGameModeSelection(boolean b) {
        gameModeSelection.setVisible(b);
        startButton.setVisible(b);
        backButton.setVisible(b);
    }

}
