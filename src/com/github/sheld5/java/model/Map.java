package model;

import main.Game;
import util.Resources;
import util.StartNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

public class Map extends JPanel {

    private Game game;
    private int width, height;
    private int[] start;
    private Tile[][] mapTile;

    public Map(int[][] mapInt, int widthInTiles, int heightInTiles, HashMap<Integer, Tile> tileSet, Game game) throws StartNotFoundException {
        this.game = game;
        width = widthInTiles;
        height = heightInTiles;
        setSize(width * game.getTileSize(), height * game.getTileSize());
        setBackground(Color.BLACK);
        initMapTile(mapInt, tileSet);
    }

    private void initMapTile(int[][] mapInt, HashMap<Integer, Tile> tileSet) throws StartNotFoundException {
        start = new int[2];
        boolean startFound = false;
        mapTile = new Tile[mapInt.length][mapInt[0].length];
        for (int y = 0; y < mapInt.length; y++) {
            for (int x = 0; x < mapInt[0].length; x++) {
                for (Integer i : tileSet.keySet()) {
                    if (i == mapInt[y][x]) {
                        mapTile[y][x] = tileSet.get(i);
                        if (tileSet.get(i) == Tile.START) {
                            if (!startFound) {
                                start[0] = x;
                                start[1] = y;
                                startFound = true;
                            } else {
                                System.out.println("More than one start found on the map");
                                throw new StartNotFoundException();
                            }
                        }
                        break;
                    }
                }
            }
        }
        if (!startFound) {
            System.out.println("No start found on the map");
            throw new StartNotFoundException();
        }
    }

    @Override
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        int tileSize = game.getTileSize();
        setSize(width * tileSize, height * tileSize);
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
                    case ICE:
                        g.drawImage(Resources.tileIce.getScaledInstance(tileSize, tileSize, Image.SCALE_SMOOTH), x * tileSize, y * tileSize, null);
                        break;
                }
            }
        }
    }

    public Tile getTile(int x, int y) {
        return mapTile[y][x];
    }

    public Tile getTile(int[] coordinates) {
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("method getTile only accepts two ints or an int array with the length of 2 as argument");
        } else {
            return mapTile[coordinates[1]][coordinates[0]];
        }
    }

    public boolean isTileRideable(int x, int y) {
        return getTile(x, y) != Tile.WALL;
    }

    public int getWidthInTiles() {
        return width;
    }

    public int getHeightInTiles() {
        return height;
    }

    // Creates a deep copy of the map, so the AI cannot alter the real map.
    public Tile[][] getMapCopy() {
        Tile[][] mapCopy = new Tile[mapTile.length][mapTile[0].length];
        for (int x = 0; x < mapTile.length; x++) {
            for (int y = 0; y < mapTile[0].length; y++) {
                mapCopy[x][y] = mapTile[x][y];
            }
        }
        return mapCopy;
    }

    public int[] getStart() {
        return start;
    }

}