package model;

import main.Resources;

import javax.swing.*;
import java.awt.*;

public class Map extends JPanel {

    private int width, height;
    private int tileSize;
    public enum Tile {
        START, FINISH, CHECKPOINT, ROAD, GRASS, WATER, WALL, SAND
    }
    private Tile[][] mapTile;

    public Map(int widthInTiles, int heightInTiles, int tileSize, int[][] mapInt) {
        width = widthInTiles;
        height = heightInTiles;
        this.tileSize = tileSize;
        init(widthInTiles, heightInTiles);
        initMapTile(mapInt);
    }

    private void init(int width, int height) {
        setSize(width * tileSize, height * tileSize);
        setBackground(Color.BLACK);
    }

    private void initMapTile(int[][] mapInt) {
        mapTile = new Tile[mapInt.length][mapInt[0].length];
        for (int y = 0; y < mapInt.length; y++) {
            for (int x = 0; x < mapInt[0].length; x++) {
                switch (mapInt[y][x]) {
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
        for (int y = 0; y < mapTile.length; y++) {
            for (int x = 0; x < mapTile[0].length; x++) {
                switch(mapTile[y][x]) {
                    case START:
                        g.drawImage(Resources.tileStart.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case FINISH:
                        g.drawImage(Resources.tileFinish.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case CHECKPOINT:
                        g.drawImage(Resources.tileCheckpoint.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case ROAD:
                        g.drawImage(Resources.tileRoad.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case GRASS:
                        g.drawImage(Resources.tileGrass.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case WATER:
                        g.drawImage(Resources.tileWater.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case WALL:
                        g.drawImage(Resources.tileWall.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                    case SAND:
                        g.drawImage(Resources.tileSand.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        return mapTile[y][x];
    }

    public boolean isTileRideable(int x, int y) {
        if (getTile(x, y) == Map.Tile.ROAD || getTile(x, y) == Map.Tile.START || getTile(x, y) == Map.Tile.CHECKPOINT || getTile(x, y) == Map.Tile.FINISH) {
            return true;
        } else {
            return false;
        }
    }

    public int getWidthInTiles() {
        return width;
    }

    public int getHeightInTiles() {
        return height;
    }

}
