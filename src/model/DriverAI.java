package model;

public interface DriverAI {

    // Should return index of the next move ranging from 0 to 8.
    int drive(int carX, int carY, int velX, int velY, Map.Tile[][] map);

}
