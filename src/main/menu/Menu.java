package main.menu;

import main.Main;
import resources.ai.DriverAI;
import util.AICompiler;
import util.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.NumberFormatter;
import java.awt.*;
import java.io.File;
import java.text.NumberFormat;
import java.util.ArrayList;

public class Menu extends JPanel {
    private final int DEFAULT_IPAD = 5;

    private Font fontBig, fontSmall;
    private JLabel racetrack, gmSelection, aiSettings;
    private JButton playButton, exitButton;
    private JButton startButton, backButton;
    private JFormattedTextField cars;
    private JTextField map;
    private JLabel carsLabel, mapLabel;
    private JPanel carsPanel, mapPanel;
    private JButton minus, plus, mapButton;
    private JLabel nOfAIs;
    private JButton aiButton;
    private JScrollPane aiScrollPane;
    private JPanel aiMainPanel;
    private ArrayList<AIPanel> aiPanels;
    private int aiCount;
    private JPanel aiButtonPanel;
    private JButton aiAdd, aiBack;

    public Menu(int width, int height) {
        fontBig = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        fontSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        initMenu(width, height);
        initMenuSelection();
        initGameModeSelection();
        initAISettings();

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
        racetrack.setVisible(true);
        racetrack.setForeground(Color.orange);
        racetrack.setFont(fontBig);
        c.gridy = 0;
        add(racetrack, c);

        playButton = new JButton("Play");
        playButton.setVisible(true);
        playButton.addActionListener(e -> goToGameModeSelection());
        c.gridy = 1;
        add(playButton, c);

        exitButton = new JButton("Exit");
        exitButton.setVisible(true);
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
        startButton.addActionListener(e -> Main.startGame());
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
        map.setPreferredSize(new Dimension(128, 21));
        mapPanel.add(map);

        mapButton = new JButton(new ImageIcon(Resources.fileManagerIcon.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        mapButton.setVisible(true);
        mapButton.addActionListener(e -> mapFileManager());
        mapButton.setPreferredSize(buttonSize);
        mapPanel.add(mapButton);

        c.gridy = 3;

        nOfAIs = new JLabel("Number of AIs: 0");
        nOfAIs.setVisible(false);
        nOfAIs.setBackground(Color.black);
        nOfAIs.setForeground(Color.cyan);
        nOfAIs.setFont(fontSmall);
        add(nOfAIs, c);

        aiButton = new JButton("AI settings");
        aiButton.setVisible(false);
        aiButton.addActionListener(e -> goToAISettings());
        c.gridwidth = 2;
        add(aiButton, c);
        c.gridwidth = 1;
    }

    @SuppressWarnings("Duplicates")
    private void initAISettings() {
        GridBagConstraints c = new GridBagConstraints();
        c.weighty = 1;
        c.ipadx = DEFAULT_IPAD;
        c.ipady = DEFAULT_IPAD;

        c.gridy = 0;
        c.weighty = 1;

        aiSettings = new JLabel("AI settings");
        aiSettings.setVisible(false);
        aiSettings.setForeground(Color.orange);
        aiSettings.setFont(fontBig);
        add(aiSettings, c);

        c.gridy = 2;
        c.weighty = 1;

        aiButtonPanel = new JPanel();
        aiButtonPanel.setVisible(false);
        aiButtonPanel.setLayout(new GridBagLayout());
        GridBagConstraints d = new GridBagConstraints();
        d.ipadx = 35;
        d.insets = new Insets(0,17, 0, 17);
        aiButtonPanel.setBackground(Color.black);
        aiButtonPanel.setMinimumSize(new Dimension(256, aiButtonPanel.getMinimumSize().height));
        add(aiButtonPanel, c);

        d.gridx = 0;
        aiAdd = new JButton("Add AI");
        aiAdd.setVisible(true);
        aiAdd.addActionListener(e -> aiFileManager());
        aiButtonPanel.add(aiAdd, d);

        d.gridx = 1;
        aiBack = new JButton("Submit");
        aiBack.setVisible(true);
        aiBack.addActionListener(e -> goToGameModeSelection());
        aiButtonPanel.add(aiBack, d);

        c.gridy = 1;
        c.weighty = 5;
        c.fill = GridBagConstraints.BOTH;

        aiPanels = new ArrayList<>();
        aiCount = 0;

        aiMainPanel = new JPanel();
        aiMainPanel.setLayout(new BoxLayout(aiMainPanel, BoxLayout.Y_AXIS));
        aiMainPanel.setBackground(Color.gray);
        aiScrollPane = new JScrollPane(aiMainPanel);
        aiScrollPane.setVisible(false);
        aiScrollPane.setBackground(Color.black);
        aiScrollPane.setMinimumSize(new Dimension(256, aiScrollPane.getMinimumSize().height));
        aiScrollPane.setPreferredSize(new Dimension(256, aiScrollPane.getPreferredSize().height));
        add(aiScrollPane, c);
    }



    private void goToGameModeSelection() {
        nOfAIs.setText("Number of AIs: " + aiPanels.size());
        setVisibleMainMenu(false);
        setVisibleAISettings(false);
        setVisibleGameModeSelection(true);
    }

    private void goToMainMenu() {
        setVisibleGameModeSelection(false);
        setVisibleAISettings(false);
        setVisibleMainMenu(true);
    }

    private void goToAISettings() {
        setVisibleMainMenu(false);
        setVisibleGameModeSelection(false);
        setVisibleAISettings(true);
    }

    private void setVisibleMainMenu(boolean b) {
        racetrack.setVisible(b);
        playButton.setVisible(b);
        exitButton.setVisible(b);
    }

    private void setVisibleGameModeSelection(boolean b) {
        gmSelection.setVisible(b);
        carsLabel.setVisible(b);
        mapLabel.setVisible(b);
        carsPanel.setVisible(b);
        mapPanel.setVisible(b);
        nOfAIs.setVisible(b);
        aiButton.setVisible(b);
        startButton.setVisible(b);
        backButton.setVisible(b);
    }

    private void setVisibleAISettings(boolean b) {
        aiSettings.setVisible(b);
        aiScrollPane.setVisible(b);
        aiButtonPanel.setVisible(b);
    }

    private void changeCars(int d) {
        cars.setValue(Integer.parseInt(cars.getText()) + d);
    }

    @SuppressWarnings("Duplicates")
    private void mapFileManager() {
        JFileChooser jfc = new JFileChooser("./src/resources/maps");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".tmx", "tmx");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            map.setText(jfc.getSelectedFile().getName());
        }
    }

    @SuppressWarnings("Duplicates")
    private void aiFileManager() {
        JFileChooser jfc = new JFileChooser("./src/resources/ai");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".java", "java");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            addAI(jfc.getSelectedFile());
        }
    }

    private void addAI(File aiFile) {
        aiPanels.add(new AIPanel(aiCount, aiFile, this));
        aiMainPanel.add(getAIPanelById(aiCount));
        aiCount++;
        revalidate();
        repaint();
    }

    public void removeAI(int id) {
        aiMainPanel.remove(getAIPanelById(id));
        aiPanels.remove(getAIPanelById(id));
        revalidate();
        repaint();
    }

    private AIPanel getAIPanelById(int id) {
        for (AIPanel ai : aiPanels) {
            if(ai.getID() == id) {
                return ai;
            }
        }
        return null;
    }

    public int getNumberOfCars() {
        return Integer.parseInt(cars.getText());
    }

    public String getMapName() {
        return map.getText();
    }

    public DriverAI[] getAI() {
        // ToDo
        return null;
    }

}
