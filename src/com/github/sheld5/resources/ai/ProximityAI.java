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
        initFinishProximity();
        findFinishes();
    }

    private void initFinishProximity() {
        finishProximity = new int[map.length][map[0].length];
        for (int x = 0; x < finishProximity.length; x++) {
            for (int y = 0; y < finishProximity[0].length; y++) {
                finishProximity[x][y] = -1;
            }
        }
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