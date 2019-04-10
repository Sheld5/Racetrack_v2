package main;

import javax.swing.*;
import java.awt.*;

public class ScorePanel extends JPanel {

    private JLabel place, playerName, aiName, turnCount;
    
    ScorePanel(int place, String playerName, String aiName, int turnCount) {
        setMinimumSize(new Dimension(480, 50));
        setPreferredSize(new Dimension(480, 50));
        setMaximumSize(new Dimension(480, 50));
        setBackground(Color.darkGray);
        setBorder(BorderFactory.createLineBorder(Color.gray));
        setLayout(new BoxLayout(this, BoxLayout.X_AXIS));

        if (place == -1) {
            this.place = new JLabel("DQ");
            this.place.setForeground(Color.red);
        } else {
            this.place = new JLabel(place + ".");
            this.place.setForeground(Color.orange);
        }
        this.place.setHorizontalAlignment(SwingConstants.CENTER);
        this.place.setMinimumSize(new Dimension(80, 50));
        this.place.setMaximumSize(new Dimension(80, 50));
        add(this.place);

        this.playerName = new JLabel(playerName);
        this.playerName.setForeground(Color.white);
        this.playerName.setHorizontalAlignment(SwingConstants.CENTER);
        this.playerName.setMinimumSize(new Dimension(160, 50));
        this.playerName.setMaximumSize(new Dimension(160, 50));
        add(this.playerName);

        if (aiName == null) {
            this.aiName = new JLabel("HUMAN");
        } else {
            this.aiName = new JLabel(aiName);
        }
        this.aiName.setForeground(Color.white);
        this.aiName.setHorizontalAlignment(SwingConstants.CENTER);
        this.aiName.setMinimumSize(new Dimension(160, 50));
        this.aiName.setMaximumSize(new Dimension(160, 50));
        add(this.aiName);

        this.turnCount = new JLabel(Integer.toString(turnCount));
        this.turnCount.setForeground(Color.white);
        this.turnCount.setHorizontalAlignment(SwingConstants.CENTER);
        this.turnCount.setMinimumSize(new Dimension(40, 50));
        this.turnCount.setMaximumSize(new Dimension(40, 50));
        add(this.turnCount);
    }

}
