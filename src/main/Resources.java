package main;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;

public class Resources {

    static BufferedImage windowIcon;
    public static BufferedImage tileStart, tileFinish, tileCheckpoint, tileRoad, tileGrass, tileWater, tileWall, tileSand;
    public static BufferedImage car, crosshair;

    private static int numberOfErrors = 0;

    static void load() {
        windowIcon = loadImage("carIcon.png");
        tileStart = loadImage("tileStart.png");
        tileFinish = loadImage("tileFinish.png");
        tileCheckpoint = loadImage("tileCheckpoint.png");
        tileRoad = loadImage("tileRoad.png");
        tileGrass = loadImage("tileGrass.png");
        tileWater = loadImage("tileWater.png");
        tileWall = loadImage("tileWall.png");
        tileSand = loadImage("tileSand.png");
        car = loadImage("car.png");
        crosshair = loadImage("crosshair.png");

        if (numberOfErrors == 0) {
            System.out.println("Resources loaded successfully");
        } else if (numberOfErrors == 1) {
            System.out.printf("%d error occurred while loading resources\n", numberOfErrors);
            System.out.printf("The game might not work properly.\n\n");
        } else {
            System.out.printf("%d errors occurred while loading resources\n", numberOfErrors);
            System.out.printf("The game might not work properly.\n\n");
        }
    }

    private static BufferedImage loadImage(String fileName) {
        BufferedImage image = null;
        try {
            image = ImageIO.read(Resources.class.getResourceAsStream("/resources/" + fileName));
        } catch (IOException e) {
            System.out.println("Error while loading " + fileName);
            e.printStackTrace();
            numberOfErrors++;
        } catch (IllegalArgumentException e) {
            System.out.println("Error while loading " + fileName);
            e.printStackTrace();
            numberOfErrors++;
        }
        return image;
    }

}
