package model;

import main.Game;
import util.Resources;
import util.StartNotFoundException;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;

/**
 * represents the map in the game
 */
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

    /**
     * initializes the 2d array of Tiles representing the map and finds the start
     * @param mapInt
     * @param tileSet
     * @throws StartNotFoundException
     */
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

    /**
     * paints each tile of the map with the corresponding texture
     * @param g
     */
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

    /**
     * returns the type of the tile with given coordinates
     * @param x
     * @param y
     * @return
     */
    public Tile getTile(int x, int y) {
        try {
            return mapTile[y][x];
        } catch (ArrayIndexOutOfBoundsException e) {
            return null;
        }

    }

    /**
     * returns the type of the tile with given coordinates
     * @param coordinates
     * @return
     */
    public Tile getTile(int[] coordinates) {
        if (coordinates.length != 2) {
            throw new IllegalArgumentException("method getTile only accepts two ints or an int array with the length of 2 as argument");
        } else {
            return mapTile[coordinates[1]][coordinates[0]];
        }
    }

    /**
     * returns true if the tile with given coordinates is rideable (anything but WALL) and is not outside the map
     * @param x
     * @param y
     * @return
     */
    public boolean isTileRideable(int x, int y) {
        return (getTile(x, y) != Tile.WALL) && (getTile(x, y) != null);
    }

    public int getWidthInTiles() {
        return width;
    }

    public int getHeightInTiles() {
        return height;
    }

    /**
     * returns a deep copy of the 2d Tile array representing the map
     * @return
     */
    public Tile[][] getMapCopy() {
        Tile[][] mapCopy = new Tile[mapTile.length][mapTile[0].length];
        for (int x = 0; x < mapTile.length; x++) {
            for (int y = 0; y < mapTile[0].length; y++) {
                mapCopy[x][y] = mapTile[x][y];
            }
        }
        return mapCopy;
    }

    /**
     * returns the coordinates of the start
     * @return
     */
    public int[] getStart() {
        return start;
    }

}
