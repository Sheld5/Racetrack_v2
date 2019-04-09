package main;

import org.xml.sax.SAXException;
import model.*;
import util.AICompiler;
import util.MapReader;
import util.StartNotFoundException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

public class Game extends JPanel implements KeyListener {

    private final static int MAP_INDENT = 16;
    private final static int TURN_MAX = 500;

    private int tileSize;

    private JPanel scoreMainPanel;
    private JScrollPane scoreScrollPane;
    private JLabel turnLabel;
    private JButton back;

    private Map map;
    private Car[] cars;
    private CrossHair[][] ch;
    private Checkpoint[] checkpoints;
    private int activeCarIndex;
    private Car activeCar;
    private boolean stop;
    private int turn;
    private int[] nextAiMove;
    private boolean aiWaiting;

    Game(Menu menu) throws IOException, StartNotFoundException, SAXException, ParserConfigurationException {
        init();
        initCrossHair();
        initCars(menu.getCarPanels());
        initMap(menu.getMapName());
        initScorePanel();
        initGUI();
        addComponents();

        moveCarsToStart();
        initCheckpoints(menu.getCarPanels().size());
        activeCarIndex = cars.length - 1;
        stop = false;
        if (cars.length == 1) {
            turn = -1;
        } else {
            turn = 0;
        }
        nextAiMove = new int[]{0,0};
        aiWaiting = false;

        System.out.println("Game initialized successfully");

        initRace();

        scoreMainPanel.add(new ScorePanel("Player", "INTELLIGENCE", 23));
        scoreMainPanel.add(new ScorePanel("Player", "INTELLIGENCE", 23));
        showScore();
    }

    private void addComponents() {
        add(scoreScrollPane);
        addCrossHairs();
        addCars();
        add(map);
        addGUI();
    }

    private void addCrossHairs() {
        for (CrossHair[] cLine : ch) {
            for(CrossHair c : cLine) {
                add(c);
            }
        }
    }

    private void addCars() {
        for (Car car : cars) {
            add(car);
        }
    }

    private void addGUI() {
        add(turnLabel);
        add(back);
    }

    private void init() {
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        tileSize = 24;
        addKeyListener(this);
    }

    @SuppressWarnings("Duplicates")
    private void initScorePanel() {
        int scoreWidth = 500;
        int scoreHeight = 500;
        scoreMainPanel = new JPanel();
        scoreMainPanel.setLayout(new BoxLayout(scoreMainPanel, BoxLayout.Y_AXIS));
        scoreMainPanel.setBounds(MAP_INDENT + map.getWidth() / 2 - scoreWidth / 2, MAP_INDENT + map.getHeight() / 2 - scoreHeight / 2, scoreWidth, scoreHeight);
        scoreMainPanel.setBackground(Color.lightGray);
        scoreScrollPane = new JScrollPane(scoreMainPanel);
        scoreScrollPane.setVisible(false);
        scoreScrollPane.setBackground(Color.black);
        scoreScrollPane.setBounds(scoreMainPanel.getBounds());
    }

    private void initGUI() {
        Font fontBig = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        Font fontSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        int guiX = map.getX() + map.getWidth() + MAP_INDENT;

        turnLabel = new JLabel("Turn: " + turn);
        turnLabel.setVisible(true);
        turnLabel.setBounds(guiX, MAP_INDENT,120, 50);
        turnLabel.setFont(fontBig);
        turnLabel.setForeground(Color.orange);

        back = new JButton("Back");
        back.setVisible(true);
        back.addActionListener(e -> Main.goToMenu());
        back.setBounds(guiX, turnLabel.getY() + turnLabel.getHeight() + MAP_INDENT, 96, 32);
        back.setFont(fontSmall);
        back.setForeground(Color.black);
    }

    private void initMap(String mapName) throws ParserConfigurationException, SAXException, IOException, StartNotFoundException {
        MapReader mr = new MapReader();
        map = new Map(mr.getData(mapName), mr.getMapSizeX(), mr.getMapSizeY(), mr.getTileSet("RacetrackTileSet.tsx"), this);
        map.setLocation(MAP_INDENT, MAP_INDENT);
        map.setVisible(true);
        add(map);

        System.out.println("map initialized");
    }

    private void initCars(ArrayList<CarPanel> carPanels) throws IOException {
        AICompiler aiCompiler = new AICompiler();
        cars = new Car[carPanels.size()];
        int i = 0;
        for (CarPanel panel : carPanels) {
            if (panel.getAiFile() == null) {
                cars[i] = new Car(panel.getPlayerName(), panel.getAiName(), panel.getCarColor(), null, this);
            } else {
                cars[i] = new Car(panel.getPlayerName(), panel.getAiName(), panel.getCarColor(), aiCompiler.compile(panel), this);
            }
            add(cars[i]);
            i++;
        }
        System.out.printf("%d cars initiated\n", cars.length);
    }

    private void initCrossHair() {
        ch = new CrossHair[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ch[x][y] = new CrossHair(new int[]{x - 1,y - 1}, this);
                ch[x][y].setVisible(false);
            }
        }

        System.out.println("crosshair initialized");
    }

    private void initCheckpoints(int numberOfCars) {

        checkpoints = new Checkpoint[0];
        boolean foundCheckpoint = false;

        for (int x = 0; x < map.getWidthInTiles(); x++) {
            for (int y = 0; y < map.getHeightInTiles(); y++) {

                if (map.getTile(x, y) == Tile.CHECKPOINT) {
                    for (Checkpoint ch : checkpoints) {
                        for (int i = 0; i < ch.getNoOfTiles(); i++) {
                            if ((ch.getXOfTile(i) - 1 <= x) && (x <= ch.getXOfTile(i) + 1) && (ch.getYOfTile(i) - 1 <= y) && (y <= ch.getYOfTile(i) + 1)) {
                                ch.addTile(x, y);
                                foundCheckpoint = true;
                            }
                            if (foundCheckpoint) {
                                break;
                            }
                        }
                        if (foundCheckpoint) {
                            break;
                        }
                    }

                    if (!foundCheckpoint) {
                        Checkpoint[] checkTemp = new Checkpoint[checkpoints.length + 1];
                        System.arraycopy(checkpoints, 0, checkTemp, 0, checkpoints.length);
                        Checkpoint ch = new Checkpoint(x, y, numberOfCars);
                        checkTemp[checkTemp.length - 1] = ch;
                        checkpoints = checkTemp;
                    }

                    foundCheckpoint = false;
                }

            }
        }

        System.out.println(checkpoints.length + " checkpoints initialized");

    }

    private void moveCarsToStart() {
        for (Car car : cars) {
            moveCar(car, map.getStart()[0], map.getStart()[1]);
        }
        moveCH(map.getStart()[0], map.getStart()[1]);
    }



    private void initRace() {
        for (Car car : cars) {
            if (car.getDriver() != null) {
                car.getDriver().init(map.getMapCopy());
            }
        }
        nextTurn();
    }

    // manages the turn cycle of the cars and calls the drive() method each turn of each car
    private void nextTurn() {
        if (activeCarIndex == 0) {
            turn++;
            updateTurnCount();
        }
        if (allCarsIdle()) {
            endRace();
        } else if (turn > TURN_MAX) {
            System.out.println("Turn limit reached!");
        } else {
            nextCar();
            if (activeCar.isCrashed()) {
                activeCar.countdown();
                nextTurn();
            } else if (map.getTile(activeCar.getCoordinates()) == Tile.ICE && (activeCar.getVelX() != 0 || activeCar.getVelY() != 0)) {
                drive(activeCar, new int[]{0,0});
                nextTurn();
            } else {
                showCH();
                if (!humanOnTurn()) {
                    nextAiMove = activeCar.getDriver().drive(activeCar.getCoordinates(), activeCar.getVelocity(), map.getMapCopy());
                    showNextAiMove(true);
                    aiWaiting = true;
                }
                // In case a human player is on turn, wait for their input from CrossHair. ( onCHClick() has to be called )
                // In case an AI player is on turn, wait for ENTER to be pressed. ( keyPressed(VK_ENTER) has to be called )
            }
        }
    }

    public void onCHClick(int[] index) {
        if (humanOnTurn()) {
            hideCH();
            drive(activeCar, index);
            nextTurn();
        }
    }

    // changes the velocities of the cars and calls the goThroughPath() function
    private void drive(Car car, int[] a) {
        car.accelerate(a);
        goThroughPath(car);
    }

    // goes through the path of the car tile by tile and calls the checkTile() function
    @SuppressWarnings("Duplicates")
    private void goThroughPath(Car car) {

        int initX = car.getTileX();
        int initY = car.getTileY();
        int targetX = initX + car.getVelX();
        int targetY = initY + car.getVelY();
        int dirX = Integer.compare(targetX, initX);
        int dirY = Integer.compare(targetY, initY);

        if (initX == targetX) {
            for (int y = initY + dirY; y - dirY != targetY; y += dirY) {
                checkTile(car, initX, y);
                if (stop) {
                    break;
                }
            }

        } else if (initY == targetY) {
            for (int x = initX + dirX; x - dirX != targetX; x += dirX) {
                checkTile(car, x, initY);
                if (stop) {
                    break;
                }
            }

        } else if (abs(initX - targetX) == abs(initY - targetY)) {
            int x, y;
            for (int i = 1; i <= abs(initX - targetX); i++) {
                x = initX + dirX * i;
                y = initY + dirY * i;
                checkTile(car, x, y);
                if (stop) {
                    break;
                }
            }

        } else {
            int a = -(targetY - initY);
            int b = targetX - initX;
            int c = - a * initX - b * initY;
            boolean firstTile = true;

            if (abs(initX - targetX) > abs(initY - targetY)) {
                for (int x = initX; x - dirX != targetX; x += dirX) {
                    for (int y = initY; y - dirY != targetY; y += dirY) {
                        if (firstTile) {
                            firstTile = false;
                        } else {
                            checkTile(car, x, y, a, b, c);
                        }
                        if (stop) {
                            break;
                        }
                    }
                    if (stop) {
                        break;
                    }
                }
            } else {
                for (int y = initY; y - dirY != targetY; y += dirY) {
                    for (int x = initX; x - dirX != targetX; x += dirX) {
                        if (firstTile) {
                            firstTile = false;
                        } else {
                            checkTile(car, x, y, a, b, c);
                        }
                        if (stop) {
                            break;
                        }
                    }
                    if (stop) {
                        break;
                    }
                }
            }
        }

        stop = false;
    }

    // checks next tile for its rideability and moves the car accordingly
    @SuppressWarnings("Duplicates")
    private void checkTile(Car car, int x, int y) {
        if (map.isTileRideable(x, y)) {
            moveCar(car, x, y);
            checkForSpecialTiles(car, x, y);
        } else {
            onCarCrash(car);
        }
    }

    @SuppressWarnings("Duplicates")
    private void checkTile(Car car, int x, int y, int a, int b, int c) {
        if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
            if (map.isTileRideable(x, y)) {
                moveCar(car, x, y);
                checkForSpecialTiles(car, x, y);
            } else {
                onCarCrash(car);
            }
        }
    }



    private void onCarCrash(Car car) {
        car.setVelocity(new int[]{0,0});
        car.crashed();
        stop = true;
    }

    private void checkForSpecialTiles(Car car, int x, int y) {
        checkForCheckpoint(x, y);
        checkForFinish(car, x, y);
        checkForSand(car, x, y);
        checkForWater(car, x, y);
    }

    private void checkForCheckpoint(int x, int y) {
        if (map.getTile(x, y) == Tile.CHECKPOINT) {
            for (int i = 0; i < checkpoints.length; i++) {
                if (checkpoints[i].tileBelongsTo(x, y) && !checkpoints[i].getCarPassed(activeCarIndex)) {
                    checkpoints[i].carPassed(activeCarIndex);
                    System.out.println("car" + activeCarIndex + " passed checkpoint" + i);
                }
                if (checkpoints[i].tileBelongsTo(x, y)) {
                    break;
                }
            }
        }
    }

    private void checkForFinish(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.FINISH) {
            for (Checkpoint ch : checkpoints) {
                if (!ch.getCarPassed(activeCarIndex)) {
                    return;
                }
            }
            car.finished();
            scoreMainPanel.add(new ScorePanel(car.getPlayerName(), car.getAiName(), turn));
            System.out.println("Car" + activeCarIndex + " finished the race!");
        }
    }

    private void checkForSand(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.SAND) {
            car.setVelocity(new int[]{0,0});
            stop = true;
        }
    }

    private void checkForWater(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.WATER) {
            car.sunk();
            stop = true;
            System.out.println("Car" + activeCarIndex + " sunk!");
        }
    }



    private void moveCar(Car car, int x, int y) {
        car.setCoordinates(x,y);
        car.setLocation(MAP_INDENT + x * tileSize, MAP_INDENT + y * tileSize);
    }

    private void moveCH(int x, int y) {
        for (CrossHair[] cLine : ch) {
            for (CrossHair c : cLine) {
                c.setTileXY(x + c.getIndex()[0], y + c.getIndex()[1]);
                c.setLocation(MAP_INDENT + c.getTileX() * tileSize, MAP_INDENT + c.getTileY() * tileSize);
            }
        }
    }

    private void showCH() {
        moveCH(activeCar.getTileX() + activeCar.getVelX(), activeCar.getTileY() + activeCar.getVelY());
        for (CrossHair[] cLine : ch) {
            for (CrossHair c : cLine) {
                c.setVisible(true);
            }
        }
    }

    private void hideCH() {
        for (CrossHair[] cLine : ch) {
            for (CrossHair c : cLine) {
                c.setVisible(false);
            }
        }
        showNextAiMove(false);
    }

    private void showNextAiMove(boolean b) {
        for (CrossHair[] cLine : ch) {
            for (CrossHair c : cLine) {
                if (b) {
                    if (c.getIndex()[0] == nextAiMove[0] && c.getIndex()[1] == nextAiMove[1]) {
                        c.setIsNextAiMove(true);
                    } else {
                        c.setIsNextAiMove(false);
                    }
                } else {
                    c.setIsNextAiMove(false);
                }
            }
        }
        repaint();
    }

    private boolean allCarsIdle() {
        for (Car c : cars) {
            if (!c.isFinished() && !c.isSunk()) {
                return false;
            }
        }
        return true;
    }

    private void nextCar() {
        rotateCar();
        while (cars[activeCarIndex].isSunk() || cars[activeCarIndex].isFinished()) {
            rotateCar();
        }
        activeCar = cars[activeCarIndex];
    }

    private void rotateCar() {
        if (activeCarIndex < cars.length - 1) {
            activeCarIndex++;
        } else {
            activeCarIndex = 0;
        }
    }

    private void endRace() {
        showScore();
        System.out.println("Race finished");
    }

    public boolean humanOnTurn() {
        return (activeCar.getDriver() == null);
    }

    private void updateTurnCount() {
        turnLabel.setText("Turn: " + turn);
    }


    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
          if (!humanOnTurn() && aiWaiting) {
              aiWaiting = false;
              hideCH();
              drive(activeCar, nextAiMove);
              nextTurn();
          }
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            if (tileSize > 4) {
                tileSize -= 4;
                updateView();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            if (tileSize < 64) {
                tileSize += 4;
                updateView();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    private void updateView() {
        for (Car car : cars) {
            moveCar(car, car.getTileX(), car.getTileY());
        }
        moveCH(ch[1][1].getTileX(), ch[1][1].getTileY());
        repaint();
        moveGUI();
    }

    private void moveGUI() {
        int guiX = MAP_INDENT + map.getWidthInTiles() * tileSize + MAP_INDENT;
        turnLabel.setBounds(guiX, MAP_INDENT,50, 50);
        back.setBounds(guiX, turnLabel.getY() + turnLabel.getHeight() + MAP_INDENT, 96, 32);
    }

    public int getTileSize() {
        return tileSize;
    }

    private void showScore() {
        scoreScrollPane.setVisible(true);
        repaint();
    }

}
