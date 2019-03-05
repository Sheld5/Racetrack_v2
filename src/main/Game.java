package main;

import model.*;
import util.MapReader;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Game extends JPanel {

    private final int TILE_SIZE = 16;
    private final int MAP_INDENT = 50;
    private final int TURN_MAX = 1000;

    private Map map;
    private Car[] cars;
    private CrossHair[] ch;
    private DriverAI[] drivers;
    private Checkpoint[] checkpoints;
    private Checkpoint finish;

    private int activeCar;
    private boolean crashed;
    private int turn;

    Game(int width, int height, int numberOfCars, DriverAI[] drivers) {
        init(width, height);
        initCrossHair();
        initCars(numberOfCars);
        initMap();
        initDrivers(drivers);
        initCheckpoints(numberOfCars);
        initFinish(numberOfCars);

        activeCar = cars.length - 1;
        crashed = false;
        turn = 0;

        System.out.println("Game initialized");
        System.out.println();

        nextTurn();
    }

    private void init(int width, int height) {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initMap() {
        map = new Map(MapReader.getData("Map01.tmx"), MapReader.getMapSizeX(), MapReader.getMapSizeY(), TILE_SIZE, MapReader.getTileSet("RacetrackTileSet.tsx"));
        map.setLocation(50, 50);
        map.setVisible(true);
        add(map);

        System.out.println("map initialized");
    }

    private void initCars(int n) {
        cars = new Car[n];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(TILE_SIZE);
            moveCar(cars[i], 3, 10);
            cars[i].setVisible(true);
            add(cars[i]);
        }

        System.out.println(cars.length + " cars initialized");
    }

    private void initCrossHair() {
        ch = new CrossHair[9];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = new CrossHair(i, TILE_SIZE, this);
            ch[i].setVisible(false);
            add(ch[i]);
        }
        moveCH(3,9);

        System.out.println("crosshair initialized");
    }

    private void initDrivers(DriverAI[] drivers) {
        if (drivers != null){
            this.drivers = drivers;
            for (int i = 0; i < drivers.length; i++) {
                drivers[i].init(cars[i], map);
            }
            System.out.println(drivers.length + " AIdrivers initialized");
        } else {
            System.out.println("0 AIdrivers initialized");
        }
    }

    private void initCheckpoints(int numberOfCars) {

        checkpoints = new Checkpoint[0];
        boolean foundCheckpoint = false;

        for (int x = 0; x < map.getWidthInTiles(); x++) {
            for (int y = 0; y < map.getHeightInTiles(); y++) {

                if (map.getTile(x, y) == Map.Tile.CHECKPOINT) {
                    for (Checkpoint ch : checkpoints) {
                        for (int i = 0; i < ch.getNoOfTiles(); i++) {
                            if ((ch.getXOfTile(i) - 1 <= x) && (x <= ch.getXOfTile(i) + 1) && (ch.getYOfTile(i) - 1 <= y) && (y <= ch.getYOfTile(i) + 1)) {
                                ch.addTile(x, y);
                                foundCheckpoint = true;
                            }
                            if (foundCheckpoint) {
                                break;
                            }
                        }
                        if (foundCheckpoint) {
                            break;
                        }
                    }

                    if (!foundCheckpoint) {
                        Checkpoint[] checkTemp = new Checkpoint[checkpoints.length + 1];
                        for (int i = 0; i < checkpoints.length; i++) {
                            checkTemp[i] = checkpoints[i];
                        }
                        Checkpoint ch = new Checkpoint(x, y, numberOfCars);
                        checkTemp[checkTemp.length - 1] = ch;
                        checkpoints = checkTemp;
                    }

                    foundCheckpoint = false;
                }

            }
        }

        System.out.println(checkpoints.length + " checkpoints initialized");

    }

    private void initFinish(int numberOfCars) {
        boolean errorOccurred = false;
        boolean addedToFinish;
        for (int x = 0; x < map.getWidthInTiles(); x++) {
            for (int y = 0; y < map.getHeightInTiles(); y++) {
                if (map.getTile(x, y) == Map.Tile.FINISH) {
                    if (finish == null) {
                        finish = new Checkpoint(x, y, numberOfCars);
                    } else {
                        addedToFinish = false;
                        for (int i = 0; i < finish.getNoOfTiles(); i++) {
                            if ((finish.getXOfTile(i) - 1 <= x) && (x <= finish.getXOfTile(i) + 1) && (finish.getYOfTile(i) - 1 <= y) && (y <= finish.getYOfTile(i) + 1)) {
                                finish.addTile(x, y);
                                addedToFinish = true;
                                break;
                            }
                        }
                        if (!addedToFinish && !errorOccurred) {
                            System.out.println("Error: More than one finish found on the map.");
                            System.out.println("Only one finish will be recognized.");
                            errorOccurred = true;
                        }
                    }
                }
            }
        }
        System.out.println("Finish initialized");
    }



    // manages the turn cycle of the cars and calls the drive() method each turn of each car
    private void nextTurn() {
        turn++;
        if (allCarsFinished()) {
            System.out.println("Game finished!");
        } else if (turn > TURN_MAX) {
            System.out.println("Turn limit reached!");
        } else {
            nextCar();
            if (drivers != null && activeCar < drivers.length) {
                drive(cars[activeCar], drivers[activeCar].drive());
                nextTurn();
            } else {
                showCH();
            }
        }
    }

    public void onCHClick(int index) {
        hideCH();
        drive(cars[activeCar], index);
        nextTurn();
    }

    // changes the velocities of the cars and calls the goThroughPath() function
    private void drive(Car car, int move) {
        if (move < 3) {
            car.accelY(-1);
        } else if (move > 5) {
            car.accelY(1);
        }
        if (move%3 == 0) {
            car.accelX(-1);
        } else if (move%3 == 2) {
            car.accelX(1);
        }
        goThroughPath(car);
    }

    // goes through the path of the car tile by tile and calls the checkForCollision() function
    @SuppressWarnings("Duplicates")
    private void goThroughPath(Car car) {

        int initX = car.getTileX();
        int initY = car.getTileY();
        int targetX = initX + car.getVelX();
        int targetY = initY + car.getVelY();
        int dirX = initDir(initX, targetX);
        int dirY = initDir(initY, targetY);

        if (initX == targetX) {
            for (int y = initY; y - dirY != targetY; y += dirY) {
                checkForCollision(car, initX, y);
                if (crashed) {
                    break;
                }
            }

        } else if (initY == targetY) {
            for (int x = initX; x - dirX != targetX; x += dirX) {
                checkForCollision(car, x, initY);
                if (crashed) {
                    break;
                }
            }

        } else if (abs(initX - targetX) == abs(initY - targetY)) {
            int x, y;
            for (int i = 0; i <= abs(initX - targetX); i++) {
                x = initX + dirX * i;
                y = initY + dirY * i;
                checkForCollision(car, x, y);
                if (crashed) {
                    break;
                }
            }

        } else {
            int a = -(targetY - initY);
            int b = targetX - initX;
            int c = - a * initX - b * initY;

            if (abs(initX - targetX) > abs(initY - targetY)) {
                for (int x = initX; x - dirX != targetX; x += dirX) {
                    for (int y = initY; y - dirY != targetY; y += dirY) {
                        checkForCollision(car, x, y, a, b, c);
                        if (crashed) {
                            break;
                        }
                    }
                    if (crashed) {
                        break;
                    }
                }
            } else {
                for (int y = initY; y - dirY != targetY; y += dirY) {
                    for (int x = initX; x - dirX != targetX; x += dirX) {
                        checkForCollision(car, x, y, a, b, c);
                        if (crashed) {
                            break;
                        }
                    }
                    if (crashed) {
                        break;
                    }
                }
            }
        }

        crashed = false;
    }

    // checks next tile for its rideability and moves the car accordingly
    @SuppressWarnings("Duplicates")
    private void checkForCollision(Car car, int x, int y) {
        if (map.isTileRideable(x, y)) {
            moveCar(car, x, y);
            checkForSpecialTile(x, y);
        } else {
            onCarCrash(car);
            crashed = true;
        }
    }

    @SuppressWarnings("Duplicates")
    private void checkForCollision(Car car, int x, int y, int a, int b, int c) {
        if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
            if (map.isTileRideable(x, y)) {
                moveCar(car, x, y);
                checkForSpecialTile(x, y);
            } else {
                onCarCrash(car);
                crashed = true;
            }
        }
    }

    private void onCarCrash(Car car) {
        car.setVelX(0);
        car.setVelY(0);
        System.out.println("car" + activeCar + " crashed");
    }

    private void checkForSpecialTile(int x, int y) {
        checkForCheckpoint(x, y);
        checkForFinish(x, y);
    }

    private void checkForCheckpoint(int x, int y) {
        if (map.getTile(x, y) == Map.Tile.CHECKPOINT) {
            for (int i = 0; i < checkpoints.length; i++) {
                if (checkpoints[i].tileBelongsTo(x, y) && !checkpoints[i].getCarPassed(activeCar)) {
                    checkpoints[i].carPassed(activeCar);
                    System.out.println("car" + activeCar + " passed checkpoint" + i);
                }
                if (checkpoints[i].tileBelongsTo(x, y)) {
                    break;
                }
            }
        }
    }

    private void checkForFinish(int x, int y) {
        if (map.getTile(x, y) == Map.Tile.FINISH) {
            for (Checkpoint ch : checkpoints) {
                if (!ch.getCarPassed(activeCar)) {
                    return;
                }
            }
            cars[activeCar].finished();
            System.out.println("Car" + activeCar + " finished the race.");
        }
    }

    private void moveCar(Car car, int x, int y) {
        car.setTileXY(x,y);
        car.setLocation(MAP_INDENT + x * TILE_SIZE, MAP_INDENT + y * TILE_SIZE);
    }

    private void moveCH(int x, int y) {
        for (int i = 0; i < ch.length; i++) {
            ch[i].setTileXY(x - 1 + i%3, y - 1 + i/3);
            ch[i].setLocation(MAP_INDENT + (x - 1 + i%3) * TILE_SIZE, MAP_INDENT + (y - 1 + i/3) * TILE_SIZE);
        }
    }

    private void showCH() {
        moveCH(cars[activeCar].getTileX() + cars[activeCar].getVelX(), cars[activeCar].getTileY() + cars[activeCar].getVelY());
        for (CrossHair c : ch) {
            c.setVisible(true);
        }
    }

    private void hideCH() {
        for (CrossHair c : ch) {
            c.setVisible(false);
        }
    }

    private boolean allCarsFinished() {
        for (Car c : cars) {
            if (!c.isFinished()) {
                return false;
            }
        }
        return true;
    }

    private void nextCar() {
        rotateCar();
        if (cars[activeCar].isFinished()) {
            rotateCar();
        }
    }

    private void rotateCar() {
        if (activeCar < cars.length - 1) {
            activeCar++;
        } else {
            activeCar = 0;
        }
    }

    private int initDir(int initC, int targetC) {
        if (targetC - initC > 0) {
            return 1;
        } else if (targetC - initC < 0) {
            return -1;
        } else {
            return 0;
        }
    }

}
