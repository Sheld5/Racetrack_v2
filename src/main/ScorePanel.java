package main;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    private JLabel playerName, aiName, turnCount;

    ScorePanel(String playerName, String aiName, int turnCount) {
        setMinimumSize(new Dimension(480, 50));
        setPreferredSize(new Dimension(480, 50));
        setMaximumSize(new Dimension(480, 50));
        setBackground(Color.darkGray);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        setLayout(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        this.playerName = new JLabel(playerName);
        c.gridx = 0;
        c.weightx = 2;
        add(this.playerName, c);

        this.aiName = new JLabel(aiName);
        c.gridx = 1;
        c.weightx = 2;
        add(this.aiName, c);

        this.turnCount = new JLabel(Integer.toString(turnCount));
        c.gridx = 2;
        c.weightx = 1;
        add(this.turnCount, c);
    }

}
