package resources.ai;

import model.Tile;

public interface DriverAI {

    // Should return vector of acceleration of the car with two coordinates with values -1 or 0 or 1.
    int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map);

}
