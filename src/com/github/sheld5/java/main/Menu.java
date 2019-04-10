package main;

import util.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

class Menu extends JPanel {
    private final int DEFAULT_IPAD = 5;

    private Font fontBig, fontSmall;
    private JLabel racetrack, gmSelection;
    private JButton playButton, exitButton;
    private JPanel startBackPanel;
    private JButton startButton, backButton;
    private JPanel mapPanel;
    private JLabel mapLabel;
    private JPanel mapFilePanel;
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

    // initializes components of the main menu
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

    // initializes components of the game settings menu
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
        add(gmSelection, c);

        c.gridy = 1;

        mapPanel = new JPanel();
        mapPanel.setVisible(false);
        mapPanel.setLayout(new FlowLayout());
        mapPanel.setBackground(Color.black);
        add(mapPanel, c);

        mapLabel = new JLabel("Map:");
        mapLabel.setForeground(Color.cyan);
        mapLabel.setFont(fontSmall);
        mapPanel.add(mapLabel);

        mapFilePanel = new JPanel();
        mapFilePanel.setLayout(new FlowLayout());
        mapFilePanel.setBackground(Color.black);
        mapPanel.add(mapFilePanel);

        map = new JTextField("Map01.tmx");
        map.setPreferredSize(new Dimension(128, 21));
        mapFilePanel.add(map);

        mapButton = new JButton(new ImageIcon(Resources.fileManagerIcon.getScaledInstance(20, 20, Image.SCALE_SMOOTH)));
        mapButton.addActionListener(e -> mapFileManager());
        mapButton.setPreferredSize(buttonSize);
        mapFilePanel.add(mapButton);

        c.gridy = 3;

        startBackPanel = new JPanel();
        startBackPanel.setVisible(false);
        startBackPanel.setLayout(new FlowLayout());
        startBackPanel.setBackground(Color.black);
        add(startBackPanel, c);

        startButton = new JButton("Start Game");
        startButton.addActionListener(e -> Main.startGame());
        startBackPanel.add(startButton);

        backButton = new JButton("Back");
        backButton.addActionListener(e -> goToMainMenu());
        startBackPanel.add(backButton);

        c.gridy = 2;
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
        addCar.setBorder(BorderFactory.createLineBorder(Color.gray));
        carMainPanel.add(addCar);

        addCar();
    }



    // hides the main menu and show the game settings menu
    private void goToGameModeSelection() {
        setVisibleGameModeSelection(true);
        setVisibleMainMenu(false);
    }

    // hides the game settings menu and shows the main menu
    private void goToMainMenu() {
        setVisibleMainMenu(true);
        setVisibleGameModeSelection(false);
    }

    // sets the visibility of the main menu
    private void setVisibleMainMenu(boolean b) {
        racetrack.setVisible(b);
        playButton.setVisible(b);
        exitButton.setVisible(b);
    }

    // sets the visibility of the game settings menu
    private void setVisibleGameModeSelection(boolean b) {
        gmSelection.setVisible(b);
        mapPanel.setVisible(b);
        startBackPanel.setVisible(b);
        carScrollPane.setVisible(b);
    }

    // initializes FileChooser and sets the text of the map selection TextField to the name of the file chosen by the user
    private void mapFileManager() {
        File targetDirectory = new File("./out/production/Racetrack_v2/maps");
        if (!targetDirectory.exists()) {
            targetDirectory = new File(".");
        }
        JFileChooser jfc = new JFileChooser(targetDirectory);
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".tmx", "tmx");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            map.setText(jfc.getSelectedFile().getName());
        }
    }

    // adds a new instance of CarPanel to the main car panel
    private void addCar() {
        carPanels.add(new CarPanel(carCount, this));
        carMainPanel.add(getCarPanelById(carCount));
        carCount++;
        carMainPanel.add(addCar);
        revalidate();
        repaint();
    }

    // removes the CarPanel with the correct id from the main car panel
    void removeCar(int id) {
        if (carPanels.size() > 1) {
            carMainPanel.remove(getCarPanelById(id));
            carPanels.remove(getCarPanelById(id));
            revalidate();
            repaint();
        }
    }

    // returns the CarPanel with the correct id
    private CarPanel getCarPanelById(int id) {
        for (CarPanel car : carPanels) {
            if(car.getID() == id) {
                return car;
            }
        }
        return null;
    }

    //returns the name of the map file previously chosen by the user
    String getMapName() {
        return map.getText();
    }

    // returns and array containing all CarPanel instances
    ArrayList<CarPanel> getCarPanels() {
        return carPanels;
    }

}
