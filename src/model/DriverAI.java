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

    // This method should include all the AI logic.
    public abstract int drive();



    // Returns type of the tile with corresponding coordinates.
    // Coordinates of the map start at 0. (If the map is 20x20 tiles, x and y coordinates range from 0 to 19.)
    private Map.Tile getTile(int x, int y) {
        if (x < 0 || x >= getMapWidth() || y < 0 || y >= getMapHeight()) {
            throw new IllegalArgumentException("getTile(int x, int y) method has been called with coordinates out of bounds of the map as its arguments");
        }
        return map.getTile(x, y);
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
