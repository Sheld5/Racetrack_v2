package model;

public class Checkpoint {

    private int[][] coordinates;

    public Checkpoint(int x, int y) {
        coordinates = new int[1][2];
        coordinates[0][0] = x;
        coordinates[0][1] = y;
    }

    public void addTile(int x, int y) {
        int[][] coorTemp;
            coorTemp = new int[coordinates.length + 1][2];

        for (int i = 0; i < coordinates.length; i++) {
            coorTemp[i][0] = coordinates[i][0];
            coorTemp[i][1] = coordinates[i][1];
        }
        coorTemp[coorTemp.length - 1][0] = x;
        coorTemp[coorTemp.length - 1][1] = y;

        coordinates = coorTemp;
    }

    public int getNoOfTiles() {
        return coordinates.length;
    }

    public int getXOfTile(int tileIndex) {
        return coordinates[tileIndex][0];
    }

    public int getYOfTile(int tileIndex) {
        return coordinates[tileIndex][1];
    }

}
