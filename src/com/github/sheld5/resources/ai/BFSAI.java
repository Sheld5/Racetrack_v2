import java.util.ArrayList;

public class BFSAI implements DriverAI {

    // Goes through the moves to get to the Finish.
    public int[] drive(int[] carCoordinates, int[] carVelocity, Tile[][] map) {
        step++;
        return movesToFinish.get(step);
    }

    private int[] start;
    private ArrayList<int[]> finishes;
    private ArrayList<Path> paths;
    private ArrayList<Path> tempPaths;
    private ArrayList<Node> visitedNodes;
    private boolean finishFound, visited;
    private ArrayList<int[]> movesToFinish;
    private int step;

    // Initializes fields. Handles the loop to find the shortest path to finish.
    public void init(Tile[][] map) {
        findStartAndFinish(map);
        paths = new ArrayList<>();
        paths.add(new Path(null, start));
        visitedNodes = new ArrayList<>();
        finishFound = false;
        visited = false;

        Path tryPath;
        while(!finishFound) {
            tempPaths = deepCopy(paths);
            paths = new ArrayList<>();
            for (Path path : tempPaths) {
                for (int dx = -1; dx <= 1; dx++) {
                    for (int dy = -1; dy <= 1; dy++) {
                        tryPath = new Path(path, new int[]{dx,dy});
                        checkForFinish(tryPath);
                        if (finishFound) {
                            break;
                        }
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
                    if (finishFound) {
                        break;
                    }
                }
                if (finishFound) {
                    break;
                }
            }
        }

        step = -1;
    }

    // Checks whether the last Node of the Path is finish. Saves moves to get to the finish if so.
    private void checkForFinish(Path path) {
        for (int[] finish : finishes) {
            if (path.getLastNode().get(0) == finish[0] && path.getLastNode().get(1) == finish[1]) {
                finishFound = true;
                movesToFinish = path.getMoves();
                break;
            }
        }
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

}




// Contains X and Y coordinates of a tile and VX and VY coordinates of current velocity vector.
class Node {

    private int[] node;

    public Node(int x, int y, int vx, int vy) {
        node = new int[]{x,y,vx,vy};
    }

    public int get(int i) {
        return node[i];
    }

}




// Contains list of Nodes (the path) and list of moves to go through the path.
class Path {

    // List of Nodes -> the path.
    private ArrayList<Node> path;
    // List of moves to get to the last Node of this path.
    private ArrayList<int[]> moves;

    // Constructs new Path with one more Node from the previous Path and int[] nextMove.
    public Path(Path parentPath, int[] nextMove) {
        path = new ArrayList<>();
        moves = new ArrayList<>();
        // if parentPath == null, then nextMove contains Start coordinates, make Start Node
        if (parentPath == null) {
            path.add(new Node(nextMove[0], nextMove[1], 0, 0));
        } else {
            for (Node node : parentPath.get()) {
                path.add(node);
            }
            for (int[] move : parentPath.getMoves()) {
                moves.add(move);
            }
            path.add(createNewNode(nextMove));
            moves.add(nextMove);
        }
    }

    // Creates new Node by using X and Y coordinates from the last Node in path
    // and VX and VY coordinates from the argument int[] nextMove.
    private Node createNewNode(int[] nextMove) {
        Node last = path.get(path.size() - 1);
        int vx = last.get(2) + nextMove[0];
        int vy = last.get(3) + nextMove[1];
        int x = last.get(0) + vx;
        int y = last.get(1) + vy;
        return new Node(x,y,vx,vy);
    }

    public ArrayList<Node> get() {
        return path;
    }

    public ArrayList<int[]> getMoves() {
        return moves;
    }

    public Node getLastNode() {
        return path.get(path.size() - 1);
    }

}