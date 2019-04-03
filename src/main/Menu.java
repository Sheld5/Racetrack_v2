package main;

import util.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
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
    private JLabel carsLabel, mapLabel;
    private JPanel carsPanel, mapPanel;
    private JFormattedTextField cars;
    private JButton minus, plus, mapButton;
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

        carsLabel = new JLabel("Number of Cars:");
        carsLabel.setVisible(false);
        carsLabel.setForeground(Color.cyan);
        carsLabel.setFont(fontSmall);
        add(carsLabel, c);

        carsPanel = new JPanel();
        carsPanel.setVisible(false);
        carsPanel.setLayout(new FlowLayout());
        carsPanel.setBackground(Color.black);
        add(carsPanel, c);

        Dimension buttonSize = new Dimension(42,21);

        minus = new JButton("-");
        minus.setVisible(true);
        minus.addActionListener(e -> changeCars(-1));
        minus.setPreferredSize(buttonSize);
        carsPanel.add(minus);

        NumberFormatter nf = new NumberFormatter(NumberFormat.getIntegerInstance());
        nf.setAllowsInvalid(false);
        cars = new JFormattedTextField(nf);
        cars.setVisible(true);
        cars.setValue(1);
        cars.setPreferredSize(buttonSize);
        cars.setBackground(Color.black);
        cars.setForeground(Color.white);
        cars.setHorizontalAlignment(SwingConstants.CENTER);
        carsPanel.add(cars);

        plus = new JButton("+");
        plus.setVisible(true);
        plus.addActionListener(e -> changeCars(1));
        plus.setPreferredSize(buttonSize);
        carsPanel.add(plus);

        c.gridy = 2;

        mapLabel = new JLabel("Map:");
        mapLabel.setVisible(false);
        mapLabel.setForeground(Color.cyan);
        mapLabel.setFont(fontSmall);
        add(mapLabel, c);

        mapPanel = new JPanel();
        mapPanel.setVisible(false);
        mapPanel.setLayout(new FlowLayout());
        mapPanel.setBackground(Color.black);
        add(mapPanel, c);

        map = new JTextField("Map01.tmx");
        map.setVisible(true);
        map.setPreferredSize(new Dimension(84, 21));
        mapPanel.add(map);

        mapButton = new JButton(new ImageIcon(Resources.fileManagerIcon.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        mapButton.setVisible(true);
        mapButton.addActionListener(e -> mapFileManager());
        mapButton.setPreferredSize(buttonSize);
        mapPanel.add(mapButton);

        c.gridy = 3;

        poop = new JButton("poop");
        poop.setVisible(false);
        poop.addActionListener(e -> compile());
        c.gridwidth = 2;
        add(poop, c);
        c.gridwidth = 1;
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
        carsLabel.setVisible(b);
        mapLabel.setVisible(b);
        carsPanel.setVisible(b);
        mapPanel.setVisible(b);
        poop.setVisible(b);
    }

    private void changeCars(int d) {
        cars.setValue(Integer.parseInt(cars.getText()) + d);
    }

    private void mapFileManager() {
        JFileChooser jfc = new JFileChooser("./src/resources/maps");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".tmx", "tmx");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            map.setText(jfc.getSelectedFile().getName());
        }
    }

    private void compile() {
        // zatim nic
    }

    int  getNumberOfCars() {
        return Integer.parseInt(cars.getText());
    }

    String getMapName() {
        return map.getText();
    }

}
