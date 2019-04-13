package util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

/**
 * Loads all image resources for the game and saves them as BufferedImage instances.
 */
public class Resources {

    /**
     * The icon used in the head of the game frame.
     */
    public static BufferedImage windowIcon;
    /**
     * The textures used to display tiles.
     */
    public static BufferedImage tileStart, tileFinish, tileCheckpoint, tileRoad, tileGrass, tileWater, tileWall, tileSand, tileIce;
    /**
     * The textures of game components.
     */
    public static BufferedImage carRed, carYellow, carBlue, carGreen, carSunk, crosshair, crosshairRed, one, two, three;

    /**
     * The number of errors encountered while loading the resources.
     */
    private static int numberOfErrors = 0;

    /**
     * Calls loadImage() method for each image.
     * @see Resources#loadImage(String)
     */
    public static void load() {
        windowIcon = loadImage("carIcon.png");

        tileStart = loadImage("tileStart.png");
        tileFinish = loadImage("tileFinish.png");
        tileCheckpoint = loadImage("tileCheckpoint.png");
        tileRoad = loadImage("tileRoad.png");
        tileGrass = loadImage("tileGrass.png");
        tileWater = loadImage("tileWater.png");
        tileWall = loadImage("tileWall.png");
        tileSand = loadImage("tileSand.png");
        tileIce = loadImage("tileIce.png");

        carRed = loadImage("carRed.png");
        carYellow = loadImage("carYellow.png");
        carBlue = loadImage("carBlue.png");
        carGreen = loadImage("carGreen.png");
        carSunk = loadImage("carSunk.png");
        crosshair = loadImage("crosshair.png");
        crosshairRed = loadImage("crosshairRed.png");
        one = loadImage("one.png");
        two = loadImage("two.png");
        three = loadImage("three.png");

        if (numberOfErrors == 0) {
            System.out.println("Resources loaded successfully");
        } else if (numberOfErrors == 1) {
            System.out.printf("%d error occurred while loading resources\n", numberOfErrors);
            System.out.println("The game might not work properly.");
        } else {
            System.out.printf("%d errors occurred while loading resources\n", numberOfErrors);
            System.out.println("The game might not work properly.");
        }
    }

    /**
     * Creates a BufferedImage from a ResourceStream from the image file.
     * @param fileName the name of the image file.
     * @return the BufferedImage created from the image file.
     */
    private static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Resources.class.getResourceAsStream("/images/" + fileName));
        } catch (IOException e) {
            System.out.println("Error while loading " + fileName);
            e.printStackTrace();
            numberOfErrors++;
        }
        return image;
    }

}
