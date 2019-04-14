package main;

import model.Car;
import util.Resources;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.File;

/**
 *  JPanel extension which contains all settings for a car and GUI to change them.
 */
public class CarPanel extends JPanel {

    /**
     * The ID of the CarPanel.
     */
    private int id;
    /**
     * The text-field for the user to input the name of the player.
     */
    private JTextField playerName;
    /**
     * Button used to cycle through available car colors.
     */
    private JButton carColor;
    /**
     * Button used to remove the CarPanel. (And remove the car by doing so.)
     */
    private JButton remove;
    /**
     * JLabel displaying the name of the AI chosen for the car.
     */
    private JLabel aiLabel;
    /**
     * Used to store the name of the chosen AI.
     */
    private String aiName;
    /**
     * Used to store the chosen AI file.
     */
    private File aiFile;
    /**
     * Button which opens file manager for the user to choose the AI for the car to use.
     */
    private JButton addAI;

    /**
     * CarPanel constructor. Initializes all components of the CarPanel.
     * @param id the id this instance of CarPanel is to have.
     * @param menu the instance of Menu to whose JPanel carMainPanel this instance of CarPanel will be added.
     * @see Menu
     */
    CarPanel(int id, Menu menu) {
        this.id = id;
        aiName = null;

        setMinimumSize(new Dimension(512, 50));
        setPreferredSize(new Dimension(this.getPreferredSize().width, 50));
        setMaximumSize(new Dimension(1024, 50));
        setBackground(Color.darkGray);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        carColor = new JButton("R");
        carColor.setForeground(Color.red);
        carColor.setBackground(Color.gray);
        carColor.setSize(new Dimension(50, 50));
        carColor.addActionListener(e -> changeColor());
        c.gridx = 0;
        c.weightx = 1;
        add(carColor, c);

        playerName = new JTextField("Player name");
        playerName.setPreferredSize(new Dimension((int)playerName.getPreferredSize().getWidth(), 50));
        playerName.setMinimumSize(new Dimension(150, (int)playerName.getMinimumSize().getHeight()));
        playerName.setMaximumSize(new Dimension(150, (int)playerName.getMaximumSize().getHeight()));
        c.weightx = 2;
        c.gridx = 1;
        add(playerName, c);

        aiLabel = new JLabel("HUMAN");
        aiLabel.setForeground(Color.white);
        aiLabel.setMinimumSize(new Dimension(100, 50));
        aiLabel.setMaximumSize(new Dimension(100, 50));
        c.gridx = 3;
        c.weightx = 2;
        add(aiLabel, c);

        addAI = new JButton(new ImageIcon(Resources.folder));
        addAI.setMinimumSize(new Dimension(42, 42));
        addAI.setMaximumSize(new Dimension(42, 42));
        addAI.addActionListener(e -> aiFileManager());
        c.gridx = 4;
        c.weightx = 1;
        add(addAI, c);

        remove = new JButton("x");
        remove.setForeground(Color.red);
        remove.setBackground(Color.gray);
        remove.setSize(new Dimension(32, 32));
        remove.addActionListener(e -> menu.removeCar(id));
        c.gridx = 5;
        c.weightx = 1;
        add(remove, c);

    }

    /**
     * Cycles through the color options for the car
     * and sets the appearance of the color button to match the currently selected color.
     */
    private void changeColor() {
        switch (carColor.getText()) {
            case "R":
                carColor.setText("Y");
                carColor.setForeground(Color.yellow);
                break;
            case "Y":
                carColor.setText("B");
                carColor.setForeground(Color.blue);
                break;
            case "B":
                carColor.setText("G");
                carColor.setForeground(Color.green);
                break;
            case "G":
                carColor.setText("R");
                carColor.setForeground(Color.red);
                break;
        }
    }

    /**
     * Initializes the JFileChooser so the user can choose the AI file.
     */
    private void aiFileManager() {
        JFileChooser jfc = new JFileChooser(".");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".java", "java");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            aiFile = (jfc.getSelectedFile());
            aiName = aiFile.getName().substring(0, aiFile.getName().length() - 5);
            aiLabel.setText(aiName);
            aiLabel.setForeground(Color.green);
        }
    }

    /**
     * Returns the id of this instance of CarPanel.
     * @return the id of this instance of CarPanel.
     */
    int getID() {
        return id;
    }

    /**
     * Returns the name of the chosen AI.
     * @return the name of the chosen AI.
     */
    public String getAiName() {
        return aiName;
    }

    /**
     * Returns the file of the chosen AI.
     * @return the file of the chosen AI.
     */
    public File getAiFile() {
        return aiFile;
    }

    /**
     * Returns the chosen color for the car.
     * @return the chosen color for the car.
     */
    Car.Color getCarColor() {
        switch(carColor.getText()) {
            case "R":
                return Car.Color.RED;
            case "Y":
                return Car.Color.YELLOW;
            case "B":
                return Car.Color.BLUE;
            case "G":
                return Car.Color.GREEN;
            default:
                return Car.Color.RED;
        }
    }

    /**
     * Returns the chosen player name.
     * @return the chosen player name.
     */
    String getPlayerName() {
        return playerName.getText();
    }

}