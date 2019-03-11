package model;

import util.Resources;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Map extends JPanel {

    private int width, height;
    private int tileSize;
    public enum Tile {
        START, FINISH, CHECKPOINT, ROAD, GRASS, WATER, WALL, SAND
    }
    private Tile[][] mapTile;
    private Tile[][] mapCopy;

    public Map(int[][] mapInt, int widthInTiles, int heightInTiles, int tileSize, HashMap<Integer, Tile> tileSet) {
        width = widthInTiles;
        height = heightInTiles;
        this.tileSize = tileSize;
        init(widthInTiles, heightInTiles);
        initMapTile(mapInt, tileSet);
    }

    private void init(int width, int height) {
        setSize(width * tileSize, height * tileSize);
        setBackground(Color.BLACK);
    }

    private void initMapTile(int[][] mapInt, HashMap<Integer, Tile> tileSet) {
        mapTile = new Tile[mapInt.length][mapInt[0].length];
        for (int y = 0; y < mapInt.length; y++) {
            for (int x = 0; x < mapInt[0].length; x++) {
                for (Integer i : tileSet.keySet()) {
                    if (i == mapInt[y][x]) {
                        mapTile[y][x] = tileSet.get(i);
                        break;
                    }
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
        if (getTile(x, y) == Tile.ROAD || getTile(x, y) == Tile.START || getTile(x, y) == Tile.CHECKPOINT || getTile(x, y) == Tile.FINISH || getTile(x, y) == Tile.SAND || getTile(x, y) == Tile.GRASS) {
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

    public Tile[][] getMapCopy() {
        mapCopy = mapTile;
        return mapCopy;
    }

}
