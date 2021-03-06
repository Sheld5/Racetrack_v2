package main;

import javax.swing.*;
import java.awt.*;

/**
 * Contains post-game info about one car and how has it placed in the race.
 */
public class ScorePanel extends JPanel {

    /**
     * Displays how has the player placed in the race.
     */
    private JLabel place;
    /**
     * Displays the name of the player.
     */
    private JLabel playerName;
    /**
     * Displays the name of the AI if there was one controlling the car.
     */
    private JLabel aiName;
    /**
     * Displays the number of turns it took to the player to finish the race.
     */
    private JLabel turnCount;

    /**
     * ScorePanel constructor. Initializes the Swing components of the ScorePanel.
     * @param place the place on which the car whose information this ScorePanel shows has finished.
     * @param playerName the player name of the player driving the car whose information this ScorePanel shows.
     * @param aiName the name of the AI which has been driving the car.
     * @param turnCount the turn on which has the car finished.
     */
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

        if (aiName == "HUMAN") {
            this.aiName = new JLabel("HUMAN");
        } else {
            this.aiName = new JLabel(aiName);
        }
        this.aiName.setForeground(Color.white);
        this.aiName.setHorizontalAlignment(SwingConstants.CENTER);
        this.aiName.setMinimumSize(new Dimension(160, 50));
        this.aiName.setMaximumSize(new Dimension(160, 50));
        add(this.aiName);

        if (place == -1) {
            this.turnCount = new JLabel("DQ");
        } else {
            this.turnCount = new JLabel(Integer.toString(turnCount));
        }
        this.turnCount.setForeground(Color.white);
        this.turnCount.setHorizontalAlignment(SwingConstants.CENTER);
        this.turnCount.setMinimumSize(new Dimension(80, 50));
        this.turnCount.setMaximumSize(new Dimension(80, 50));
        add(this.turnCount);
    }

    /**
     * Transforms the CarPanel into a special CarPanels used as the heading for the carMainPanel.
     */
    public void makeIntoHeading() {
        setBackground(Color.black);
        place.setText("PLACE");
        playerName.setText("PLAYER NAME");
        aiName.setText("AI NAME");
        turnCount.setText("TURNS");
        place.setForeground(Color.orange);
        playerName.setForeground(Color.orange);
        aiName.setForeground(Color.orange);
        turnCount.setForeground(Color.orange);
    }

}
