package main;

import util.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.util.ArrayList;

class Menu extends JPanel {
    private final int DEFAULT_IPAD = 5;

    private Font fontBig, fontSmall;
    private JLabel racetrack, gmSelection;
    private JButton playButton, exitButton;
    private JButton startButton, backButton;
    private JLabel mapLabel;
    private JPanel mapPanel;
    private JTextField map;
    private JButton mapButton;
    private JScrollPane carScrollPane;
    private JPanel carMainPanel;
    private ArrayList<CarPanel> carPanels;
    private int carCount;
    private JButton addCar;

    Menu() {
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        fontBig = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        fontSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        initMenuSelection();
        initGameModeSelection();

        System.out.println("Menu initialized");
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
        Dimension buttonSize = new Dimension(42,21);
        GridBagConstraints c = new GridBagConstraints();
        c.ipadx = DEFAULT_IPAD;
        c.ipady = DEFAULT_IPAD;
        c.weighty = 1;

        c.gridy = 0;

        gmSelection = new JLabel("Game Settings");
        gmSelection.setVisible(false);
        gmSelection.setForeground(Color.orange);
        gmSelection.setFont(fontBig);
        c.gridwidth = 2;
        add(gmSelection, c);
        c.gridwidth = 1;

        c.gridy = 1;

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

        startButton = new JButton("Start Game");
        startButton.setVisible(false);
        startButton.addActionListener(e -> Main.startGame());
        add(startButton, c);

        backButton = new JButton("Back");
        backButton.setVisible(false);
        backButton.addActionListener(e -> goToMainMenu());
        add(backButton, c);

        c.gridy = 2;
        c.gridwidth = 2;
        c.weighty = 5;
        c.fill = GridBagConstraints.BOTH;

        carPanels = new ArrayList<>();
        carCount = 0;

        carMainPanel = new JPanel();
        carMainPanel.setLayout(new BoxLayout(carMainPanel, BoxLayout.Y_AXIS));
        carMainPanel.setBackground(Color.gray);
        carScrollPane = new JScrollPane(carMainPanel);
        carScrollPane.setVisible(false);
        carScrollPane.setBackground(Color.black);
        carScrollPane.setMinimumSize(new Dimension(256, carScrollPane.getMinimumSize().height));
        carScrollPane.setPreferredSize(new Dimension(512, carScrollPane.getPreferredSize().height));
        add(carScrollPane, c);

        addCar = new JButton("Add car");
        addCar.setVisible(true);
        addCar.addActionListener(e -> addCar());
        carMainPanel.add(addCar);

        addCar();
    }



    private void goToGameModeSelection() {
        setVisibleGameModeSelection(true);
        setVisibleMainMenu(false);
    }

    private void goToMainMenu() {
        setVisibleMainMenu(true);
        setVisibleGameModeSelection(false);
    }

    private void setVisibleMainMenu(boolean b) {
        racetrack.setVisible(b);
        playButton.setVisible(b);
        exitButton.setVisible(b);
    }

    private void setVisibleGameModeSelection(boolean b) {
        gmSelection.setVisible(b);
        mapLabel.setVisible(b);
        mapPanel.setVisible(b);
        startButton.setVisible(b);
        backButton.setVisible(b);
        carScrollPane.setVisible(b);
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

    private void addCar() {
        carPanels.add(new CarPanel(carCount, this));
        carMainPanel.add(getCarPanelById(carCount));
        carCount++;
        carMainPanel.add(addCar);
        revalidate();
        repaint();
    }

    void removeCar(int id) {
        if (carPanels.size() > 1) {
            carMainPanel.remove(getCarPanelById(id));
            carPanels.remove(getCarPanelById(id));
            revalidate();
            repaint();
        }
    }

    private CarPanel getCarPanelById(int id) {
        for (CarPanel car : carPanels) {
            if(car.getID() == id) {
                return car;
            }
        }
        return null;
    }

    String getMapName() {
        return map.getText();
    }

    ArrayList<CarPanel> getCarPanels() {
        return carPanels;
    }

}
