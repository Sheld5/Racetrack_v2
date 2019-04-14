package model;

public class ProximityAI implements DriverAI {

    @Override
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        return new int[0];
    }

    private int[][] proximityMap;

    @Override
    public void init(Tile[][] map) {
        initProximityMap(map);
    }

    private void initProximityMap(Tile[][] map) {

    }
}