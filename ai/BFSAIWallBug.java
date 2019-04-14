import java.util.ArrayList;

import static java.lang.Math.*;

/*
     ! ! ! ! ! ! ! ! ! ! ! !
This is an exact copy of BFSAI with the only difference of having a replication of a bug which has occurred
while developing the AI. This bug causes the AI to think that it can "jump" over walls and water.
(The cause of the bug is on lines 478 and 482 where the lastTile condition should be removed.
Originally, the bug was caused by a different mistake, but at that point the AI also was not yet able to take
checkpoints into consideration, so I created this bugged version instead, because I find the bug quite interesting.
     ! ! ! ! ! ! ! ! ! ! ! !
 */

/*
Implementation of DriverAI which uses Breadth-First-Search to find the shortest route to finish.
This AI does take into consideration all special tiles and their functions. (Is able to use sand to brake etc.)
It is guaranteed to always find the fastest path. However, it is only practical for smaller maps
or for maps without much open space (e.g. labyrinth-like maps) as it would take hours to calculate the best path
for a big map with a lot of open space this way.
 */
@SuppressWarnings("Duplicates")
public class BFSAIWallBug implements DriverAI {

    // Goes through the moves generated in init() method to go through the race.
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        step++;
        while (movesToFinish.get(step) == null) {
            step++;
        }
        return movesToFinish.get(step);
    }

    // Saved copy of the map.
    private Tile[][] map;
    // Start coordinates.
    private int[] start;
    // List of all Checkpoints and coordinates of each tile of each Checkpoint.
    private ArrayList<ArrayList<int[]>> checkpoints;
    // List of all currently saved paths.
    private ArrayList<Path> paths;
    // List of all visited Nodes.
    private ArrayList<Node> visitedNodes;
    // Is set to true if a Path which ends in a Finish node has been found.
    // Used to break loops.
    private boolean finishFound;
    // Is set to true if the Node in which the newly generated Path ends has been visited before.
    // Path which ends in a Node visited before will be thrown away.
    private boolean visited;
    // The result of the while loop. Contains moves to finish the race.
    private ArrayList<int[]> movesToFinish;
    // Used to go through 'movesToFinish' step by step in each call of the drive() method.
    private int step;

    // Initializes fields. Handles the while loop to find the shortest path to finish.
    public void init(Tile[][] map) {
        this.map = map;
        findStart();
        findCheckpoints();
        paths = new ArrayList<>();
        paths.add(new Path(start, this));
        visitedNodes = new ArrayList<>();
        finishFound = false;
        visited = false;

        System.out.println("Start: " + start[0] + " " + start[1]);
        System.out.println("Checkpoints: " + checkpoints.size());

        int i = 0;
        // Used to store the Path currently being tested.
        Path tryPath;
        // Goes deeper in the search tree in every iteration.
        // In every iteration, considers each next possible move from each Path from previous iteration. (BFS AI)
        // Throws away new Paths which lead to a Node already visited as these would only be longer.
        while(!finishFound) {
            // ArrayList<> 'paths' is copied to 'tempPaths' at the beginning of the while loop
            // to allow for new Paths to be saved in 'paths'.
            ArrayList<Path> tempPaths = paths;
            paths = new ArrayList<>();
            /*
                FOR each Path from previous iteration:
                    IF the last Node is a special tile:
                        -> make a new Path according to the rules of the special tile.
                    ELSE:
                        -> go through all nine possible next moves and create new Paths for them.
            */
            // Whenever a new Path is created, checks whether the last Node has already been visited
            // and the new Path is thrown away if it has.
            // If Finish is found, all loops are broken and the moves of the Path which leads to Finish are
            for (Path path : tempPaths) {
                if (path.getLastNode().isWater()) {
                    // This path will be deleted as it ends in water.
                } else if (path.getLastNode().isIce()) {
                    // 'nextMove' is null to represent that no move will be made this turn as the Car is on ICE.
                    tryPath = new Path(path, null, map, this);
                    checkForVisited(tryPath);
                } else if (path.getLastNode().getWall() > 0) {
                    Node node = new Node(path.getLastNode().get(0), path.getLastNode().get(1), 0, 0);
                    node.setWall(path.getLastNode().getWall() - 1);
                    // 'nextMove' is null to represent that no move will be made this turn as the Car is crashed.
                    tryPath = new Path(path, node, null);
                    checkForVisited(tryPath);
                } else {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            tryPath = new Path(path, new int[]{dx,dy}, map, this);
                            checkForVisited(tryPath);
                            if (finishFound) {
                                movesToFinish = tryPath.getMoves();
                                break;
                            }
                        }
                        if (finishFound) {
                            break;
                        }
                    }
                }
                if (finishFound) {
                    break;
                }
            }
            i++;
            if (finishFound) {
                System.out.println("Turn " + i + ": Finish found!");
            } else {
                System.out.println("Turn " + i + ": " + paths.size() + " paths");
            }
        }

        step = -1;
    }

    // Checks if the last node of the new path was visited and adds the new Path to 'paths' if not.
    // The Node only counts as visited if it has been visited with the same velocity vector,
    // same checkpoints passed and same after-crash waiting value.
    private void checkForVisited(Path tryPath) {
        for (Node node : visitedNodes) {
            if (compareNodes(tryPath.getLastNode(), node) && compareCheckpoints(tryPath, node) && tryPath.getLastNode().getWall() == node.getWall()) {
                visited = true;
                break;
            }
        }
        if (!visited) {
            Node newNode = tryPath.getLastNode();
            newNode.setCheckpointsPassed(tryPath.getCheckpointsPassed());
            visitedNodes.add(newNode);
            paths.add(tryPath);
        }
        visited = false;
    }

    // Compares two nodes. Returns true if the coordinates and velocity values are the same.
    private boolean compareNodes(Node nodeA, Node nodeB) {
        for (int i = 0; i < 4; i++) {
            if (nodeA.get(i) != nodeB.get(i)) {
                return false;
            }
        }
        return true;
    }

    // Compares checkpoints passed by a Path and checkpoints passed saved in a Node.
    private boolean compareCheckpoints(Path path, Node node) {
        for (int i = 0; i < checkpoints.size(); i++) {
            if (path.getCheckpointsPassed()[i] != node.getCheckpointsPassed()[i]) {
                return false;
            }
        }
        return true;
    }

    // Finds and saves the coordinates of the Start.
    private void findStart() {
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == Tile.START) {
                    start = new int[]{x,y};
                }
            }
        }
    }

    // Finds and saves coordinates of each tile of each Checkpoint.
    // Each Checkpoint is saved as 2D int array, where the first number selects a tile of the Checkpoint
    // and the second number selects a coordinate of the tile.
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
    }

    void finishFound() {
        finishFound = true;
    }

    ArrayList<ArrayList<int[]>> getCheckpoints() {
        return checkpoints;
    }

}




// Contains X and Y coordinates of a tile and VX and VY coordinates of current velocity vector.
// Contains additional fields used to store information about special tiles.
class Node {

    // Contains the coordinates and velocity values.
    private int[] node;
    // Stores information about the Node being ICE or WATER.
    private boolean ice, water;
    // Stores information about the after-crash waiting time.
    private int wall;
    // Used only for saving visited Nodes, not for Nodes in Paths. (Paths have their checkpointsPassed[] lists.)
    private boolean[] checkpointsPassed;

    Node(int x, int y, int vx, int vy) {
        node = new int[]{x,y,vx,vy};
        ice = false;
        water = false;
        wall = 0;
    }

    void setCheckpointsPassed(boolean[] checkpointsPassed) {
        this.checkpointsPassed = checkpointsPassed;
    }

    boolean[] getCheckpointsPassed() {
        return checkpointsPassed;
    }

    int get(int i) {
        return node[i];
    }

    void setIceTrue() {
        ice = true;
    }

    void setWaterTrue() {
        water = true;
    }

    void setWall(int i) {
        wall = i;
    }

    boolean isIce() {
        return ice;
    }

    boolean isWater() {
        return water;
    }

    int getWall() {
        return wall;
    }

}




// Contains list of Nodes (forming the path) and list of moves to go through the path.
class Path {

    // List of Nodes -> the path.
    private ArrayList<Node> path;
    // List of moves to be taken to go through this path.
    private ArrayList<int[]> moves;
    // Used to store information about which checkpoints has this Path crossed.
    private boolean[] checkpointsPassed;

    // Constructor for the first Path instance with only one Node: the Start.
    Path(int[] start, BFSAIWallBug ai) {
        path = new ArrayList<>();
        moves = new ArrayList<>();
        path.add(new Node(start[0], start[1], 0, 0));
        checkpointsPassed = new boolean[ai.getCheckpoints().size()];
        for (boolean b : checkpointsPassed) {
            b = false;
        }
    }

    // Constructor for adding custom Node and Move without calling createNewNode().
    // Is used for handling after-crash waiting.
    Path(Path parentPath, Node nextNode, int[] nextMove) {
        path = new ArrayList<>();
        moves = new ArrayList<>();
        checkpointsPassed = new boolean[parentPath.getCheckpointsPassed().length];
        for (Node node : parentPath.get()) {
            path.add(node);
        }
        for (int[] move : parentPath.getMoves()) {
            moves.add(move);
        }
        for (int i = 0; i < checkpointsPassed.length; i++) {
            checkpointsPassed[i] = parentPath.getCheckpointsPassed()[i];
        }
        path.add(nextNode);
        moves.add(nextMove);
    }

    // Constructs new Path with one more Node than the previous Path given to it as parameter.
    // The new Node is created according to the nextMove given to it as parameter.
    Path(Path parentPath, int[] nextMove, Tile[][] map, BFSAIWallBug ai) {
        path = new ArrayList<>();
        moves = new ArrayList<>();
        checkpointsPassed = new boolean[parentPath.getCheckpointsPassed().length];
        for (Node node : parentPath.get()) {
            path.add(node);
        }
        for (int[] move : parentPath.getMoves()) {
            moves.add(move);
        }
        for (int i = 0; i < checkpointsPassed.length; i++) {
            checkpointsPassed[i] = parentPath.getCheckpointsPassed()[i];
        }
        if (nextMove == null) {
            moves.add(null);
            nextMove = new int[]{0,0};
        } else {
            moves.add(nextMove);
        }
        path.add(createNewNode(nextMove, map, ai));
    }

    // Checks the path which would be taken by the car going from the last Node to the new one.
    // Checks for special tiles in the path using the checkForSpecialTiles() method
    // and return new "special" Node if a special tile is encountered.
    // If no special tile is encountered, creates "normal" Node at the end.
    // (The code for checking the path of the car was taken from the main.Game.goThroughPath() method from the game.)
    @SuppressWarnings("Duplicates")
    private Node createNewNode(int[] nextMove, Tile[][] map, BFSAIWallBug ai) {
        Node last = path.get(path.size() - 1);

        int initX = last.get(0);
        int initY = last.get(1);
        int targetX = initX + last.get(2) + nextMove[0];
        int targetY = initY + last.get(3) + nextMove[1];
        int dirX = Integer.compare(targetX, initX);
        int dirY = Integer.compare(targetY, initY);

        int a = -(targetY - initY);
        int b = targetX - initX;
        int c = - a * initX - b * initY;
        double d = sqrt(a * a + b * b);
        boolean firstTile = true;

        int lastX = initX;
        int lastY = initY;
        Node tryNode;

        if (initX == targetX) {
            lastX = initX;
            for (int y = initY + dirY; y - dirY != targetY; y += dirY) {
                if (y == targetY) {
                    tryNode = checkForSpecialTiles(targetX, targetY, lastX, lastY, map, ai, true, nextMove);
                } else {
                    tryNode = checkForSpecialTiles(initX, y, lastX, lastY, map, ai, false, nextMove);
                }
                if (tryNode != null) {
                    return tryNode;
                }
                lastY = y;
            }
        } else if (initY == targetY) {
            lastY = initY;
            for (int x = initX + dirX; x - dirX != targetX; x += dirX) {
                if (x == targetX) {
                    tryNode = checkForSpecialTiles(targetX, targetY, lastX, lastY, map, ai, true, nextMove);
                } else {
                    tryNode = checkForSpecialTiles(x, initY, lastX, lastY, map, ai, false, nextMove);
                }
                if (tryNode != null) {
                    return tryNode;
                }
                lastX = x;
            }
        } else if (abs(initX - targetX) == abs(initY - targetY)) {
            int x, y;
            for (int i = 1; i <= abs(initX - targetX); i++) {
                x = initX + dirX * i;
                y = initY + dirY * i;
                if (x == targetX && y == targetY) {
                    tryNode = checkForSpecialTiles(targetX, targetY, lastX, lastY, map, ai, true, nextMove);
                } else {
                    tryNode = checkForSpecialTiles(x, y, lastX, lastY, map, ai, false, nextMove);
                }
                if (tryNode != null) {
                    return tryNode;
                }
                lastX = x;
                lastY = y;
            }
        } else if (abs(initX - targetX) > abs(initY - targetY)) {
            for (int x = initX; x - dirX != targetX; x += dirX) {
                for (int y = initY; y - dirY != targetY; y += dirY) {
                    if (firstTile) {
                        firstTile = false;
                        continue;
                    }
                    if (abs(a * x + b * y + c) / d <= 0.5) {
                        if (x == targetX && y == targetY) {
                            tryNode = checkForSpecialTiles(targetX, targetY, lastX, lastY, map, ai, true, nextMove);
                        } else {
                            tryNode = checkForSpecialTiles(x, y, lastX, lastY, map, ai, false, nextMove);
                        }
                        if (tryNode != null) {
                            return tryNode;
                        }
                        lastX = x;
                        lastY = y;
                    }
                }
            }
        } else {
            for (int y = initY; y - dirY != targetY; y += dirY) {
                for (int x = initX; x - dirX != targetX; x += dirX) {
                    if (firstTile) {
                        firstTile = false;
                        continue;
                    }
                    if (abs(a * x + b * y + c) / d <= 0.5) {
                        if (x == targetX && y == targetY) {
                            tryNode = checkForSpecialTiles(targetX, targetY, lastX, lastY, map, ai, true, nextMove);
                        } else {
                            tryNode = checkForSpecialTiles(x, y, lastX, lastY, map, ai, false, nextMove);
                        }
                        if (tryNode != null) {
                            return tryNode;
                        }
                        lastX = x;
                        lastY = y;
                    }
                }
            }
        }

        return new Node(targetX, targetY, last.get(2) + nextMove[0], last.get(3) + nextMove[1]);
    }

    // Checks for special tile. Returns "special" Node if a special tile is encountered. Returns null otherwise.
    @SuppressWarnings("Duplicates")
    private Node checkForSpecialTiles(int x, int y, int lastX, int lastY, Tile[][] map, BFSAIWallBug ai, boolean lastTile, int[] nextMove) {
        try {
            if (map[x][y] == Tile.GRASS) {}
        }  catch (ArrayIndexOutOfBoundsException e) {
            Node node = new Node(lastX, lastY, 0, 0);
            node.setWall(3);
            return node;
        }
        if (lastTile && map[x][y] == Tile.WALL) {
            Node node = new Node(lastX, lastY, 0, 0);
            node.setWall(3);
            return node;
        } else if (lastTile && map[x][y] == Tile.WATER) {
            Node node = new Node(x, y, 0, 0);
            node.setWaterTrue();
            return node;
        } else if (map[x][y] == Tile.SAND) {
            return new Node(x, y, 0, 0);
        } else if (map[x][y] == Tile.FINISH) {
            boolean passedAllCheckpoints = true;
            for (boolean b : checkpointsPassed) {
                if (!b) {
                    passedAllCheckpoints = false;
                    break;
                }
            }
            if (passedAllCheckpoints) {
                ai.finishFound();
                return new Node(x, y, 0, 0);
            }
        } else if (lastTile && map[x][y] == Tile.ICE) {
            Node node = new Node(x, y, path.get(path.size() - 1).get(2) + nextMove[0], path.get(path.size() - 1).get(3) + nextMove[1]);
            node.setIceTrue();
            return node;
        } else if (map[x][y] == Tile.CHECKPOINT) {
            boolean checkpointFound = false;
            for (int i = 0; i < ai.getCheckpoints().size(); i++) {
                for (int[] tile : ai.getCheckpoints().get(i)) {
                    if (x == tile[0] && y == tile[1]) {
                        checkpointsPassed[i] = true;
                        checkpointFound = true;
                        break;
                    }
                }
                if (checkpointFound) {
                    break;
                }
            }
        }
        return null;
    }

    ArrayList<Node> get() {
        return path;
    }

    ArrayList<int[]> getMoves() {
        return moves;
    }

    Node getLastNode() {
        return path.get(path.size() - 1);
    }

    boolean[] getCheckpointsPassed() {
        return checkpointsPassed;
    }

}