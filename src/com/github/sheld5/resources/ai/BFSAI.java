package model;

import java.util.ArrayList;

import static java.lang.Math.abs;
import static java.lang.Math.sqrt;

// Implementation of DriverAI which uses Breadth-First-Search to find the shortest route to finish.
// This AI does take into consideration all special tiles and their functions.
// Does not go through checkpoints for now.
public class BFSAI implements DriverAI {

    // Goes through the moves to get to the Finish.
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        step++;
        return movesToFinish.get(step);
    }

    private Tile[][] map;
    // Start coordinates.
    private int[] start;
    // Coordinates of all finishes.
    private ArrayList<int[]> finishes;
    // List of all currently saved paths.
    private ArrayList<Path> paths;
    private ArrayList<Path> tempPaths;
    // List of all visited nodes.
    private ArrayList<Node> visitedNodes;
    private boolean finishFound, visited;
    // Array of moves to which the result is saved.
    private ArrayList<int[]> movesToFinish;
    private int step;

    // Initializes fields. Handles the loop to find the shortest path to finish.
    public void init(Tile[][] map) {
        findStartAndFinish(map);
        this.map = map;
        paths = new ArrayList<>();
        paths.add(new Path(start));
        visitedNodes = new ArrayList<>();
        finishFound = false;
        visited = false;

        Path tryPath;
        while(!finishFound) {
            tempPaths = deepCopy(paths);
            paths = new ArrayList<>();
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

    // Checks if the last node of the new path was visited and adds the new path to paths if not.
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

    // Finds and saves the coordinates of the Start and Finishes.
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

    // Compares two nodes. Returns true if all attributes have the same value.
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




// Contains list of Nodes (the path) and list of moves to go through the path.
class Path {

    // List of Nodes -> the path.
    private ArrayList<Node> path;
    // List of moves to get to the last Node of this path.
    private ArrayList<int[]> moves;

    // Constructs new Path with one more Node from the previous Path and int[] nextMove.
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

    // Constructor for the first Path instance with the only node being Start.
    Path(int[] start) {
        path = new ArrayList<>();
        moves = new ArrayList<>();
        path.add(new Node(start[0], start[1], 0, 0));
    }

    // Creates new Node by using X and Y coordinates from the last Node in path
    // and VX and VY coordinates from the argument int[] nextMove.
    // Checks the whole path of the car for special tiles
    // and creates nodes with special attributes to correspond with these attributes.
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

        if (abs(initX - targetX) > abs(initY - targetY)) {
            for (int x = initX; x - dirX != targetX; x += dirX) {
                for (int y = initY; y - dirY != targetY; y += dirY) {
                    if (firstTile) {
                        firstTile = false;
                    } else if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
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
                    }
                }
            }
        } else {
            for (int y = initY; y - dirY != targetY; y += dirY) {
                for (int x = initX; x - dirX != targetX; x += dirX) {
                    if (firstTile) {
                        firstTile = false;
                    } else if (abs(a * x + b * y + c) / (sqrt(a * a + b * b)) <= 0.5) {
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
                    }
                }
            }
        }
        if (map[targetX][targetY] == null || map[targetX][targetY] == Tile.WALL) {
            Node node = new Node(targetX - dirX, targetY - dirY, 0, 0);
            node.setWall(3);
            return node;
        } else if (map[targetX][targetY] == Tile.WATER) {
            Node node = new Node(targetX, targetY, 0, 0);
            node.setWaterTrue();
            return node;
        } else if (map[targetX][targetY] == Tile.SAND) {
            return new Node(targetX, targetY, 0, 0);
        } else if (map[targetX][targetY] == Tile.ICE) {
            Node node = new Node(targetX, targetY, last.get(2) + nextMove[0], last.get(3) + nextMove[1]);
            node.setIceTrue();
            return node;
        } else {
            for (int[] finish : ai.getFinishes()) {
                if (targetX == finish[0] && targetY == finish[1]) {
                    ai.finishFound();
                    return new Node(targetX, targetY, 0, 0);
                }
            }
        }
        return new Node(targetX, targetY, last.get(2) + nextMove[0], last.get(3) + nextMove[1]);
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