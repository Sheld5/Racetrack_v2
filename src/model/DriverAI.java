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

    public void init(Car car, Map map) {
        this.car = car;
        this.map = map;
    }

    // Is called by the game each turn to determine what will the car driven by this AI do.
    // Calls logic() and returns the same index if it is valid.
    // Returns the index 4 (going straight and not changing the velocity) if the index returned by logic() is not valid.
    public int drive() {
        int nextMove = logic();
        if (0 <= nextMove && nextMove < 9) {
            return nextMove;
        } else {
            return 4;
        }
    }

    // This method should include all the AI logic and return the index of the next move (0-8).
    abstract int logic();



    // Returns type of the tile with corresponding coordinates.
    // Coordinates of the map start at 0. (If the map is 20x20 tiles, x and y coordinates range from 0 to 19.)
    private Map.Tile getTile(int x, int y) {
        try {
            return map.getTile(x, y);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("AI called getTile(int x, int y) method with x and/or y out of bounds of the map");
            e.printStackTrace();

            // toDo: domakat tenhle exception
            return null;
        }
    }

    // Returns the x coordinate of the car.
    private int getCarX() {
        return car.getX();
    }

    // Returns the y coordinate of the car.
    private int getCarY() {
        return car.getY();
    }

    // Returns the velocity of the car in the direction of the x axis.
    private int getVelX() {
        return car.getVelX();
    }

    // Returns the velocity of the car in the direction of the y axis.
    private int getVelY() {
        return car.getVelY();
    }

    // Returns the width of the map as a number of tiles.
    private int getMapWidth() {
        return map.getWidthInTiles();
    }

    // Returns the height of the map as a number of tiles.
    private int getMapHeight() {
        return map.getHeightInTiles();
    }

}
