package main;

import model.Car;
import model.CrossHair;
import model.Map;

import javax.swing.*;
import java.awt.*;

public class Game extends JPanel {

    private final int TILE_SIZE = 16;
    private final int MAP_INDENT = 50;

    private Map map;
    private Car[] cars;
    private CrossHair[] ch;

    private int activeCar = 0;

    Game(int width, int height) {
        init(width, height);
        initCrossHair();
        initCars();
        initMap();
        System.out.println("Game initialized");
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

    private void initCars() {
        cars = new Car[4];
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

    public void onCHClick(int index) {
        System.out.println("CrossHair" + index + " clicked");

        if (index < 3) {
            cars[activeCar].accelVelY(-1);
        } else if (index > 5) {
            cars[activeCar].accelVelY(1);
        }
        if (index%3 == 0) {
            cars[activeCar].accelVelX(-1);
        } else if (index%3 == 2) {
            cars[activeCar].accelVelX(1);
        }

        moveCar(cars[activeCar], cars[activeCar].getTileX() + cars[activeCar].getVelX(), cars[activeCar].getTileY() + cars[activeCar].getVelY());

        if (activeCar == cars.length - 1) {
            activeCar = 0;
            moveCH(cars[activeCar].getTileX() + cars[activeCar].getVelX(), cars[activeCar].getTileY() + cars[activeCar].getVelY());
        } else {
            activeCar++;
            moveCH(cars[activeCar].getTileX() + cars[activeCar].getVelX(), cars[activeCar].getTileY() + cars[activeCar].getVelY());
        }
    }

}
