package model;

/**
 * Represents a checkpoint in the game.
 */
public class Checkpoint {

    private int[][] coordinates;
    private boolean[] carsPassed;

    public Checkpoint(int x, int y, int numberOfCars) {
        coordinates = new int[1][2];
        coordinates[0][0] = x;
        coordinates[0][1] = y;

        carsPassed = new boolean[numberOfCars];
        for (boolean b : carsPassed) b = false;
    }

    /**
     * Adds another CHECKPOINT tile to this checkpoint.
     * @param x
     * @param y
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
     * Returns number of tiles this checkpoint contains.
     * @return
     */
    public int getNoOfTiles() {
        return coordinates.length;
    }

    /**
     * Returns the X coordinate of the given tile.
     * @param tileIndex
     * @return
     */
    public int getXOfTile(int tileIndex) {
        return coordinates[tileIndex][0];
    }

    /**
     * Returns the Y coordinate of the given tile.
     * @param tileIndex
     * @return
     */
    public int getYOfTile(int tileIndex) {
        return coordinates[tileIndex][1];
    }

    /**
     * Return true if the tile with given coordinates belongs to this checkpoint.
     * @param x
     * @param y
     * @return
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
     * @param car
     */
    public void carPassed(int car) {
        carsPassed[car] = true;
    }

    /**
     * Returns true if the given car has passed this checkpoint.
     * @param car
     * @return
     */
    public boolean getCarPassed(int car) {
        return carsPassed[car];
    }

}
