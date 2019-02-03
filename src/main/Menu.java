package main;

import javax.swing.*;
import java.awt.*;

class Menu extends JPanel {

    private final int BUTTON_WIDTH = 100;
    private final int BUTTON_HEIGHT = 50;
    private final int SELECTION_X = (GameMain.getGameWidth() / 2) - (BUTTON_WIDTH / 2);

    private JButton playButton, exitButton, pvpButton, pvaiButton, aivaiButton, backButton;

    Menu() {
        init();
        initSelection();
        setVisible(true);
        System.out.println("Menu initialized");
    }

    private void init() {
        setSize(GameMain.getGameWidth(), GameMain.getGameHeight());
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initSelection() {
        initPlayButton();
        initExitButton();
        initPVPButton();
        initPVAIButton();
        initAIVAIButton();
        initBackButton();
    }

    private void initPlayButton() {
        playButton = new JButton("Play");
        playButton.setBounds(SELECTION_X, 50, BUTTON_WIDTH, BUTTON_HEIGHT);
        playButton.addActionListener(e -> goToGameModeSelection());
        add(playButton);
        setVisible(true);
    }

    private void initExitButton() {
        exitButton = new JButton("Exit");
        exitButton.setBounds(SELECTION_X, 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        exitButton.addActionListener(e -> System.exit(0)    );
        add(exitButton);
        setVisible(true);
    }

    private void initPVPButton() {
        pvpButton = new JButton("PVP");
        pvpButton.setBounds(SELECTION_X, 50, BUTTON_WIDTH, BUTTON_HEIGHT);
        pvpButton.addActionListener(e -> System.out.println("PVP button pressed"));
        add(pvpButton);
        pvpButton.setVisible(false);
    }

    private void initPVAIButton() {
        pvaiButton = new JButton("PVAI");
        pvaiButton.setBounds(SELECTION_X, 150, BUTTON_WIDTH, BUTTON_HEIGHT);
        pvaiButton.addActionListener(e -> System.out.println("PVAI button pressed"));
        add(pvaiButton);
        pvaiButton.setVisible(false);
    }

    private void initAIVAIButton() {
        aivaiButton = new JButton("AIVAI");
        aivaiButton.setBounds(SELECTION_X, 250, BUTTON_WIDTH, BUTTON_HEIGHT);
        aivaiButton.addActionListener(e -> System.out.println("AIVAI button pressed"));
        add(aivaiButton);
        aivaiButton.setVisible(false);
    }

    private void initBackButton() {
        backButton = new JButton("Back");
        backButton.setBounds(SELECTION_X, 350, BUTTON_WIDTH, BUTTON_HEIGHT);
        backButton.addActionListener(e -> goToMainMenu());
        add(backButton);
        backButton.setVisible(false);
    }

    private void goToGameModeSelection() {
        playButton.setVisible(false);
        exitButton.setVisible(false);
        pvpButton.setVisible(true);
        pvaiButton.setVisible(true);
        aivaiButton.setVisible(true);
        backButton.setVisible(true);
    }

    private void goToMainMenu() {
        pvpButton.setVisible(false);
        pvaiButton.setVisible(false);
        aivaiButton.setVisible(false);
        backButton.setVisible(false);
        playButton.setVisible(true);
        exitButton.setVisible(true);
    }

}
