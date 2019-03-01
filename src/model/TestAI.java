package model;

public class TestAI extends DriverAI {

    int dir = 3;
    int last = 1;

    int logic() {
        if (getVelX() == 0 && getVelY() == 0) {
            rotateDir();
            switch (dir) {
                case 0:
                    last = 1;
                    return 1;
                case 1:
                    last = 5;
                    return 5;
                case 2:
                    last = 7;
                    return 7;
                case 3:
                    last = 3;
                    return 3;
            }
        }
        return last;
    }

    private void rotateDir() {
        if (dir >= 3) {
            dir = 0;
        } else {
            dir++;
        }
    }

}
