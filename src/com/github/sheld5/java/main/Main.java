package main;

import org.xml.sax.SAXException;
import util.Resources;
import util.StartNotFoundException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

// initializes the application, switches between menu and game
public class Main {

    private static final String GAME_TITLE = "Racetrack";
    private static final int GAME_WIDTH = 555;
    private static final int GAME_HEIGHT = 555;

    private static JFrame frame;
    private static Menu menu;
    private static Game game;

    public static void main(String[] args) {
        Resources.load();
        initFrame();
        initMenu();
    }

    // initializes JFrame for the application
    private static void initFrame() {
        frame = new JFrame(GAME_TITLE);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(true);
        frame.setSize(GAME_WIDTH, GAME_HEIGHT);
        frame.setIconImage(Resources.windowIcon);
        frame.setVisible(true);
    }

    // initializes menu
    private static void initMenu() {
        menu = new Menu();
        menu.setVisible(true);
        frame.add(menu);
        frame.revalidate();
    }

    // initializes game and hides menu
    static void startGame() {
        try {
            game = new Game(menu);
            game.setVisible(true);
            frame.add(game);
            menu.setVisible(false);
            frame.revalidate();
        } catch (IOException | SAXException | ParserConfigurationException | StartNotFoundException | IllegalArgumentException e) {
            System.out.println("An error occurred. The game could not be initiated.");
            e.printStackTrace();
        }
    }

    // hides game and shows menu
    static void goToMenu() {
        frame.remove(menu);
        frame.add(menu);
        menu.setVisible(true);
        frame.remove(game);
    }

}
