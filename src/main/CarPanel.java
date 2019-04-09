package main;

import model.Car;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

import java.awt.*;
import java.io.File;

public class CarPanel extends JPanel {
    private static int MAX_NAME_LENGTH = 16;

    private int id;
    private JTextField playerName;
    private JLabel aiNameLabel;
    private JButton carColor, remove, addAI;
    private String aiName;
    private File aiFile;

    CarPanel(int id, Menu menu) {
        this.id = id;
        aiFile = null;
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
        carColor.setPreferredSize(new Dimension(50, 50));
        carColor.addActionListener(e -> changeColor());
        c.gridx = 0;
        c.weightx = 1;
        add(carColor, c);

        playerName = new JTextField("Player name");
        playerName.setPreferredSize(new Dimension((int)playerName.getPreferredSize().getWidth(), 50));
        playerName.setMinimumSize(new Dimension(150, (int)playerName.getMinimumSize().getHeight()));
        playerName.setMaximumSize(new Dimension(150, (int)playerName.getMaximumSize().getHeight()));
        c.weightx = 5;
        c.gridx = 1;
        add(playerName, c);

        aiNameLabel = new JLabel("HUMAN");
        aiNameLabel.setForeground(Color.white);
        aiNameLabel.setPreferredSize(new Dimension(150, 50));
        c.gridx = 2;
        c.weightx = 5;
        add(aiNameLabel, c);

        addAI = new JButton("Add AI");
        addAI.setPreferredSize(new Dimension(100, 50));
        addAI.addActionListener(e -> aiFileManager());
        c.gridx = 3;
        c.weightx = 2;
        add(addAI, c);

        remove = new JButton("x");
        remove.setForeground(Color.red);
        remove.setBackground(Color.gray);
        remove.setPreferredSize(new Dimension(50, 50));
        remove.addActionListener(e -> menu.removeCar(id));
        c.gridx = 4;
        c.weightx = 1;
        add(remove, c);

    }

    private void aiFileManager() {
        JFileChooser jfc = new JFileChooser("./src/resources/ai");
        FileNameExtensionFilter fnef = new FileNameExtensionFilter(".java", "java");
        jfc.setFileFilter(fnef);
        int returnValue = jfc.showOpenDialog(this);
        if (returnValue == JFileChooser.APPROVE_OPTION) {
            aiFile = (jfc.getSelectedFile());
        }
        aiName = aiFile.getName().substring(0, aiFile.getName().length() - 5);
        if (aiName.length() > 16) {
            aiNameLabel.setText(aiName.substring(0, MAX_NAME_LENGTH - 1) + "...");
        } else {
            aiNameLabel.setText(aiName);
        }
        aiNameLabel.setForeground(Color.orange);
    }

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

    int getID() {
        return id;
    }

    public String getAiName() {
        return aiName;
    }

    public File getAiFile() {
        return aiFile;
    }

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

    String getPlayerName() {
        return playerName.getText();
    }

}