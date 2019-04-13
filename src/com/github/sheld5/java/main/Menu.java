package main;

import util.DataReader;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Manages the menu, all its components and all its functions.
 */
class Menu extends JPanel {
    /**
     * The value to which the padding of GridBagLayouts used in the menu is set.
     */
    private final int DEFAULT_IPAD = 5;

    /**
     * Font used in the menu.
     */
    private Font fontBig, fontSmall;
    /**
     * Heading of the menu.
     */
    private JLabel racetrack, gmSelection;
    /**
     * Button used to go the the pre-game settings menu.
     */
    private JButton playButton;
    /**
     * Button used to exit the game.
     */
    private JButton exitButton;
    /**
     * Contains startButton and backButton.
     */
    private JPanel startBackPanel;
    /**
     * Button used to start the game after choosing the settings.
     */
    private JButton startButton;
    /**
     * Button used to go back to the main menu.
     */
    private JButton backButton;
    /**
     * Contains the GUI for changing the map settings.
     */
    private JPanel mapPanel;
    /**
     * Marks the map settings.
     */
    private JLabel mapLabel;
    /**
     * Combo-box used to select the map.
     */
    private JComboBox mapSelector;
    /**
     * Contains instances of CarPanel representing the cars.
     */
    private JPanel carMainPanel;
    /**
     * Used to be able to scroll in the carMainPanel.
     */
    private JScrollPane carScrollPane;
    /**
     * The array containing all CarPanels.
     */
    private ArrayList<CarPanel> carPanels;
    /**
     * The current number of CarPanels.
     */
    private int carCount;
    /**
     * Button used to add a CarPanel to the carMainPanel.
     */
    private JButton addCar;
    /**
     * Used to store the tile-size last used in the previous game.
     */
    private int lastGameTileSize;

    /**
     * Menu constructor. Initializes the main panel (background etc). Calls other init-methods.
     * @see Menu#initMenuSelection()
     * @see Menu#initGameModeSelection()
     */
    Menu() {
        lastGameTileSize = 24;
        setBackground(Color.BLACK);
        setLayout(new GridBagLayout());
        fontBig = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        fontSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        initMenuSelection();
        initGameModeSelection();

        System.out.println("Menu initialized");
    }

    /**
     * Initializes the Swing components of the main menu.
     */
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

    /**
     * Initializes the Swing components of the game settings menu.
     * Uses the getListOfFiles() method of the DataReader to get the list of map files.
     * @see DataReader#getListOfFiles(String)
     */
    @SuppressWarnings("Duplicates")
    private void initGameModeSelection() {
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

        DataReader dr = new DataReader();
        mapSelector = new JComboBox(dr.getListOfFiles("/META-INF/maps.txt"));
        mapSelector.setPreferredSize(new Dimension(150, 25));
        mapPanel.add(mapSelector);

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

        addCar = new JButton("Add Player");
        addCar.setVisible(true);
        addCar.addActionListener(e -> addCar());
        addCar.setBorder(BorderFactory.createLineBorder(Color.gray));
        carMainPanel.add(addCar);

        addCar();
    }

    /**
     * Hides the main menu and shows the game settings menu.
     * @see Menu#setVisibleGameModeSelection(boolean)
     * @see Menu#setVisibleMainMenu(boolean)
     */
    private void goToGameModeSelection() {
        setVisibleGameModeSelection(true);
        setVisibleMainMenu(false);
    }

    /**
     * Hides the game settings menu and shows the main menu.
     * @see Menu#setVisibleMainMenu(boolean)
     * @see Menu#setVisibleGameModeSelection(boolean)
     */
    private void goToMainMenu() {
        setVisibleMainMenu(true);
        setVisibleGameModeSelection(false);
    }

    /**
     * Sets the visibility of the main menu to the value of the given parameter.
     * @param b the parameter to which visibilities of all components of the main menu are to be set.
     */
    private void setVisibleMainMenu(boolean b) {
        racetrack.setVisible(b);
        playButton.setVisible(b);
        exitButton.setVisible(b);
    }

    /**
     * Sets the visibility of the game settings menu to the value of the given parameter.
     * @param b the parameter to which visibilities of all components of the game settings menu are to be set.
     */
    private void setVisibleGameModeSelection(boolean b) {
        gmSelection.setVisible(b);
        mapPanel.setVisible(b);
        startBackPanel.setVisible(b);
        carScrollPane.setVisible(b);
    }

    /**
     * Adds a new instance of CarPanel to the main car panel.
     */
    private void addCar() {
        carPanels.add(new CarPanel(carCount, this));
        carMainPanel.add(getCarPanelById(carCount));
        carCount++;
        carMainPanel.add(addCar);
        revalidate();
        repaint();
    }

    /**
     * Removes the CarPanel with the given id from the JPanel mainCarPanel and from the carPanels array.
     * @param id the id of the CarPanel which is to be removed.
     */
    void removeCar(int id) {
        if (carPanels.size() > 1) {
            carMainPanel.remove(getCarPanelById(id));
            carPanels.remove(getCarPanelById(id));
            revalidate();
            repaint();
        }
    }

    /**
     * Returns the CarPanel with the given id.
     * @param id the id of the CarPanel which is to be returned.
     * @return the CarPanel with the id given as the parameter.
     */
    private CarPanel getCarPanelById(int id) {
        for (CarPanel car : carPanels) {
            if(car.getID() == id) {
                return car;
            }
        }
        return null;
    }

    /**
     * Returns the name of the map file chosen by the user.
     * @return the name of the map file chosen by the user.
     */
    String getMapName() {
        return (String)mapSelector.getSelectedItem();
    }

    /**
     * Returns an array containing all CarPanel instances of this menu.
     * @return the array containing all CarPanel instances of this menu.
     */
    ArrayList<CarPanel> getCarPanels() {
        return carPanels;
    }

    /**
     * Saves the tile-size (corresponds to the zoom-level) last used in the game before leaving back to the menu.
     * @param lastTileSize the tile-size to be saved as the last tile-size used in the game.
     */
    void setLastGameTileSize(int lastTileSize) {
        lastGameTileSize = lastTileSize;
    }

    /**
     * Returns the tile-size last used in the previous game before leaving.
     * @return the tile-size last used in the previous game before leaving.
     */
    int getLastGameTileSize() {
        return lastGameTileSize;
    }

}
