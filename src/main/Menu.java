package main;

import javax.swing.*;
import java.awt.*;

class Menu extends JPanel {

    private int width, height;
    private JButton playButton, exitButton;
    private JLabel gameModeSelection;
    private JButton startButton, backButton;

    Menu(int width, int height) {
        this.width = width;
        this.height = height;
        init();
        initMenuSelection();
        initGameModeSelection();
        System.out.println("Menu initialized");
    }

    private void init() {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initMenuSelection() {
        int buttonWidth = 100;
        int buttonHeight = 50;
        int selectionX = width / 2 - buttonWidth / 2;
        initPlayButton(selectionX, buttonWidth, buttonHeight);
        initExitButton(selectionX, buttonWidth, buttonHeight);
    }

    private void initPlayButton(int x, int width, int height) {
        playButton = new JButton("Play");
        playButton.setBounds(x , 50, width, height);
        playButton.addActionListener(e -> goToGameModeSelection());
        playButton.setVisible(true);
        add(playButton);
    }

    private void initExitButton(int x, int width, int height) {
        exitButton = new JButton("Exit");
        exitButton.setBounds(x , 150, width, height);
        exitButton.addActionListener(e -> System.exit(0));
        exitButton.setVisible(true);
        add(exitButton);
    }

    private void initGameModeSelection() {
        gameModeSelection = new JLabel("Game Mode Selection");
        gameModeSelection.setBounds(width / 2 - 125, height / 2 - 225, 250, 50);
        gameModeSelection.setVisible(false);
        add(gameModeSelection);

        startButton = new JButton("Start Game");
        startButton.setBounds(width / 2 - 50, height / 2 + 25, 100, 50);
        startButton.addActionListener(e -> GameMain.startGame());
        startButton.setVisible(false);
        add(startButton);

        backButton = new JButton("Back");
        backButton.setBounds(width / 2 - 50, height / 2 + 125, 100, 50);
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
