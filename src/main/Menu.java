package main;

import javax.swing.*;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.text.NumberFormat;

class Menu extends JPanel {
    private final int DEFAULT_IPAD = 5;

    private Font fontBig, fontSmall;
    private JLabel racetrack;
    private JButton playButton, exitButton;
    private JLabel gmSelection;
    private JButton startButton, backButton;
    private JLabel numberOfCars, mapName;
    private JPanel cars;
    private JFormattedTextField nCars;
    private JButton minus, plus;
    private JTextField map;
    private JButton poop;

    Menu(int width, int height) {
        fontBig = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        fontSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        initMenu(width, height);
        initMenuSelection();
        initGameModeSelection();
        setVisibleMainMenu(true);

        System.out.println("Menu initialized");
    }

    private void initMenu(int width, int height) {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
    }

    @SuppressWarnings("Duplicates")
    private void initMenuSelection() {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.ipadx = DEFAULT_IPAD;
        c.ipady = DEFAULT_IPAD;

        racetrack = new JLabel("Racetrack");
        racetrack.setVisible(false);
        racetrack.setForeground(Color.orange);
        racetrack.setFont(fontBig);
        c.gridy = 0;
        add(racetrack, c);

        playButton = new JButton("Play");
        playButton.setVisible(false);
        playButton.addActionListener(e -> goToGameModeSelection());
        c.gridy = 1;
        add(playButton, c);

        exitButton = new JButton("Exit");
        exitButton.setVisible(false);
        exitButton.addActionListener(e -> System.exit(0));
        c.gridy = 2;
        add(exitButton, c);
    }

    @SuppressWarnings("Duplicates")
    private void initGameModeSelection() {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.ipadx = DEFAULT_IPAD;
        c.ipady = DEFAULT_IPAD;

        c.gridy = 0;

        gmSelection = new JLabel("Game Mode Selection");
        gmSelection.setVisible(false);
        gmSelection.setForeground(Color.orange);
        gmSelection.setFont(fontBig);
        c.gridwidth = 2;
        add(gmSelection, c);
        c.gridwidth = 1;

        c.gridy = 4;

        startButton = new JButton("Start Game");
        startButton.setVisible(false);
        startButton.addActionListener(e -> GameMain.startGame());
        add(startButton, c);

        backButton = new JButton("Back");
        backButton.setVisible(false);
        backButton.addActionListener(e -> goToMainMenu());
        add(backButton, c);

        c.gridy = 1;

        numberOfCars = new JLabel("Number of Cars:");
        numberOfCars.setVisible(false);
        numberOfCars.setForeground(Color.white);
        numberOfCars.setFont(fontSmall);
        add(numberOfCars, c);

        cars = new JPanel();
        cars.setVisible(false);
        cars.setLayout(new FlowLayout());
        cars.setBackground(Color.black);
        add(cars, c);

        minus = new JButton("-");
        minus.setVisible(false);
        minus.addActionListener(e -> changeCars(-1));
        cars.add(minus);

        NumberFormatter nf = new NumberFormatter(NumberFormat.getIntegerInstance());
        nf.setAllowsInvalid(false);
        nCars = new JFormattedTextField(nf);
        nCars.setVisible(false);
        nCars.setValue(1);
        nCars.setBackground(Color.black);
        nCars.setForeground(Color.white);
        cars.add(nCars);

        plus = new JButton("+");
        plus.setVisible(false);
        plus.addActionListener(e -> changeCars(1));
        cars.add(plus);

        c.gridy = 2;

        mapName = new JLabel("Map:");
        mapName.setVisible(false);
        mapName.setForeground(Color.white);
        mapName.setFont(fontSmall);
        add(mapName, c);

        map = new JTextField("Map01");
        map.setVisible(false);
        c.ipadx = 50;
        add(map, c);
        c.ipadx = DEFAULT_IPAD;

        c.gridy = 3;

        poop = new JButton("poop");
        poop.setVisible(false);
        poop.addActionListener(e -> compile());
        c.gridwidth = 2;
        add(poop, c);
        c.gridwidth = 1;
    }



    private void compile() {
        // zatim nic
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
        racetrack.setVisible(b);
        playButton.setVisible(b);
        exitButton.setVisible(b);
    }

    private void setVisibleGameModeSelection(boolean b) {
        gmSelection.setVisible(b);
        startButton.setVisible(b);
        backButton.setVisible(b);
        numberOfCars.setVisible(b);
        nCars.setVisible(b);
        mapName.setVisible(b);
        map.setVisible(b);
        poop.setVisible(b);
        minus.setVisible(b);
        plus.setVisible(b);
        cars.setVisible(b);
    }

    private void changeCars(int d) {
        nCars.setValue(Integer.parseInt(nCars.getText()) + d);
    }

    int  getNumberOfCars() {
        return Integer.parseInt(nCars.getText());
    }

    String getMapName() {
        return map.getText() + ".tmx";
    }

}
