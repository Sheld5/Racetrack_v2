package main;

import resources.ai.DriverAI;
import model.*;
import util.MapReader;
import util.StartNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Game extends JPanel {

    private final int TILE_SIZE = 24;
    private final int MAP_INDENT = 16;
    private final int TURN_MAX = 500;

    private JLabel turnLabel;
    private Font font;

    private Map map;
    private Car[] cars;
    private CrossHair[] ch;
    private DriverAI[] drivers;
    private Checkpoint[] checkpoints;

    private int activeCarIndex;
    private Car activeCar;
    private boolean stop;
    private int turn;

    Game(int width, int height, int numberOfCars, DriverAI[] drivers, String mapName) throws FileNotFoundException, StartNotFoundException {
        System.out.println();

        init(width, height);
        initCrossHair();
        initCars(numberOfCars);
        initMap(mapName);
        initTurnLabel();

        moveCarsToStart();

        this.drivers = drivers;
        initCheckpoints(numberOfCars);

        activeCarIndex = cars.length - 1;
        stop = false;
        if (cars.length == 1) {
            turn = -1;
        } else {
            turn = 0;
        }

        System.out.println("Game initialized successfully");
        System.out.println();

        nextTurn();
    }

    private void init(int width, int height) {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initTurnLabel() {
        font = new Font(Font.SANS_SERIF, Font.BOLD, 24);

        turnLabel = new JLabel(String.valueOf(turn));
        turnLabel.setVisible(true);
        turnLabel.setFont(font);
        turnLabel.setForeground(Color.orange);
        turnLabel.setBounds(map.getX() + map.getWidth() + MAP_INDENT, MAP_INDENT,50, 50);
        add(turnLabel);
    }

    private void initMap(String mapName) throws FileNotFoundException {
        MapReader mr = new MapReader();
        map = new Map(mr.getData(mapName), mr.getMapSizeX(), mr.getMapSizeY(), TILE_SIZE, mr.getTileSet("RacetrackTileSet.tsx"));
        map.setLocation(MAP_INDENT, MAP_INDENT);
        map.setVisible(true);
        add(map);

        System.out.println("map initialized");
    }

    private void initCars(int n) {
        cars = new Car[n];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(TILE_SIZE);
            cars[i].setVisible(true);
            add(cars[i]);
        }

        if (cars.length == 1) {
            System.out.println("1 car initialized");
        } else {
            System.out.println(cars.length + " cars initialized");
        }
    }

    private void initCrossHair() {
        ch = new CrossHair[9];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = new CrossHair(i, TILE_SIZE, this);
            ch[i].setVisible(false);
            add(ch[i]);
        }

        System.out.println("crosshair initialized");
    }

    private void initCheckpoints(int numberOfCars) {

        checkpoints = new Checkpoint[0];
        boolean foundCheckpoint = false;

        for (int x = 0; x < map.getWidthInTiles(); x++) {
            for (int y = 0; y < map.getHeightInTiles(); y++) {

                if (map.getTile(x, y) == Tile.CHECKPOINT) {
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

    private void moveCarsToStart() throws StartNotFoundException {
        for (Car car : cars) {
            moveCar(car, map.getStartX(), map.getStartY());
        }
        moveCH(map.getStartX(), map.getStartY());
    }



    // manages the turn cycle of the cars and calls the drive() method each turn of each car
    private void nextTurn() {
        if (activeCarIndex == 0) {
            turn++;
            update();
        }
        if (allCarsIdle()) {
            endRace();
        } else if (turn > TURN_MAX) {
            System.out.println("Turn limit reached!");
        } else {
            nextCar();
            if (activeCar.isCrashed()) {
                activeCar.countdown();
                nextTurn();
            } else if (map.getTile(activeCar.getCoordinates()) == Tile.ICE && (activeCar.getVelocity()[0] != 0 || activeCar.getVelocity()[1] != 0)) {
                drive(activeCar, new int[]{0,0});
                nextTurn();
            } else {
                if (drivers != null && activeCarIndex < drivers.length) {
                    drive(activeCar, drivers[activeCarIndex].drive(activeCar.getCoordinates(), activeCar.getVelocity(), map.getMapCopy()));
                    nextTurn();
                } else {
                    showCH();
                }
            }
        }
    }

    public void onCHClick(int index) {
        hideCH();

        int[] a = new int[]{0,0};
        if (index < 3) {
            a[1] = -1;
        } else if (index > 5) {
            a[1] = 1;
        }
        if (index%3 == 0) {
            a[0] = -1;
        } else if (index%3 == 2) {
            a[0] = 1;
        }

        drive(activeCar, a);
        nextTurn();
    }

    // changes the velocities of the cars and calls the goThroughPath() function
    private void drive(Car car, int[] a) {
        car.accelerate(a);
        goThroughPath(car);
    }

    // goes through the path of the car tile by tile and calls the checkTile() function
    @SuppressWarnings("Duplicates")
    private void goThroughPath(Car car) {

        int initX = car.getCoordinates()[0];
        int initY = car.getCoordinates()[1];
        int targetX = initX + car.getVelocity()[0];
        int targetY = initY + car.getVelocity()[1];
        int dirX = initDir(initX, targetX);
        int dirY = initDir(initY, targetY);

        if (initX == targetX) {
            for (int y = initY + dirY; y - dirY != targetY; y += dirY) {
                checkTile(car, initX, y);
                if (stop) {
                    break;
                }
            }

        } else if (initY == targetY) {
            for (int x = initX + dirX; x - dirX != targetX; x += dirX) {
                checkTile(car, x, initY);
                if (stop) {
                    break;
                }
            }

        } else if (abs(initX - targetX) == abs(initY - targetY)) {
            int x, y;
            for (int i = 1; i <= abs(initX - targetX); i++) {
                x = initX + dirX * i;
                y = initY + dirY * i;
                checkTile(car, x, y);
                if (stop) {
                    break;
                }
            }

        } else {
            int a = -(targetY - initY);
            int b = targetX - initX;
            int c = - a * initX - b * initY;
            boolean firstTile = true;

            if (abs(initX - targetX) > abs(initY - targetY)) {
                for (int x = initX; x - dirX != targetX; x += dirX) {
                    for (int y = initY; y - dirY != targetY; y += dirY) {
                        if (firstTile) {
                            firstTile = false;
                        } else {
                            checkTile(car, x, y, a, b, c);
                        }
                        if (stop) {
                            break;
                        }
                    }
                    if (stop) {
                        break;
                    }
                }
            } else {
                for (int y = initY; y - dirY != targetY; y += dirY) {
                    for (int x = initX; x - dirX != targetX; x += dirX) {
                        if (firstTile) {
                            firstTile = false;
                        } else {
                            checkTile(car, x, y, a, b, c);
                        }
                        if (stop) {
                            break;
                        }
                    }
                    if (stop) {
                        break;
                    }
                }
            }
        }

        stop = false;
    }

    // checks next tile for its rideability and moves the car accordingly
    @SuppressWarnings("Duplicates")
    private void checkTile(Car car, int x, int y) {
        if (map.isTileRideable(x, y)) {
            moveCar(car, x, y);
            checkForSpecialTiles(car, x, y);
        } else {
            onCarCrash(car);
        }
    }

    @SuppressWarnings("Duplicates")
    private void checkTile(Car car, int x, int y, int a, int b, int c) {
        if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
            if (map.isTileRideable(x, y)) {
                moveCar(car, x, y);
                checkForSpecialTiles(car, x, y);
            } else {
                onCarCrash(car);
            }
        }
    }



    private void onCarCrash(Car car) {
        car.setVelocity(new int[]{0,0});
        car.crashed();
        stop = true;
    }

    private void checkForSpecialTiles(Car car, int x, int y) {
        checkForCheckpoint(x, y);
        checkForFinish(car, x, y);
        checkForSand(car, x, y);
        checkForWater(car, x, y);
    }

    private void checkForCheckpoint(int x, int y) {
        if (map.getTile(x, y) == Tile.CHECKPOINT) {
            for (int i = 0; i < checkpoints.length; i++) {
                if (checkpoints[i].tileBelongsTo(x, y) && !checkpoints[i].getCarPassed(activeCarIndex)) {
                    checkpoints[i].carPassed(activeCarIndex);
                    System.out.println("car" + activeCarIndex + " passed checkpoint" + i);
                }
                if (checkpoints[i].tileBelongsTo(x, y)) {
                    break;
                }
            }
        }
    }

    private void checkForFinish(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.FINISH) {
            for (Checkpoint ch : checkpoints) {
                if (!ch.getCarPassed(activeCarIndex)) {
                    return;
                }
            }
            car.finished();
            car.setTurnOfFinish(turn);
            System.out.println("Car" + activeCarIndex + " finished the race!");
        }
    }

    private void checkForSand(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.SAND) {
            car.setVelocity(new int[]{0,0});
            stop = true;
        }
    }

    private void checkForWater(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.WATER) {
            car.sunk();
            stop = true;
            System.out.println("Car" + activeCarIndex + " sunk!");
        }
    }



    private void moveCar(Car car, int x, int y) {
        car.setCoordinates(x,y);
        car.setLocation(MAP_INDENT + x * TILE_SIZE, MAP_INDENT + y * TILE_SIZE);
    }

    private void moveCH(int x, int y) {
        for (int i = 0; i < ch.length; i++) {
            ch[i].setTileXY(x - 1 + i%3, y - 1 + i/3);
            ch[i].setLocation(MAP_INDENT + (x - 1 + i%3) * TILE_SIZE, MAP_INDENT + (y - 1 + i/3) * TILE_SIZE);
        }
    }

    private void showCH() {
        moveCH(activeCar.getCoordinates()[0] + activeCar.getVelocity()[0], activeCar.getCoordinates()[1] + activeCar.getVelocity()[1]);
        for (CrossHair c : ch) {
            c.setVisible(true);
        }
    }

    private void hideCH() {
        for (CrossHair c : ch) {
            c.setVisible(false);
        }
    }

    private boolean allCarsIdle() {
        for (Car c : cars) {
            if (!c.isFinished() && !c.isSunk()) {
                return false;
            }
        }
        return true;
    }

    private void nextCar() {
        rotateCar();
        while (cars[activeCarIndex].isSunk() || cars[activeCarIndex].isFinished()) {
            rotateCar();
        }
        activeCar = cars[activeCarIndex];
    }

    private void rotateCar() {
        if (activeCarIndex < cars.length - 1) {
            activeCarIndex++;
        } else {
            activeCarIndex = 0;
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

    private void endRace() {
        System.out.println();
        System.out.println("Race finished");
        for (int i = 0; i < cars.length; i++) {
            if (cars[i].isFinished()) {
                System.out.println("Car" + i + " finished the race in " + cars[i].getTurnOfFinish() + " turns.");
            } else if (cars[i].isSunk()) {
                System.out.println("Car" + i + " went swimming and thus could not finish the race.");
            } else {
                System.out.println("Car" + i + " was not able to finish the race in " + TURN_MAX + " turns.");
            }
        }
    }

    private void update() {
        turnLabel.setText(String.valueOf(turn));
    }

}
