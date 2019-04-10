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

/**
 * manages the game
 */
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
    private Crosshair[][] ch;
    private Checkpoint[] checkpoints;
    private int activeCarIndex;
    private Car activeCar;
    private boolean stop;
    private int turn, carsFinished;
    private int[] nextAiMove;
    private boolean aiWaiting;

    /**
     * initializes the game with information gathered from menu using its get-methods
     * @param menu
     * @throws IOException
     * @throws StartNotFoundException
     * @throws SAXException
     * @throws ParserConfigurationException
     */
    Game(Menu menu) throws IOException, StartNotFoundException, SAXException, ParserConfigurationException {
        init();
        initCrosshair();
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
        carsFinished = 0;
        nextAiMove = new int[]{0,0};
        aiWaiting = false;

        System.out.println("Game initialized successfully");

        initRace();
    }

    /**
     * adds all components in correct order
     */
    private void addComponents() {
        add(scoreScrollPane);
        addCrosshair();
        addCars();
        add(map);
        addGUI();
    }

    /**
     * adds the crosshair (used to get input from human players)
     */
    private void addCrosshair() {
        for (Crosshair[] cLine : ch) {
            for(Crosshair c : cLine) {
                add(c);
            }
        }
    }

    /**
     * adds all cars
     */
    private void addCars() {
        for (Car car : cars) {
            add(car);
        }
    }

    /**
     * adds the turn count and the back button
     */
    private void addGUI() {
        add(turnLabel);
        add(back);
    }

    /**
     * initializes the JPanel attributes of Game
     */
    private void init() {
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        tileSize = 24;
        addKeyListener(this);
    }

    /**
     * initializes the post-game score panel
     */
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

    /**
     * initializes the turn count and the back button
     */
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

    /**
     * initializes the map
     * @param mapName
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws StartNotFoundException
     */
    private void initMap(String mapName) throws ParserConfigurationException, SAXException, IOException, StartNotFoundException {
        MapReader mr = new MapReader();
        map = new Map(mr.getData(mapName), mr.getMapWidth(), mr.getMapHeight(), mr.getTileSet("RacetrackTileSet.tsx"), this);
        map.setLocation(MAP_INDENT, MAP_INDENT);
        map.setVisible(true);
        add(map);

        System.out.println("map initialized");
    }

    /**
     * initializes all cars and uses AICompiler to compile and get instances of all AI players
     * @param carPanels
     * @throws IOException
     */
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

    /**
     * initializes crosshair
     */
    private void initCrosshair() {
        ch = new Crosshair[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ch[x][y] = new Crosshair(new int[]{x - 1,y - 1}, this);
                ch[x][y].setVisible(false);
            }
        }

        System.out.println("crosshair initialized");
    }

    /**
     * initializes checkpoints; treats more CHECKPOINT tiles next to each other as one checkpoint
     * @param numberOfCars
     */
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

    /**
     * relocates all cars to the start
     */
    private void moveCarsToStart() {
        for (Car car : cars) {
            moveCar(car, map.getStart()[0], map.getStart()[1]);
        }
        moveCH(map.getStart()[0], map.getStart()[1]);
    }


    /**
     * calls the init() method of each AI in the game and then calls the nextTurn() method to start the first turn
     */
    private void initRace() {
        for (Car car : cars) {
            if (car.getDriver() != null) {
                car.getDriver().init(map.getMapCopy());
            }
        }
        nextTurn();
    }

    /**
     * is called when new turn is to begin (turn is treated as one move of one car)
     * increases the turn count by one every time the game cycles through all cars
     * determines whether a human or an AI is on turn and obtains their next move accordingly
     * calls itself recursively to maintain the turn cycle
     * calls the endRace() method to end the race when all cars are finished or sunk or the turn-max is reached
     */
    private void nextTurn() {
        if (activeCarIndex == 0) {
            turn++;
            updateTurnCount();
        }
        if (allCarsIdle()) {
            endRace();
        } else if (turn > TURN_MAX) {
            System.out.println("Turn limit reached!");
            endRace();
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
                // in case a human player is on turn, wait for their input from Crosshair ( onCHClick() has to be called )
                // in case an AI player is on turn, show the decision the AI made and wait for ENTER to be pressed ( keyPressed(VK_ENTER) has to be called )
            }
        }
    }

    /**
     * is called when crosshair is clicked; passes the move made by the player to the drive() method
     * @param index
     */
    public void onCHClick(int[] index) {
        if (humanOnTurn()) {
            hideCH();
            drive(activeCar, index);
            nextTurn();
        }
    }

    /**
     * changes the velocities of the car according to the move made by the player and calls the goThroughPath() method
     * @param car
     * @param a
     */
    private void drive(Car car, int[] a) {
        car.accelerate(a);
        goThroughPath(car);
    }

    /**
     * finds the straightest and shortest path from the current location of the car to the target location (target location = current location + car velocity vector)
     * cars can move to any adjacent tile including diagonal ones
     * goes through all tiles in the path of the car and calls the checkTile() method for each
     * stops going through tiles if the car crashed
     * @param car
     */
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
                        } else if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
                            checkTile(car, x, y);
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
                        } else if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
                            checkTile(car, x, y);
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

    /**
     * checks whether the tile is rideable and moves the car there or calls the onCarCrash() method accordingly
     * call the checkForSpecialTiles() method
     * @param car
     * @param x
     * @param y
     */
    private void checkTile(Car car, int x, int y) {
        if (map.isTileRideable(x, y)) {
            moveCar(car, x, y);
            checkForSpecialTiles(car, x, y);
        } else {
            onCarCrash(car);
        }
    }

    /**
     * sets the velocity vector of the car to (0;0)
     * calls the car.crashed() method
     * sets stop to true to stop the goThroughPath() method from going through further
     * @param car
     */
    private void onCarCrash(Car car) {
        car.setVelocity(new int[]{0,0});
        car.crashed();
        stop = true;
    }

    /**
     * calls all methods which check for special tiles
     * @param car
     * @param x
     * @param y
     */
    private void checkForSpecialTiles(Car car, int x, int y) {
        checkForCheckpoint(x, y);
        checkForFinish(car, x, y);
        checkForSand(car, x, y);
        checkForWater(car, x, y);
    }

    /**
     * checks whether the tile is checkpoint and saves that the car has passed this checkpoint if so
     * @param x
     * @param y
     */
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

    /**
     * checks whether the tile is finish and if the car has passed all checkpoints
     * if so, calls the car.finished() method and creates new instance of CarPanel for the finished car
     * @param car
     * @param x
     * @param y
     */
    private void checkForFinish(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.FINISH) {
            for (Checkpoint ch : checkpoints) {
                if (!ch.getCarPassed(activeCarIndex)) {
                    return;
                }
            }
            car.finished();
            carsFinished++;
            scoreMainPanel.add(new ScorePanel(carsFinished, car.getPlayerName(), car.getAiName(), turn));
            System.out.println("Car" + activeCarIndex + " finished the race!");
        }
    }

    /**
     * checks whether the tile is sand and sets the car velocity vector to (0;0) if so
     * @param car
     * @param x
     * @param y
     */
    private void checkForSand(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.SAND) {
            car.setVelocity(new int[]{0,0});
            stop = true;
        }
    }

    /**
     * checks whether the tile is water and sets the car as "sunk" if so
     * (sunk car cannot continue the race and is DQed)
     * @param car
     * @param x
     * @param y
     */
    private void checkForWater(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.WATER) {
            car.sunk();
            stop = true;
            System.out.println("Car" + activeCarIndex + " sunk!");
        }
    }


    /**
     * moves the given car to the given coordinates
     * @param car
     * @param x
     * @param y
     */
    private void moveCar(Car car, int x, int y) {
        car.setCoordinates(x,y);
        car.setLocation(MAP_INDENT + x * tileSize, MAP_INDENT + y * tileSize);
    }

    /**
     * moves the crosshair to the given coordinates
     * @param x
     * @param y
     */
    private void moveCH(int x, int y) {
        for (Crosshair[] cLine : ch) {
            for (Crosshair c : cLine) {
                c.setTileXY(x + c.getIndex()[0], y + c.getIndex()[1]);
                c.setLocation(MAP_INDENT + c.getTileX() * tileSize, MAP_INDENT + c.getTileY() * tileSize);
            }
        }
    }

    /**
     * shows the crosshair
     */
    private void showCH() {
        moveCH(activeCar.getTileX() + activeCar.getVelX(), activeCar.getTileY() + activeCar.getVelY());
        for (Crosshair[] cLine : ch) {
            for (Crosshair c : cLine) {
                c.setVisible(true);
            }
        }
    }

    /**
     * hides the crosshair
     */
    private void hideCH() {
        for (Crosshair[] cLine : ch) {
            for (Crosshair c : cLine) {
                c.setVisible(false);
            }
        }
        showNextAiMove(false);
    }

    /**
     * shows or hides the red next AI move highlight on the crosshair
     * @param b
     */
    private void showNextAiMove(boolean b) {
        for (Crosshair[] cLine : ch) {
            for (Crosshair c : cLine) {
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

    /**
     * returns true if all cars are finished or sunk
     * @return
     */
    private boolean allCarsIdle() {
        for (Car c : cars) {
            if (!c.isFinished() && !c.isSunk()) {
                return false;
            }
        }
        return true;
    }

    /**
     * calls the rotateCar() method until the activeCar is a car which has not finished or sunk yet
     */
    private void nextCar() {
        rotateCar();
        while (cars[activeCarIndex].isSunk() || cars[activeCarIndex].isFinished()) {
            rotateCar();
        }
        moveActiveCarToForeground();
    }

    /**
     * rotates activeCarIndex and activeCar to the next car
     */
    private void rotateCar() {
        if (activeCarIndex < cars.length - 1) {
            activeCarIndex++;
        } else {
            activeCarIndex = 0;
        }
        activeCar = cars[activeCarIndex];
    }

    /**
     * moves the activeCar to the foreground
     */
    private void moveActiveCarToForeground() {
        add(scoreScrollPane);
        addCrosshair();

        for (int i = activeCarIndex; i >= 0; i--) {
            add(cars[i]);
        }
        for (int i = cars.length - 1; i > activeCarIndex; i--) {
            add(cars[i]);
        }

        add(map);
        addGUI();
    }

    /**
     * is called when the race is finished
     * adds score panels for all cars which have not been able to finish the race
     * shows the post-game score board
     */
    private void endRace() {
        for (Car car : cars) {
            if (!car.isFinished()) {
                scoreMainPanel.add(new ScorePanel(-1, car.getPlayerName(), car.getAiName(), turn));
            }
        }
        showScore();
        System.out.println("Race finished");
    }

    /**
     * returns true if human player is on turn
     * @return
     */
    public boolean humanOnTurn() {
        return (activeCar.getDriver() == null);
    }

    /**
     * updates turnLabel to the correct value
     */
    private void updateTurnCount() {
        turnLabel.setText("Turn: " + turn);
    }



    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * is called when a key on the keyboard is pressed
     * acts accordingly to the key pressed
     * @param e
     */
    @Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
            // if AI is on turn and waiting for the user, perform the turn of the AI and call nextTurn()
            if (!humanOnTurn() && aiWaiting) {
                aiWaiting = false;
                hideCH();
                drive(activeCar, nextAiMove);
                nextTurn();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_MINUS) {
            // decrease the size of the map and the components on the map
            if (tileSize > 4) {
                tileSize -= 4;
                updateView();
            }
        } else if (e.getKeyCode() == KeyEvent.VK_PLUS || e.getKeyCode() == KeyEvent.VK_EQUALS) {
            // increase the size of the map and the components on the map
            if (tileSize < 64) {
                tileSize += 4;
                updateView();
            }
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * repaint components with correct sizes and positions
     */
    private void updateView() {
        for (Car car : cars) {
            moveCar(car, car.getTileX(), car.getTileY());
        }
        moveCH(ch[1][1].getTileX(), ch[1][1].getTileY());
        repaint();
        moveGUI();
    }

    /**
     *  move the turn count and back button to the correct position
     */
    private void moveGUI() {
        int guiX = MAP_INDENT + map.getWidthInTiles() * tileSize + MAP_INDENT;
        turnLabel.setBounds(guiX, MAP_INDENT,50, 50);
        back.setBounds(guiX, turnLabel.getY() + turnLabel.getHeight() + MAP_INDENT, 96, 32);
    }

    /**
     * returns the size of one tile of the map in pixels
     * @return
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * shows the post-game score board
     */
    private void showScore() {
        scoreScrollPane.setVisible(true);
        repaint();
    }

}
