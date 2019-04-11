package model;

/**
 * Interface for writing your own AI for this game.
 */
public interface DriverAI {

    /**
     * Should return vector of acceleration of the car with two coordinates with values -1 or 0 or 1.
     * @param carCoordinates
     * @param carVelocity
     * @param map
     * @return
     */
    int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map);

    /**
     * Is called once before the first turn. Does not have to be used.
     * @param map
     */
    void init(Tile[][] map);

}
