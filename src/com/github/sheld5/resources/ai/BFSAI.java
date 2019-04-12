package model;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

// Implementation of DriverAI which uses Breadth-First-Search to find the shortest route to finish.
// This AI does take into consideration all special tiles and their functions.
// Does not go through checkpoints for now.
public class BFSAI implements DriverAI {

    // Goes through the moves generated in init() method to get to the Finish.
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        step++;
        return movesToFinish.get(step);
    }

    // Saved copy of the map.
    private Tile[][] map;
    // Start coordinates.
    private int[] start;
    // Coordinates of all finishes.
    private ArrayList<int[]> finishes;
    // List of all currently saved paths.
    private ArrayList<Path> paths;
    // ArrayList<> 'paths' is copied to 'tempPaths' at the beginning of the while loop
    // to allow for new Paths to be saved in 'paths'.
    private ArrayList<Path> tempPaths;
    // List of all visited Nodes.
    private ArrayList<Node> visitedNodes;
    // Is set to true if a Path which ends in a Finish node has been found.
    // Used to break loops.
    private boolean finishFound;
    // Is set to true if the Node in which the new Path ends has been visited before.
    // Path which ends in a Node visited before will not be saved.
    private boolean visited;
    // The result of the while loop. Contains moves to be returned in drive() method.
    private ArrayList<int[]> movesToFinish;
    // Used to go through 'movesToFinish' step by step in each call of the drive() method.
    private int step;

    // Initializes fields. Handles the while loop to find the shortest path to finish.
    public void init(Tile[][] map) {
        findStartAndFinish(map);
        this.map = map;
        paths = new ArrayList<>();
        paths.add(new Path(start));
        visitedNodes = new ArrayList<>();
        finishFound = false;
        visited = false;

        Path tryPath;
        // Goes deeper in the search tree in every iteration.
        // In every iteration, considers each next possible move from each Path from previous iteration. (BFS AI)
        // Throws away new Paths which lead to a Node already visited as these would only be longer.
        while(!finishFound) {
            // Save Paths from the previous iteration into 'tempPaths' and clear 'paths'.
            tempPaths = deepCopy(paths);
            paths = new ArrayList<>();
            /*
                FOR each Path from previous iteration:
                    IF the last Node is a special tile:
                        -> make a new Path according to the special tile.
                    ELSE:
                        -> go through all nine possible next moves and create new Paths for them.
            */
            // Whenever a new Path is created, checks whether the last Node has already been visited
            // and the new Path is not saved if it has.
            // If Finish is found, all loops are broken.
            for (Path path : tempPaths) {
                if (path.getLastNode().isWater()) {
                    // This path will be deleted.
                } else if (path.getLastNode().isIce()) {
                    tryPath = new Path(path, new int[]{0,0}, map, this);
                    checkForVisited(tryPath);
                } else if (path.getLastNode().getWall() > 0) {
                    tryPath = new Path(path, new int[]{0,0}, map, this);
                    checkForVisited(tryPath);
                } else {
                    for (int dx = -1; dx <= 1; dx++) {
                        for (int dy = -1; dy <= 1; dy++) {
                            tryPath = new Path(path, new int[]{dx,dy}, map, this);
                            checkForVisited(tryPath);
                            if (finishFound) {
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
        }

        step = -1;
    }

    // Checks if the last node of the new path was visited and adds the new Path to 'paths' if not.
    private void checkForVisited(Path tryPath) {
        for (Node node : visitedNodes) {
            if (compareNodes(tryPath.getLastNode(), node)) {
                visited = true;
                break;
            }
        }
        if (!visited) {
            visitedNodes.add(tryPath.getLastNode());
            paths.add(tryPath);
        }
        visited = false;
    }

    // Finds and saves the coordinates of the Start and all Finishes.
    private void findStartAndFinish(Tile[][] map) {
        finishes = new ArrayList<>();
        for (int x = 0; x < map.length; x++) {
            for (int y = 0; y < map[0].length; y++) {
                if (map[x][y] == Tile.START) {
                    start = new int[]{x,y};
                } else if (map[x][y] == Tile.FINISH) {
                    finishes.add(new int[]{x,y});
                }
            }
        }
    }

    // Returns deep copy of the ArrayList<Path> given to it as the parameter.
    private ArrayList<Path> deepCopy(ArrayList<Path> original) {
        ArrayList<Path> copy = new ArrayList<>();
        for (Path path : original) {
            copy.add(path);
        }
        return copy;
    }

    // Compares two nodes. Returns true if all attributes have the same values.
    private boolean compareNodes(Node nodeA, Node nodeB) {
        for (int i = 0; i < 4; i++) {
            if (nodeA.get(i) != nodeB.get(i)) {
                return false;
            }
        }
        return true;
    }

    ArrayList<int[]> getFinishes() {
        return finishes;
    }

    void finishFound() {
        finishFound = true;
    }

    void setMovesToFinish(ArrayList<int[]> moves) {
        movesToFinish = moves;
    }

}




// Contains X and Y coordinates of a tile and VX and VY coordinates of current velocity vector.
// Contains additional settings for special tiles.
class Node {

    private int[] node;
    private boolean ice, water;
    private int wall;

    Node(int x, int y, int vx, int vy) {
        node = new int[]{x,y,vx,vy};
        ice = false;
        water = false;
        wall = 0;
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

    // Constructs new Path with one more Node than the previous Path given to it as parameter.
    // The new Node is created according to the nextMove given to it as parameter.
    Path(Path parentPath, int[] nextMove, Tile[][] map, BFSAI ai) {
        path = new ArrayList<>();
        moves = new ArrayList<>();

        for (Node node : parentPath.get()) {
            path.add(node);
        }
        for (int[] move : parentPath.getMoves()) {
            moves.add(move);
        }
        path.add(createNewNode(nextMove, map, ai));
        moves.add(nextMove);
        ai.setMovesToFinish(moves);
    }

    // Constructor for the first Path instance with only one Node: the Start.
    Path(int[] start) {
        path = new ArrayList<>();
        moves = new ArrayList<>();
        path.add(new Node(start[0], start[1], 0, 0));
    }

    // Checks the path which would be taken by the car going from the last Node to the new one.
    // Checks for special tiles in the path using the checkForSpecialTiles() method
    // and return new "special" Node if a special tile is encountered.
    // If no special tile is encountered, creates "normal" Node at the end.
    @SuppressWarnings("Duplicates")
    private Node createNewNode(int[] nextMove, Tile[][] map, BFSAI ai) {
        Node last = path.get(path.size() - 1);

        if (last.getWall() > 0) {
            Node node = new Node(last.get(0), last.get(1), 0, 0);
            node.setWall(last.getWall() - 1);
            return node;
        }

        int initX = last.get(0);
        int initY = last.get(1);
        int targetX = initX + last.get(2) + nextMove[0];
        int targetY = initY + last.get(3) + nextMove[1];
        int dirX = Integer.compare(targetX, initX);
        int dirY = Integer.compare(targetY, initY);

        int a = -(targetY - initY);
        int b = targetX - initX;
        int c = - a * initX - b * initY;
        boolean firstTile = true;

        Node tryNode;
        if (abs(initX - targetX) > abs(initY - targetY)) {
            for (int x = initX; x - dirX != targetX; x += dirX) {
                for (int y = initY; y - dirY != targetY; y += dirY) {
                    if (firstTile) {
                        firstTile = false;
                        continue;
                    }
                    if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
                        tryNode = checkForSpecialTiles(x, y, dirX, dirY, map, ai);
                        if (tryNode != null) {
                            return tryNode;
                        }
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
                    if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
                        tryNode = checkForSpecialTiles(x, y, dirX, dirY, map, ai);
                        if (tryNode != null) {
                            return tryNode;
                        }
                    }
                }
            }
        }
        tryNode = checkForSpecialTiles(targetX, targetY, dirX, dirY, map, ai);
        if (tryNode != null) {
            return tryNode;
        }
        return new Node(targetX, targetY, last.get(2) + nextMove[0], last.get(3) + nextMove[1]);
    }

    // Checks for special tile. Returns "special" Node if a special tile is encountered. Returns null otherwise.
    @SuppressWarnings("Duplicates")
    private Node checkForSpecialTiles(int x, int y, int dirX, int dirY, Tile[][] map, BFSAI ai) {
        if (map[x][y] == null || map[x][y] == Tile.WALL) {
            Node node = new Node(x - dirX, y - dirY, 0, 0);
            node.setWall(3);
            return node;
        } else if (map[x][y] == Tile.WATER) {
            Node node = new Node(x, y, 0, 0);
            node.setWaterTrue();
            return node;
        } else if (map[x][y] == Tile.SAND) {
            return new Node(x, y, 0, 0);
        } else {
            for (int[] finish : ai.getFinishes()) {
                if (x == finish[0] && y == finish[1]) {
                    ai.finishFound();
                    return new Node(x, y, 0, 0);
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

}