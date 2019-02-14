package main;

import model.Car;
import model.CrossHair;
import model.DriverAI;
import model.Map;

import javax.swing.*;
import java.awt.*;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Game extends JPanel {

    private final int TILE_SIZE = 16;
    private final int MAP_INDENT = 50;

    private Map map;
    private Car[] cars;
    private CrossHair[] ch;
    private DriverAI[] drivers;

    private int activeCar;
    private boolean crashed;



    Game(int width, int height, int numberOfCars, DriverAI[] drivers) {
        init(width, height);
        initCrossHair();
        initCars(numberOfCars);
        initMap();
        initDrivers(drivers);
        activeCar = 0;
        crashed = false;
        System.out.println("Game initialized");

        run();
    }

    private void init(int width, int height) {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initMap() {
        map = new Map(20, 20, TILE_SIZE,
                new int[][] {
                    {7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7},
                    {7,3,4,4,4,4,4,3,3,3,3,3,3,3,3,3,3,3,3,7},
                    {7,3,4,4,4,4,4,4,4,1,4,4,4,4,3,3,3,3,3,7},
                    {7,3,4,4,4,4,4,4,4,1,4,4,4,4,4,4,4,3,3,7},
                    {7,3,4,4,3,3,3,8,8,8,8,5,5,4,4,4,4,3,3,7},
                    {7,3,4,4,3,3,8,8,8,5,5,5,5,5,5,4,4,3,3,7},
                    {7,3,4,4,8,8,8,8,8,8,5,5,5,5,5,4,4,3,3,7},
                    {7,3,4,4,8,4,4,4,4,4,4,5,5,8,8,4,4,3,3,7},
                    {7,3,4,4,8,4,4,4,4,4,4,4,8,8,8,4,4,3,3,7},
                    {7,3,4,4,8,4,4,8,4,4,4,1,8,8,3,4,4,3,3,7},
                    {7,3,4,6,8,4,4,8,8,3,1,4,4,3,4,4,4,3,3,7},
                    {7,3,4,4,8,4,4,8,8,8,4,4,4,4,4,4,4,3,3,7},
                    {7,3,2,2,8,4,4,8,8,8,8,4,4,4,4,4,4,3,3,7},
                    {7,3,4,4,8,4,4,4,8,8,8,7,7,7,7,7,7,7,7,7},
                    {7,3,4,4,8,4,4,4,4,4,4,4,4,4,4,4,1,3,3,7},
                    {7,3,4,4,8,8,4,4,4,4,4,4,4,4,4,1,4,3,3,7},
                    {7,3,4,4,7,7,7,7,7,7,7,7,7,7,7,4,4,3,3,7},
                    {7,3,4,4,7,7,7,7,7,7,7,7,7,7,7,4,4,3,3,7},
                    {7,3,4,4,4,4,4,4,4,4,4,4,4,4,4,4,4,3,3,7},
                    {7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7,7}
                }
        );

        map.setLocation(50, 50);
        map.setVisible(true);
        add(map);
    }

    private void initCars(int n) {
        cars = new Car[n];
        for (int i = 0; i < cars.length; i++) {
            cars[i] = new Car(TILE_SIZE);
            moveCar(cars[i], 3, 10);
            cars[i].setVisible(true);
            add(cars[i]);
        }
    }

    private void initCrossHair() {
        ch = new CrossHair[9];
        for (int i = 0; i < ch.length; i++) {
            ch[i] = new CrossHair(i, TILE_SIZE, this);
            ch[i].setVisible(true);
            add(ch[i]);
        }
        moveCH(3,9);
    }

    private void initDrivers(DriverAI[] drivers) {
        if (drivers != null){
            this.drivers = drivers;
            for (int i = 0; i < drivers.length; i++) {
                drivers[i].init(cars[i], map);
            }
        }
    }



    // manages the turn cycle of the cars and calls the drive() method each turn of each car
    private void run() {
        if (drivers != null && activeCar < drivers.length) {
            drive(cars[activeCar], drivers[activeCar].drive());
            nextCar();
            if (!allCarsFinished()) {
                run();
            }
        } else {
            showCH();
        }
    }

    public void onCHClick(int index) {
        hideCH();
        drive(cars[activeCar], index);
        nextCar();
        if (!allCarsFinished()) {
            run();
        }
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

        System.out.println();
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
        System.out.println("direction: " + dirX + " " + dirY);

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
        System.out.print(x + " " + y + " " + map.getTile(x, y));
        if (map.isTileRideable(x, y)) {
            System.out.println(" RIDEABLE");
            moveCar(car, x, y);
        } else {
            System.out.println(" NOT RIDEABLE");
            car.setVelX(0);
            car.setVelY(0);
            crashed = true;
        }
    }

    @SuppressWarnings("Duplicates")
    private void checkForCollision(Car car, int x, int y, int a, int b, int c) {
        if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
            System.out.print(x + " " + y + " " + map.getTile(x, y));
            if (map.isTileRideable(x, y)) {
                System.out.println(" RIDEABLE");
                moveCar(car, x, y);
            } else {
                System.out.println(" NOT RIDEABLE");
                car.setVelX(0);
                car.setVelY(0);
                crashed = true;
            }
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
        for (Car car : cars) {
            if (!(map.getTile(car.getTileX(), car.getTileY()) == Map.Tile.FINISH)) {
                return false;
            }
        }
        return true;
    }

    private void nextCar() {
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
