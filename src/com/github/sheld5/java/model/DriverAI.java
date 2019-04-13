package model;

/**
 * Interface for writing your own AI for this game.
 * Custom implementations of DriverAI have to be added to the /resources/ai directory.
 */
public interface DriverAI {

    /**
     * Is called each turn of the car controlled this AI to determine its next move.
     * Should return a vector of acceleration of the car for the next turn with two coordinates with values -1 or 0 or 1.
     * If the returned object is of an incorrect format, the next move of the car controlled by this AI will be {0,0}.
     * @param carCoordinates the coordinates of the car controlled by this AI.
     * @param carVelocity the velocity vector of the car controlled by this AI.
     * @param map the map on which are the cars racing.
     * @return the acceleration vector for the car for the next turn.
     */
    int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map);

    /**
     * Is called once before the first turn. Does not have to be used.
     * @param map the map on which are the cars going to be racing.
     */
    void init(Tile[][] map);

}
