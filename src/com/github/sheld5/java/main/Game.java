package main;

import org.xml.sax.SAXException;
import model.*;
import util.AICompiler;
import util.StartNotFoundException;

import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

/**
 * Manages the game, all components of the game, the turn cycle of the game, all game logic, game GUI and view.
 */
public class Game extends JPanel implements KeyListener {

    private final static int MAP_INDENT = 16;
    private final static int TURN_MAX = 500;
    private final static String TILE_SET_FILE_NAME = "RacetrackTileSet.tsx";

    private int tileSize;

    private JPanel scoreMainPanel;
    private JScrollPane scoreScrollPane;
    private JLabel turnLabel;
    private JButton back;

    private Map map;
    private Car[] cars;
    private CrosshairTile[][] ch;
    private Checkpoint[] checkpoints;
    private int activeCarIndex;
    private Car activeCar;
    private boolean stop;
    private int turn, carsFinished;
    private int[] nextAiMove;
    private boolean aiWaiting;

    /**
     * Initializes the game with information using get-methods of the menu instance given to it as parameter.
     * Calls other init-methods to initialize different parts of the class.
     * @param menu the instance of Menu from which the game should gather information to initialize the game.
     * @throws IOException
     * @throws StartNotFoundException
     * @throws SAXException
     * @throws ParserConfigurationException
     * @see Menu
     * @see Game#init()
     * @see Game#initCrosshair()
     * @see Game#initCars(ArrayList)
     * @see Game#initMap(String)
     * @see Game#initScorePanel()
     * @see Game#initGUI()
     * @see Game#addComponents()
     * @see Game#moveCarsToStart()
     * @see Game#initCheckpoints(int)
     * @see Game#initRace()
     */
    Game(Menu menu) throws IOException, StartNotFoundException, SAXException, ParserConfigurationException {
        tileSize = 24;
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
     * Initializes the JPanel attributes of Game.
     */
    private void init() {
        setBackground(Color.BLACK);
        setLayout(null);
        setFocusable(true);
        requestFocusInWindow();
        addKeyListener(this);
    }

    /**
     * Initializes the JPanel used to show post-game score.
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
        ScorePanel heading = new ScorePanel(0, "", "", 0);
        heading.makeIntoHeading();
        scoreMainPanel.add(heading);
    }

    /**
     * Initializes the JLabel showing the turn count and the JButton used to go back to menu.
     */
    private void initGUI() {
        Font fontBig = new Font(Font.SANS_SERIF, Font.BOLD, 24);
        Font fontSmall = new Font(Font.SANS_SERIF, Font.PLAIN, 16);
        int guiX = map.getX() + map.getWidth() + MAP_INDENT;

        turnLabel = new JLabel(Integer.toString(turn));
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
     * Initializes the map.
     * @param mapName the name of the file from which the map is to be initialized.
     * @throws ParserConfigurationException
     * @throws SAXException
     * @throws IOException
     * @throws StartNotFoundException
     * @see Map
     */
    private void initMap(String mapName) throws ParserConfigurationException, SAXException, IOException, StartNotFoundException {
        map = new Map(mapName, TILE_SET_FILE_NAME, this);
        map.setLocation(MAP_INDENT, MAP_INDENT);
        map.setVisible(true);
        add(map);

        System.out.println("map initialized");
    }

    /**
     * Initializes all cars and uses AICompiler to compile and get instances of all AIs for the cars.
     * @param carPanels the array of instances of CarPanel
     *                  from which the settings of cars are to be gathered using their get-methods.
     * @throws IOException
     * @see Car
     * @see DriverAI
     * @see AICompiler
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
     * Initializes crosshair used to get input from the user for the human-controlled cars.
     * @see CrosshairTile
     */
    private void initCrosshair() {
        ch = new CrosshairTile[3][3];
        for (int x = 0; x < 3; x++) {
            for (int y = 0; y < 3; y++) {
                ch[x][y] = new CrosshairTile(new int[]{x - 1,y - 1}, this);
                ch[x][y].setVisible(false);
            }
        }

        System.out.println("crosshair initialized");
    }

    /**
     * Initializes checkpoints. Treats more CHECKPOINT tiles next to each other as one checkpoint.
     * @param numberOfCars the number of instances of Car in the game.
     * @see Checkpoint
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
     * Adds all components to the game in correct order
     * so the correct ones are in the foreground and the background.
     */
    private void addComponents() {
        add(scoreScrollPane);
        addCrosshair();
        addCars();
        add(map);
        addGUI();
    }

    /**
     * Adds the crosshair (used to get input from human players) to the game.
     */
    private void addCrosshair() {
        for (CrosshairTile[] cLine : ch) {
            for(CrosshairTile c : cLine) {
                add(c);
            }
        }
    }

    /**
     * Adds all cars to the game.
     */
    private void addCars() {
        for (Car car : cars) {
            add(car);
        }
    }

    /**
     * Adds the JLabel showing the turn count and the JButton used to go back to menu.
     */
    private void addGUI() {
        add(turnLabel);
        add(back);
    }

    /**
     * Relocates all cars to the start.
     */
    private void moveCarsToStart() {
        for (Car car : cars) {
            moveCar(car, map.getStart()[0], map.getStart()[1]);
        }
        moveCH(map.getStart()[0], map.getStart()[1]);
    }


    /**
     * Calls the init() method of each AI in the game
     * and then calls the nextTurn() method to start the first turn.
     * @see DriverAI#init(Tile[][])
     * @see Game#nextTurn()
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
     * Is called when new turn is to begin (turn is treated here as one move of one car).
     * Cycles through cars so different one is on turn each time.
     * Increases the turn count by one every time the game cycles through all cars.
     * Determines whether a human or an AI is on turn.
     * If an AI is on turn, calls its drive() method to determine its next move,
     * shows it by highlighting the corresponding crosshair tile
     * and waits for the user to press ENTER (keyPressed() is called),
     * which is going to call nextTurn() again.
     * If a human is on turn, waits for the crosshair to be clicked (onCHClick() is called),
     * which is going to call nextTurn() again.
     * Calls itself recursively to maintain the turn cycle.
     * Calls the endRace() method to end the race when all cars are finished or sunk or the turn-max is reached.
     * @see Game#updateTurnCount()
     * @see Game#nextCar()
     * @see Game#keyPressed(KeyEvent)
     * @see Game#onCHClick(int[])
     * @see Game#endRace()
     * @see DriverAI#drive(int[], int[], Tile[][])
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
                // in case a human player is on turn, wait for their input from CrosshairTile ( onCHClick() has to be called )
                // in case an AI player is on turn, show the decision the AI made and wait for ENTER to be pressed ( keyPressed(VK_ENTER) has to be called )
            }
        }
    }

    /**
     * Is called when crosshair is clicked.
     * Receives the index of the clicked crosshair tile as parameter.
     * Calls drive() method and passes the crosshair index to it as parameter.
     * The crosshair index corresponds to the acceleration vector,
     * which is to be added to the velocity vector of the car.
     * Then calls nextTurn() to start the next turn.
     * @param index the index of the clicked crosshair tile.
     * @see CrosshairTile#mouseClicked(MouseEvent)
     * @see Game#drive(Car, int[])
     * @see Game#nextTurn()
     */
    public void onCHClick(int[] index) {
        if (humanOnTurn()) {
            hideCH();
            drive(activeCar, index);
            nextTurn();
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * Is called when a key on the keyboard is pressed.
     * MINUS and PLUS keys zoom in and out of the game view.
     * When ENTER is pressed, if AI is on turn and waiting for the user,
     * the turn of the AI is performed by calling the drive() method
     * and nextTurn() method is called to begin the next turn.
     * @param e
     * @see Game#drive(Car, int[])
     * @see Game#nextTurn()
     */
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

    /**
     * Changes the velocity vector of the car according to the move made by the player
     * given to this method as the int[] a parameter using the Car.accelerate(int[] a) method
     * and calls the goThroughPath() method to make the car travel through the path
     * it is to take this turn according to its velocity vector tile by tile.
     * @param car the car which is to be driven.
     * @param a the acceleration vector which is to be added to the velocity vector of the car before it is driven.
     * @see Car#accelerate(int[])
     * @see Game#goThroughPath(Car)
     */
    private void drive(Car car, int[] a) {
        car.accelerate(a);
        goThroughPath(car);
    }

    /**
     * Finds the straightest and shortest path from the current location of the car to the target location.
     * (target location = current location + car velocity vector)
     * Car then moves tile by tile through the path. It can move to any adjacent tile including diagonal ones.
     * checkTile() method is called for each tile the car is to move over to determine if the car can move there
     * and if any special actions are to be made on that tile.
     * Stops going through tiles if the car crashes (drives into a wall) or sinks (drives into water).
     * @param car the car which is to change its position by adding its velocity vector to it
     *            and go through all tiles in the path.
     * @see Car
     * @see Game#checkTile(Car, int, int)
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
     * Checks whether the tile is rideable using the Map.isTileRideable(int x, int y)
     * and moves the car there or calls the onCarCrash() method accordingly.
     * Then calls the checkForSpecialTiles() method to check if any special actions are to be made on this tile.
     * @param car the car which is to be moved to the tile or crashed depending on the type of the tile.
     * @param x the X coordinate of the tile which is to be checked.
     * @param y the Y coordinate of the tile which is to be checked.
     * @see Map#isTileRideable(int, int)
     * @see Game#moveCar(Car, int, int)
     * @see Game#onCarCrash(Car)
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
     * Sets the velocity vector of the car to (0;0).
     * Calls the car.crashed() method.
     * Sets boolean stop to true to stop the goThroughPath() method from going through any more tiles.
     * @param car the car that crashed.
     * @see Car#setVelocity(int[])
     * @see Car#crashed()
     */
    private void onCarCrash(Car car) {
        car.setVelocity(new int[]{0,0});
        car.crashed();
        stop = true;
    }

    /**
     * Calls all methods which check for special tiles.
     * @param car the car which has driven over the tile
     *            and which will be affected by the special actions if there are any on the tile.
     * @param x the X coordinate of the tile which is to be checked for special actions.
     * @param y the Y coordinate of the tile which is to be checked for special actions.
     * @see Game#checkForCheckpoint(int, int)
     * @see Game#checkForFinish(Car, int, int)
     * @see Game#checkForSand(Car, int, int)
     * @see Game#checkForWater(Car, int, int)
     */
    private void checkForSpecialTiles(Car car, int x, int y) {
        checkForCheckpoint(x, y);
        checkForFinish(car, x, y);
        checkForSand(car, x, y);
        checkForWater(car, x, y);
    }

    /**
     * Checks whether the tile is checkpoint and saves that the car has passed this checkpoint
     * using the Checkpoint.carPassed(int carIndex) if so.
     * @param x the X coordinate of the tile which is to be checked.
     * @param y the Y coordinate of the tile which is to be checked.
     * @see Checkpoint
     * @see Checkpoint#carPassed(int)
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
     * Checks whether the tile is finish and if the car has passed all checkpoints.
     * Calls the Car.finished() method and creates new instance of CarPanel
     * to display the score for the finished car.
     * @param car the car which had driven over the tile.
     * @param x the X coordinate of the tile which is to be checked.
     * @param y the Y coordinate of the tile which is to be checked.
     * @see Car#finished()
     * @see CarPanel
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
     * Checks whether the tile is sand and sets the car velocity vector to (0;0)
     * using the Car.setVelocity(int[] velocity) method if so.
     * @param car the car which has driven over the tile.
     * @param x the X coordinate of the tile which is to be checked.
     * @param y the Y coordinate of the tile which is to be checked.
     * @see Car#setVelocity(int[])
     */
    private void checkForSand(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.SAND) {
            car.setVelocity(new int[]{0,0});
            stop = true;
        }
    }

    /**
     * Checks whether the tile is water and sets the car as "sunk"
     * using the Car.sunk() method if so.
     * (Sunk car cannot continue the race and is DQed.)
     * @param car the car which has driven over the tile.
     * @param x the X coordinate of the tile which is to be checked.
     * @param y the Y coordinate of the tile which is to be checked.
     * @see Car#sunk()
     */
    private void checkForWater(Car car, int x, int y) {
        if (map.getTile(x, y) == Tile.WATER) {
            car.sunk();
            stop = true;
            System.out.println("Car" + activeCarIndex + " sunk!");
        }
    }



    /**
     * Moves the given car to the given coordinates.
     * @param car the car to be moved.
     * @param x the X coordinate to which the car is to be moved.
     * @param y the Y coordinate to which the car is to be moved.
     * @see Car
     */
    private void moveCar(Car car, int x, int y) {
        car.setCoordinates(x,y);
        car.setLocation(MAP_INDENT + x * tileSize, MAP_INDENT + y * tileSize);
    }

    /**
     * Moves the crosshair to the given coordinates.
     * @param x the X coordinate to which the crosshair is to be moved.
     * @param y the Y coordinate to which the crosshair is to be moved.
     * @see CrosshairTile
     */
    private void moveCH(int x, int y) {
        for (CrosshairTile[] cLine : ch) {
            for (CrosshairTile c : cLine) {
                c.setTileXY(x + c.getIndex()[0], y + c.getIndex()[1]);
                c.setLocation(MAP_INDENT + c.getTileX() * tileSize, MAP_INDENT + c.getTileY() * tileSize);
            }
        }
    }

    /**
     * Shows the crosshair.
     * @see CrosshairTile
     */
    private void showCH() {
        moveCH(activeCar.getTileX() + activeCar.getVelX(), activeCar.getTileY() + activeCar.getVelY());
        for (CrosshairTile[] cLine : ch) {
            for (CrosshairTile c : cLine) {
                c.setVisible(true);
            }
        }
    }

    /**
     * Hides the crosshair.
     * @see CrosshairTile
     */
    private void hideCH() {
        for (CrosshairTile[] cLine : ch) {
            for (CrosshairTile c : cLine) {
                c.setVisible(false);
            }
        }
        showNextAiMove(false);
    }

    /**
     * Shows or hides the red highlight of the next AI move on the crosshair.
     * @param b the boolean to which is the value of visibility of the highlight to be set.
     * @see CrosshairTile#setIsNextAiMove(boolean)
     */
    private void showNextAiMove(boolean b) {
        for (CrosshairTile[] cLine : ch) {
            for (CrosshairTile c : cLine) {
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
     * Returns true if all cars are finished or sunk.
     * @return true if all cars are finished or sunk
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
     * Keeps calling the rotateCar() method until the activeCar is a car which has not finished or sunk yet.
     * Then calls moveActiveCarToForeground() method.
     * @see Game#rotateCar()
     * @see Game#moveActiveCarToForeground()
     */
    private void nextCar() {
        rotateCar();
        while (cars[activeCarIndex].isSunk() || cars[activeCarIndex].isFinished()) {
            rotateCar();
        }
        moveActiveCarToForeground();
    }

    /**
     * Rotates activeCarIndex and activeCar to the next car.
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
     * Moves the activeCar to the foreground so it is not covered up by other cars.
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
     * Is called when the race is finished.
     * Adds score panels for all cars which have not been able to finish the race.
     * Shows the post-game score board.
     * @see ScorePanel
     * @see Game#showScore()
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
     * Returns true if human player is on turn.
     * @return true if human player is on turn.
     */
    public boolean humanOnTurn() {
        return (activeCar.getDriver() == null);
    }

    /**
     * Updates the text of JPanel turnLabel to show the correct value.
     */
    private void updateTurnCount() {
        turnLabel.setText(Integer.toString(turn));
    }



    /**
     * Repaint components of the game with correct sizes and positions.
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
     *  Move the turn count and back button to the correct position.
     */
    private void moveGUI() {
        int guiX = MAP_INDENT + map.getWidthInTiles() * tileSize + MAP_INDENT;
        turnLabel.setBounds(guiX, MAP_INDENT,50, 50);
        back.setBounds(guiX, turnLabel.getY() + turnLabel.getHeight() + MAP_INDENT, 96, 32);
    }

    /**
     * Returns the size of one tile of the map in pixels.
     * @return the size of one tile of the map in pixels.
     */
    public int getTileSize() {
        return tileSize;
    }

    /**
     * Shows the post-game score board.
     */
    private void showScore() {
        scoreScrollPane.setVisible(true);
        repaint();
    }

}
