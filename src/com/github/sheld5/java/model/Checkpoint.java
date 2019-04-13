package model;

/**
 * Represents a checkpoint in the game. Manages all information about the checkpoint.
 */
public class Checkpoint {

    /**
     * Stores coordinates of each tile of the checkpoint.
     */
    private int[][] coordinates;
    /**
     * Each boolean of the array is set to true once the corresponding car has passed the checkpoint.
     */
    private boolean[] carsPassed;

    /**
     * The Checkpoint class constructor.
     * @param x the X coordinate of the first tile to be added to this checkpoint.
     * @param y the Y coordinate of the first tile to be added to this checkpoint.
     * @param numberOfCars the number of cars in the game.
     */
    public Checkpoint(int x, int y, int numberOfCars) {
        coordinates = new int[1][2];
        coordinates[0][0] = x;
        coordinates[0][1] = y;

        carsPassed = new boolean[numberOfCars];
        for (boolean b : carsPassed) b = false;
    }

    /**
     * Adds another CHECKPOINT tile to this checkpoint.
     * @param x the X coordinate of the tile to be added to this checkpoint.
     * @param y the Y coordinate of the tile to be added to this checkpoint.
     */
    public void addTile(int x, int y) {
        int[][] coorTemp;
            coorTemp = new int[coordinates.length + 1][2];

        for (int i = 0; i < coordinates.length; i++) {
            coorTemp[i][0] = coordinates[i][0];
            coorTemp[i][1] = coordinates[i][1];
        }
        coorTemp[coorTemp.length - 1][0] = x;
        coorTemp[coorTemp.length - 1][1] = y;

        coordinates = coorTemp;
    }

    /**
     * Returns the number of tiles this checkpoint contains.
     * @return the number of tiles this checkpoint contains.
     */
    public int getNoOfTiles() {
        return coordinates.length;
    }

    /**
     * Returns the X coordinate of the given tile.
     * @param tileIndex the index which determines of which tile is to be returned the X coordinate.
     * @return the X coordinate of the tile of this checkpoint determined by the index.
     */
    public int getXOfTile(int tileIndex) {
        return coordinates[tileIndex][0];
    }

    /**
     * Returns the Y coordinate of the given tile.
     * @param tileIndex the index which determines of which tile is to be returned the Y coordinate.
     * @return the X coordinate of the tile of this checkpoint determined by the index.
     */
    public int getYOfTile(int tileIndex) {
        return coordinates[tileIndex][1];
    }

    /**
     * Returns true if the tile with given coordinates belongs to this checkpoint.
     * @param x the X coordinate of the tile to be checked whether it belongs to this checkpoint.
     * @param y the Y coordinate of the tile to be checked whether it belongs to this checkpoint.
     * @return true if the tile with given coordinates belongs to this checkpoint.
     */
    public boolean tileBelongsTo(int x, int y) {
        for (int[] tile : coordinates) {
            if (tile[0] == x && tile[1] == y) {
                return true;
            }
        }
        return false;
    }

    /**
     * Saves that the given car has passed this checkpoint.
     * @param car the car which has passed this checkpoint.
     */
    public void carPassed(int car) {
        carsPassed[car] = true;
    }

    /**
     * Returns true if the given car has passed this checkpoint.
     * @param car the car which is to be checked whether it has passed this checkpoint.
     * @return true if the given car has passed this checkpoint.
     */
    public boolean getCarPassed(int car) {
        return carsPassed[car];
    }

}
