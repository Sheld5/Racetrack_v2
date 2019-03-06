package model;

/*
The AI should be a class extending DriverAI.
The drive() method is called every turn to determine what move will the car make.
All AI logic should be included in the drive() method (or methods called from the drive() method).
The drive() method must return an int from 0 to 8 corresponding to the nine tiles the car can move each turn.
The nine tiles are numbered from top-left corner by rows.
The AI should use the "get" methods to obtain all necessary information about the state of the game.
 */

public abstract class DriverAI {

    private Car car;
    private Map map;

    // AIs should use these variables to get map dimensions.
    protected int mapWidth, mapHeight;

    public void init(Car car, Map map, int mapWidth, int mapHeight) {
        this.car = car;
        this.map = map;
        this.mapWidth = mapWidth;
        this.mapHeight = mapHeight;
    }

    // Is called by the game each turn to determine what will the car driven by the AI do.
    // Calls logic() and returns the same index if it is valid.
    // Returns the index 4 (not changing velX nor velY) if the index returned by logic() is not valid.
    public int drive() {
        int nextMove = logic();
        if (0 <= nextMove && nextMove < 9) {
            return nextMove;
        } else {
            return 4;
        }
    }

    // This method should include all the AI logic and everything the AI does every turn.
    // It should return the index of the next move (0-8).
    protected abstract int logic();



    // Get-methods should be used by the AI to get necessary information about the map and the state of the car.

    // Returns type of the tile with corresponding coordinates.
    protected Map.Tile getTile(int x, int y) {
        try {
            return map.getTile(x, y);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("AI called getTile(int x, int y) method with x and/or y out of bounds of the map");
            e.printStackTrace();
            return null;
        }
    }

    // Returns whether the tile with corresponding coordinates is rideable or not.
    protected boolean isTileRideable(int x, int y) {
        try {
            return map.isTileRideable(x, y);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("AI called isTileRideable(int x, int y) method with x and/or y out of bounds of the map");
            e.printStackTrace();
            return false;
        }
    }

    // Returns the x coordinate of the car.
    protected int getCarX() {
        return car.getTileX();
    }

    // Returns the y coordinate of the car.
    protected int getCarY() {
        return car.getTileY();
    }

    // Returns the velocity of the car in the direction of the x axis.
    protected int getVelX() {
        return car.getVelX();
    }

    // Returns the velocity of the car in the direction of the y axis.
    protected int getVelY() {
        return car.getVelY();
    }

}
