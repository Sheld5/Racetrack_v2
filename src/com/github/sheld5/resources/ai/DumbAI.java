import model.Tile;

import java.util.ArrayList;

public class DumbAI implements DriverAI {

    private Tile[][] map;
    private int[][] finishProximity;

    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        return new int[]{0,-1};
    }

    public void init(Tile[][] map) {
        this.map = map;
        finishProximity = new int[map.length][map[0].length];
        findFinishes();
        System.out.println(finishProximity[5][5]);
    }

    private void findFinishes() {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == Tile.FINISH) {
                    finishProximity[x][y] = 0;
                }
            }
        }
    }

}