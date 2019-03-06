package model;

public class TestAI extends DriverAI {

    private int dir = 3;

    protected int logic() {
        if (getVelX() == 0 && getVelY() == 0) {
            rotateDir();
        }
        return dir;
    }

    private void rotateDir() {
        switch (dir) {
            case 1:
                dir = 5;
                break;
            case 5:
                dir = 7;
                break;
            case 7:
                dir = 3;
                break;
            case 3:
                dir = 1;
                break;
        }
    }

}
