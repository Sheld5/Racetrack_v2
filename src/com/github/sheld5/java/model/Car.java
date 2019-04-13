package model;

import main.Game;
import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Represents a car in the game. Manages all information about the car and its driver.
 */
public class Car extends JPanel {
    /**
     * How many turns are to be skipped as punishment for crashing into a wall (or the side of the map).
     */
    private static int TURNS_SKIPPED_ON_CRASH = 3;

    /**
     * Used to describe the color of the car.
     */
    public enum Color {
        RED, YELLOW, BLUE, GREEN
    }

    /**
     * The game of which is the car component.
     */
    private Game game;
    /**
     * The name of the player controlling this car.
     */
    private String playerName;
    /**
     * The name of the AI controlling this car if there is one.
     */
    private String aiName;
    /**
     * The color of the car.
     */
    private Color color;
    /**
     * The AI controlling the car if there is one.
     */
    private DriverAI driver;
    /**
     * The coordinates at which is the car currently located.
     */
    private int[] coordinates;
    /**
     * The vector representing the current velocity of the car.
     */
    private int[] velocity;
    /**
     * Is true if the car has crashed and has to skip turns as punishment.
     */
    private boolean crashed;
    /**
     * Is true if the car has sunk into water and is no longer able to continue the race.
     */
    private boolean sunk;
    /**
     * Is true if the car has finished the race.
     */
    private boolean finished;
    /**
     * Stores the value of how many turn has the car to wait before continuing the race because it has crashed.
     */
    private int crashCountdown;

    /**
     * The Car class constructor. Initializes the attributes of the car.
     * @param playerName the name of the player whose this car is.
     * @param aiName the name of the AI driving this car. (NULL if human is driving the car.)
     * @param color the color of the car.
     * @param driver the instance of the compiled AI which will be driving this car.
     * @param game instance of Game to which the car will be added.
     * @see DriverAI
     */
    public Car(String playerName, String aiName, Color color, DriverAI driver, Game game) {
        this.playerName = playerName;
        this.aiName = aiName;
        this.game = game;
        this.color = color;
        this.driver = driver;
        coordinates = new int[]{0,0};
        velocity = new int[]{0,0};
        crashCountdown = 0;
        finished = false;
        setSize(game.getTileSize(), game.getTileSize());
        setBackground(new java.awt.Color(0, 0, 0, 0));
    }

    /**
     * Determines which texture should the car use and paints the car.
     * Also paints the numbers representing the waiting time (in turns) after the car crashed.
     * @param g
     */
    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        setSize(game.getTileSize(), game.getTileSize());
        BufferedImage image;
        switch (color) {
            case RED:
                image = Resources.carRed;
                break;
            case YELLOW:
                image = Resources.carYellow;
                break;
            case BLUE:
                image = Resources.carBlue;
                break;
            case GREEN:
                image = Resources.carGreen;
                break;
            default:
                image = Resources.carRed;
        }
        g.drawImage(image.getScaledInstance(game.getTileSize(), game.getTileSize(), Image.SCALE_SMOOTH), 0, 0, null);
        if (crashed) {
            switch (crashCountdown) {
                case 1:
                    image = Resources.one;
                    break;
                case 2:
                    image = Resources.two;
                    break;
                case 3:
                    image = Resources.three;
                    break;
            }
            if (0 < crashCountdown && crashCountdown < 4) {
                g.drawImage(image.getScaledInstance(game.getTileSize() - 6, game.getTileSize() - 6, Image.SCALE_SMOOTH), 3, 3, null);
            }
        }
    }

    /**
     * Sets the coordinates of the car to the given values.
     * @param x the value to which is the X coordinate of the car to be set.
     * @param y the value to which is the Y coordinate of the car to be set.
     */
    public void setCoordinates(int x, int y) {
        coordinates[0] = x;
        coordinates[1] = y;
    }

    /**
     * Sets the coordinates of the car to the given values.
     * @param coordinates the values to which are the coordinates of the car to be set.
     * @throws IllegalArgumentException thrown if the length of the int[] given as the parameter is not 2.
     */
    public void setCoordinates(int[] coordinates) {
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("Coordinates have to be an int array containing two Integers.");
        } else {
            this.coordinates = coordinates;
        }
    }

    /**
     * Changes the velocity vector of the car by adding the acceleration vector given as parameter.
     * @param a the acceleration vector which is to be added to the velocity vector of the car.
     * @throws IllegalArgumentException thrown if the length of the int[] given as parameter is not 2
     *                                  or the values of the int[] are not -1,0 or 1.
     */
    public void accelerate(int[] a) {
        if (a.length != 2) {
            throw new IllegalArgumentException("Coordinates have to be an int array containing two Integers.");
        } else if (a[0] < -1 || a[0] > 1 || a[1] < -1 || a[1] > 1) {
            throw new IllegalArgumentException("Method accelerate only accepts values of {-1;0;1}.");
        } else {
            velocity[0] += a[0];
            velocity[1] += a[1];
        }
    }

    /**
     * Sets the velocity vector of the car to the given values.
     * @param velocity the values to which is the velocity vector of the car to be set.
     * @throws IllegalArgumentException thrown if the length of the int[] given as the parameter is not 2.
     */
    public void setVelocity(int[] velocity) {
        if (velocity.length != 2) {
            throw new IllegalArgumentException("Method setVelocity only accepts an int array with the length of 2 as its argument.");
        } else {
            this.velocity[0] = velocity[0];
            this.velocity[1] = velocity[1];
        }
    }

    /**
     * Returns a deep copy of the coordinates of the car.
     * @return a deep copy of the coordinates of the car.
     */
    public int[] getCoordinates() {
        int[] coordinatesCopy = new int[2];
        coordinatesCopy[0] = coordinates[0];
        coordinatesCopy[1] = coordinates[1];
        return coordinatesCopy;
    }

    /**
     * Returns the X coordinate of the car.
     * @return the X coordinate of the car.
     */
    public int getTileX() {
        return coordinates[0];
    }

    /**
     * Returns the Y coordinate of the car.
     * @return the Y coordinate of the car.
     */
    public int getTileY() {
        return coordinates[1];
    }

    /**
     * Returns a deep copy of the velocity vector of the car.
     * @return a deep copy of the velocity vector of the car.
     */
    public int[] getVelocity() {
        int[] velocityCopy = new int[2];
        velocityCopy[0] = velocity[0];
        velocityCopy[1] = velocity[1];
        return velocityCopy;
    }

    /**
     * Returns the X coordinate of the velocity vector of the car.
     * @return the X coordinate of the velocity vector of the car.
     */
    public int getVelX() {
        return velocity[0];
    }

    /**
     * Returns the Y coordinate of the velocity vector of the car.
     * @return the Y coordinate of the velocity vector of the car.
     */
    public int getVelY() {
        return velocity[1];
    }

    /**
     * Sets the car to "crashed" and start the crash-countdown.
     */
    public void crashed() {
        crashed = true;
        crashCountdown = TURNS_SKIPPED_ON_CRASH;
        game.repaint();
    }

    /**
     * Sets the car to "sunk".
     */
    public void sunk() {
        sunk = true;
        game.repaint();
    }

    /**
     * Sets the car to "finished".
     */
    public void finished() {
        finished = true;
    }

    /**
     * Returns true if the car is crashed.
     * @return true if the car is crashed.
     */
    public boolean isCrashed() {
        return crashed;
    }

    /**
     * Returns true if the car is sunk.
     * @return true if the car is sunk.
     */
    public boolean isSunk() {
        return sunk;
    }

    /**
     * Returns true if the car is finished.
     * @return true if the car is finished.
     */
    public boolean isFinished() {
        return finished;
    }

    /**
     * Decreases crashCountdown by one and sets the car to "not crashed" if the countdown is finished.
     */
    public void countdown() {
        crashCountdown--;
        if (crashCountdown == 0) {
            crashed = false;
        }
        game.repaint();
    }

    /**
     * Returns the instance of AI assigned to drive this car.
     * @return the instance of AI assigned to drive this car.
     */
    public DriverAI getDriver() {
        return driver;
    }

    /**
     * Returns the name of the player this car has been assigned to.
     * @return the name of the player this car has been assigned to.
     */
    public String getPlayerName() {
        return playerName;
    }

    /**
     * Returns the name of the AI assigned to this car.
     * @return the name of the AI assigned to this car.
     */
    public String getAiName() {
        return aiName;
    }

}
