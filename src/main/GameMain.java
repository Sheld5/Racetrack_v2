package main;

import model.DriverAI;
import model.TestAI;

import javax.swing.*;

public class GameMain {

    private static final String GAME_TITLE = "Racetrack";
    private static final int GAME_WIDTH = 512;
    private static final int GAME_HEIGHT = 512;

    private static JFrame frame;
    private static Menu menu;

    public static void main(String[] args) {
        Resources.load();
        frame = new JFrame(GAME_TITLE);
        initFrame(frame);
        menu = new Menu(GAME_WIDTH, GAME_HEIGHT);
        menu.setVisible(true);
        frame.add(menu);
        frame.revalidate();
    }

    private static void initFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setIconImage(Resources.windowIcon);
        frame.setVisible(true);
    }

    static void startGame() {
        DriverAI[] drivers = new DriverAI[] {new TestAI()};
        Game game = new Game(GAME_WIDTH, GAME_HEIGHT, 1, null);
        frame.add(game);
        menu.setVisible(false);
        game.setVisible(true);
        frame.revalidate();
    }

}
