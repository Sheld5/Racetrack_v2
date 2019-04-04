package main;

import main.menu.Menu;
import resources.ai.DriverAI;
import util.Resources;
import util.StartNotFoundException;

import javax.swing.*;
import java.io.FileNotFoundException;

public class Main {

    private static final String GAME_TITLE = "Racetrack";
    private static final int GAME_WIDTH = 555;
    private static final int GAME_HEIGHT = 555;

    private static JFrame frame;
    private static Menu menu;
    private static Game game;

    public static void main(String[] args) {
        Resources.load();
        frame = new JFrame(GAME_TITLE);
        initFrame(frame);
        menu = new Menu(GAME_WIDTH, GAME_HEIGHT);
        menu.setVisible(true);
        frame.add(menu);
        frame.revalidate();
    }

    public static void goToMenu() {
        game.setVisible(false);
        menu.setVisible(true);
    }

    private static void initFrame(JFrame frame) {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setIconImage(Resources.windowIcon);
        frame.setVisible(true);
    }

    public static void startGame() {
        try {
            DriverAI[] drivers = null;
            game = new Game(GAME_WIDTH, GAME_HEIGHT, menu.getNumberOfCars(), drivers, menu.getMapName());
            frame.add(game);
            menu.setVisible(false);
            game.setVisible(true);
            frame.revalidate();
        } catch (FileNotFoundException e) {
            System.out.println("An error occurred. The game could not be initiated.");
        } catch (StartNotFoundException e) {
            System.out.println("An error occurred. The game could not be initiated.");
        }
    }

}
