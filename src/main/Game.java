package main;

import javax.swing.*;
import java.awt.*;

class Game extends JPanel {

    private int width, height;
    private int[][] mapInt;
    private Tile[][] mapTile;
    private enum Tile {
        START, FINISH, CHECKPOINT, ROAD, GRASS, WATER, WALL, SAND
    }

    Game(int width, int height) {
        this.width = width;
        this.height = height;
        init();
        initMap();
        System.out.println("Game initialized");
    }

    private void init() {
        setSize(width, height);
        setBackground(Color.BLACK);
        setLayout(null);
    }

    private void initMap() {
        mapInt = new int[][] {
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
        };
        mapTile = new Tile[mapInt.length][mapInt[0].length];
        for (int y = 0; y < mapInt.length; y++) {
            for (int x = 0; x < mapInt[0].length; x++) {
                switch(mapInt[y][x]) {
                    case 6:
                        mapTile[y][x] = Tile.START;
                        break;
                    case 2:
                        mapTile[y][x] = Tile.FINISH;
                        break;
                    case 1:
                        mapTile[y][x] = Tile.CHECKPOINT;
                        break;
                    case 4:
                        mapTile[y][x] = Tile.ROAD;
                        break;
                    case 3:
                        mapTile[y][x] = Tile.GRASS;
                        break;
                    case 8:
                        mapTile[y][x] = Tile.WATER;
                        break;
                    case 7:
                        mapTile[y][x] = Tile.WALL;
                        break;
                    case 5:
                        mapTile[y][x] = Tile.SAND;
                        break;
                }
            }
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = 16;
        int mapX = (width - mapInt[0].length * tileSize) / 2;
        int mapY = (height - mapInt.length * tileSize) / 2;
        for (int y = 0; y < mapInt.length; y++) {
            for (int x = 0; x < mapInt[0].length; x++) {
                switch(mapTile[y][x]) {
                    case START:
                        g.drawImage(Resources.tileStart.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case FINISH:
                        g.drawImage(Resources.tileFinish.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case CHECKPOINT:
                        g.drawImage(Resources.tileCheckpoint.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case ROAD:
                        g.drawImage(Resources.tileRoad.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case GRASS:
                        g.drawImage(Resources.tileGrass.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case WATER:
                        g.drawImage(Resources.tileWater.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case WALL:
                        g.drawImage(Resources.tileWall.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                    case SAND:
                        g.drawImage(Resources.tileSand.getScaledInstance(tileSize, tileSize, Image.SCALE_DEFAULT), x * tileSize + mapX, y * tileSize + mapY, null);
                        break;
                }
            }
        }
    }

}
