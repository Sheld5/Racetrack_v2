import java.util.ArrayList;
import static java.lang.Math.*;

// This is a copy of the ProximityAI except it drives fast.
// Crashes and swims a lot. Also tries to jump over water and walls sometimes.
@SuppressWarnings("Duplicates")
public class ProximityAIReckless implements DriverAI {

    private Tile[][] map;
    private ArrayList<int[]> finishes;
    private ArrayList<ArrayList<int[]>> checkpoints;
    private boolean[] checkpointsPassed;
    private int[][] finishProximityMap;
    private ArrayList<int[][]> checkpointProximityMaps;
    private boolean hasTarget;
    private int target; // -1 if finishes are the target; the value of the index of the checkpoint which is the target otherwise

    @Override
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        if (target != -1) {
            if (checkpointProximityMaps.get(target)[carCoordinates[0]][carCoordinates[1]] == 0) {
                checkpointsPassed[target] = true;
                hasTarget = false;
            }
        }

        if (!hasTarget) {
            if (allCheckpointsPassed()) {
                target = -1;
            } else {
                int value = -1;
                for (int i = 0; i < checkpoints.size(); i++) {
                    if (checkpointsPassed[i] == false) {
                        if (value == -1 || checkpointProximityMaps.get(i)[carCoordinates[0]][carCoordinates[1]] < value) {
                            target = i;
                            value = checkpointProximityMaps.get(i)[carCoordinates[0]][carCoordinates[1]];
                        }
                    }
                }
            }
            hasTarget = true;
            System.out.println("New target: " + target);
        }

        int bestValue = -1;
        int[] move = new int[2];
        for (int x = -1; x <= 1; x++) {
            for (int y = -1; y <= 1; y++) {
                try {
                    if (target == -1) {
                        int tileValue = finishProximityMap[carCoordinates[0] + carVelocity[0] + x][carCoordinates[1] + carVelocity[1] + y];
                        if (tileValue != -1 && (bestValue == -1 || bestValue > tileValue)) {
                            bestValue = tileValue;
                            move = new int[]{x,y};
                        }
                    } else {
                        int tileValue = checkpointProximityMaps.get(target)[carCoordinates[0] + carVelocity[0] + x][carCoordinates[1] + carVelocity[1] + y];
                        if (tileValue != -1 && (bestValue == -1 || bestValue > tileValue)) {
                            bestValue = tileValue;
                            move = new int[]{x,y};
                        }
                    }
                } catch (ArrayIndexOutOfBoundsException e) {}
            }
        }

        return move;
    }

    private boolean allCheckpointsPassed() {
        for (boolean checkpoint : checkpointsPassed) {
            if (!checkpoint) {
                return false;
            }
        }
        return true;
    }

    @Override
    public void init(Tile[][] map) {
        this.map = map;
        findFinishes();
        findCheckpoints();
        initProximityMapForFinishes();
        initProximityMapsForCheckpoints();
        hasTarget = false;
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

    private void findCheckpoints() {
        checkpoints = new ArrayList<>();
        boolean tileAssigned = false;
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == Tile.CHECKPOINT) {
                    for (ArrayList<int[]> checkpoint : checkpoints) {
                        for (int[] tile : checkpoint) {
                            if ((tile[0] - x == 0 || abs(tile[0] - x) == 1) && (tile[1] - y == 0 || abs(tile[1] - y) == 1)) {
                                tileAssigned = true;
                                checkpoint.add(new int[]{x,y});
                            }
                            if (tileAssigned) {
                                break;
                            }
                        }
                        if (tileAssigned) {
                            break;
                        }
                    }
                    if (!tileAssigned) {
                        ArrayList<int[]> newCheckpoint = new ArrayList<>();
                        newCheckpoint.add(new int[]{x,y});
                        checkpoints.add(newCheckpoint);
                    }
                    tileAssigned = false;
                }
            }
        }
        checkpointsPassed = new boolean[checkpoints.size()];
        for (boolean b : checkpointsPassed) {
            b = false;
        }
    }

    private void initProximityMapForFinishes() {
        finishProximityMap = new int[map.length][map[0].length];
        for (int x = 0; x < finishProximityMap.length; x++) {
            for (int y = 0; y < finishProximityMap[0].length; y++) {
                finishProximityMap[x][y] = -1;
            }
        }

        for (int[] finish : finishes) {
            finishProximityMap[finish[0]][finish[1]] = 0;
        }
        for (int[] finish : finishes) {
            checkAdjacent(-1, finish[0], finish[1]);
        }
    }

    private void initProximityMapsForCheckpoints() {
        checkpointProximityMaps = new ArrayList<>();
        for (ArrayList<int[]> checkpoint : checkpoints) {
            checkpointProximityMaps.add(new int[map.length][map[0].length]);
        }
        for (int[][] proximityMap : checkpointProximityMaps) {
            for (int x = 0; x < proximityMap.length; x++) {
                for (int y = 0; y < proximityMap[0].length; y++) {
                    proximityMap[x][y] = -1;
                }
            }
        }

        for (int i = 0; i < checkpoints.size(); i++) {
            for (int[] checkpointTile : checkpoints.get(i)) {
                checkpointProximityMaps.get(i)[checkpointTile[0]][checkpointTile[1]] = 0;
            }
            for (int[] checkpointTile : checkpoints.get(i)) {
                checkAdjacent(i, checkpointTile[0], checkpointTile[1]);
            }
        }
    }

    private void checkAdjacent(int index, int tileX, int tileY) {
        if (index == -1) {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    try {
                        if (map[tileX + x][tileY +  y] != Tile.WALL
                                && map[tileX + x][tileY +  y] != Tile.WATER
                                && map[tileX + x][tileY +  y] != Tile.ICE) {
                            if (finishProximityMap[tileX + x][tileY + y] == -1
                                    || finishProximityMap[tileX + x][tileY +  y] > finishProximityMap[tileX][tileY] + 1) {
                                finishProximityMap[tileX + x][tileY + y] = finishProximityMap[tileX][tileY] + 1;
                                checkAdjacent(index, tileX + x, tileY + y);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {}
                }
            }
        } else {
            for (int x = -1; x <= 1; x++) {
                for (int y = -1; y <= 1; y++) {
                    try {
                        if (map[tileX + x][tileY +  y] != Tile.WALL
                                && map[tileX + x][tileY +  y] != Tile.WATER
                                && map[tileX + x][tileY +  y] != Tile.ICE) {
                            if (checkpointProximityMaps.get(index)[tileX + x][tileY + y] == -1
                                    || checkpointProximityMaps.get(index)[tileX + x][tileY +  y] > checkpointProximityMaps.get(index)[tileX][tileY] + 1) {
                                checkpointProximityMaps.get(index)[tileX + x][tileY + y] = checkpointProximityMaps.get(index)[tileX][tileY] + 1;
                                checkAdjacent(index, tileX + x, tileY + y);
                            }
                        }
                    } catch (ArrayIndexOutOfBoundsException e) {}
                }
            }
        }
    }

    private void printProximityMap(int index) {
        if (index == -1) {
            for (int y = 0; y < finishProximityMap[0].length; y++) {
                for (int x = 0; x < finishProximityMap.length; x++) {
                    if (finishProximityMap[x][y] != -1 && finishProximityMap[x][y] / 10 == 0){
                        System.out.print(" " + finishProximityMap[x][y] + " ");
                    } else {
                        System.out.print(finishProximityMap[x][y] + " ");
                    }
                }
                System.out.println();
            }
        }
        else {
            for (int y = 0; y < map[0].length; y++) {
                for (int x = 0; x < map.length; x++) {
                    if (checkpointProximityMaps.get(index)[x][y] != -1 && checkpointProximityMaps.get(index)[x][y] / 10 == 0){
                        System.out.print(" " + checkpointProximityMaps.get(index)[x][y] + " ");
                    } else {
                        System.out.print(checkpointProximityMaps.get(index)[x][y] + " ");
                    }
                }
                System.out.println();
            }
        }
    }

}