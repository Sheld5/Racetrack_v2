package model;

import java.util.ArrayList;
import static java.lang.Math.*;

// Simple AI which creates "Proximity map" which assigns every tile value depending on their proximity to the finish.
// Then, every turn the AI chooses the option with the lowest proximity values.
// This is a copy of the ProximityAI except it never goes faster than one tile per turn.
@SuppressWarnings("Duplicates")
public class ProximityOneByOneAI implements DriverAI {

    private Tile[][] map;
    private ArrayList<int[]> finishes;
    private int[][] proximityMap;

    @Override
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        int bestValue = -1;
        int[] move = new int[2];
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                try {
                    if (abs(carVelocity[0] + x) <= 1 && abs(carVelocity[1] + y) <= 1) {
                        int tileValue = proximityMap[carCoordinates[0] + carVelocity[0] + x][carCoordinates[1] + carVelocity[1] + y];
                        if (tileValue != -1 && (bestValue == -1 || bestValue > tileValue)) {
                            bestValue = tileValue;
                            move = new int[]{x,y};
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }

        try {
            System.out.println("Move: " + move[0] + " " + move[1]);
        } catch (NullPointerException e) {
            System.out.println("Move: udelal jsem nejakou blbost");
        }
        return move;
    }

    @Override
    public void init(Tile[][] map) {
        this.map = map;
        findFinishes();
        initProximityMap();
        printProximityMap();
    }

    private void findFinishes() {
        finishes = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == Tile.FINISH) {
                    finishes.add(new int[]{x,y});
                }
            }
        }
    }

    private void initProximityMap() {
        proximityMap = new int[map.length][map[0].length];
        for (int x = 0; x < proximityMap.length; x++) {
            for (int y = 0; y < proximityMap[0].length; y++) {
                proximityMap[x][y] = -1;
            }
        }
        for (int[] finish : finishes) {
            proximityMap[finish[0]][finish[1]] = 0;
        }
        for (int[] finish : finishes) {
            checkAdjacent(finish[0], finish[1]);
        }
    }

    private void checkAdjacent(int tileX, int tileY) {
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                try {
                    if (map[tileX + x][tileY +  y] != Tile.WALL
                            && map[tileX + x][tileY +  y] != Tile.WATER
                            && map[tileX + x][tileY +  y] != Tile.ICE) {
                        if (proximityMap[tileX + x][tileY + y] == -1
                                || proximityMap[tileX + x][tileY +  y] > proximityMap[tileX][tileY] + 1) {
                            proximityMap[tileX + x][tileY + y] = proximityMap[tileX][tileY] + 1;
                            checkAdjacent(tileX + x, tileY + y);
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }
    }

    private void printProximityMap() {
        for (int y = 0; y < proximityMap[0].length; y++) {
            for (int x = 0; x < proximityMap.length; x++) {
                if (proximityMap[x][y] != -1 && proximityMap[x][y] / 10 == 0){
                    System.out.print(" " + proximityMap[x][y] + " ");
                } else {
                    System.out.print(proximityMap[x][y] + " ");
                }
            }
            System.out.println();
        }
    }

}