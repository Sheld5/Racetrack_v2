package model;

public class TestAI implements DriverAI {
    @Override
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        return new int[]{0,-1};
    }

    @Override
    public void init(Tile[][] map) {

    }
}
