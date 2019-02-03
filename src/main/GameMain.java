package main;

import javax.swing.*;

public class GameMain {

    private static final String GAME_TITLE = "Racetrack";
    private static final int GAME_WIDTH = 512;
    private static final int GAME_HEIGHT = 512;

    private static JFrame frame;

    public static void main(String[] args) {
        Resources.load();
        frame = new JFrame(GAME_TITLE);
        createWindow(frame);
        Menu menu = new Menu(GAME_WIDTH, GAME_HEIGHT);
        frame.add(menu);
        frame.revalidate();
    }

    private static void createWindow(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setIconImage(Resources.windowIcon);
        frame.setVisible(true);
    }

    static void startGame() {
        System.out.println("Game Started");
    }

    static int getGameWidth() {
        return GAME_WIDTH;
    }

    static int getGameHeight() {
        return GAME_HEIGHT;
    }

}
