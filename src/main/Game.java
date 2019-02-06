package main;

import model.Car;
import model.CrossHair;
import model.DriverAI;
import model.Map;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {

    private final int TILE_SIZE = 16;
    private final int MAP_INDENT = 50;

    private Map map;
    private Car[] cars;
    private CrossHair[] ch;
    private DriverAI[] drivers;

    private int activeCar;



    Game(int width, int height, int numberOfCars, DriverAI[] drivers) {
        init(width, height);
        initCrossHair();
        initCars(numberOfCars);
        initMap();
        initDrivers(drivers);
        activeCar = 0;
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
            moveCar(cars[i], 3, 9);
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



    // manages the next turns of the cars and calls the drive() method
    private void run() {
        if (drivers != null && activeCar < drivers.length) {
            drive(drivers[activeCar].drive());
            nextCar();
            if (!allCarsFinished()) {
                run();
            }
        } else {
            showCH();
        }
    }

    // changes the velocities of the cars and calls the relocateCar() function
    private void drive(int move) {
        if (move < 3) {
            cars[activeCar].accelVelY(-1);
        } else if (move > 5) {
            cars[activeCar].accelVelY(1);
        }
        if (move%3 == 0) {
            cars[activeCar].accelVelX(-1);
        } else if (move%3 == 2) {
            cars[activeCar].accelVelX(1);
        }

        relocateCar(cars[activeCar], cars[activeCar].getTileX() + cars[activeCar].getVelX(), cars[activeCar].getTileY() + cars[activeCar].getVelY());
    }

    // receives the coordinates to which the driver wishes to move and determines where will the car actually end up
    private void relocateCar(Car car, int x, int y) {
        ToDo: collision detection and stuff
    }

    private boolean checkTile(int x, int y) {
        if (map.getTile(x, y) == Map.Tile.ROAD || map.getTile(x, y) == Map.Tile.START || map.getTile(x, y) == Map.Tile.CHECKPOINT || map.getTile(x, y) == Map.Tile.FINISH) {
            return true;
        } else {
            return false;
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

    public void onCHClick(int index) {
        hideCH();
        drive(index);
        nextCar();
        if (!allCarsFinished()) {
            run();
        }
    }

    private void nextCar() {
        if (activeCar < cars.length - 1) {
            activeCar++;
        } else {
            activeCar = 0;
        }
    }

}
