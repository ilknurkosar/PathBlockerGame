import java.util.*;

public class PathFinder {
    private Board board;
    private Scene scene;
    private List<Step> path;

    public PathFinder(Board board, Scene scene) {
        this.board = board;
        this.scene = scene;
        this.path = new ArrayList<>();
    }

    public List<Step> findPath() {
        // Checking for START and GOAL position in the map.
        int[] sPos = board.findChar('S');
        int[] gPos = board.findChar('G');
        if (sPos == null || gPos == null) {
            System.out.println("No 'S' or 'G' found in " + board.getLevelName());
            return null;
        }

        // Initial step
        char[][] initBoard = board.getBoardState();
        Step initStep = new Step(sPos[0], sPos[1], sPos[0], sPos[1], initBoard);
        path.add(initStep);

        // A* algorithm is used.
        boolean found = aStarSliding(sPos[0], sPos[1]);
        if (found) {
            return path;
        } else {
            System.out.println("Path not found for " + board.getLevelName());
            return null;
        }
    }

    private boolean aStarSliding(int startRow, int startCol) {
        // In A* search, priority queue is used.

        int[] goal = board.findChar('G'); // Searching for 'G' char which is GOAL in map.
        if (goal == null) return false;
        int goalRow = goal[0];
        int goalCol = goal[1];

        // openSet + costSoFar
        PriorityQueue<AStarNode> openSet = new PriorityQueue<>();
        Map<String, Double> costSoFar = new HashMap<>();

        List<Step> initPath = new ArrayList<>();
        initPath.add(path.get(0));
        AStarNode startNode = new AStarNode(
                startRow, startCol,
                0.0,
                heuristic(startRow, startCol, goalRow, goalCol),
                board.getBoardState(),
                initPath
        );
        openSet.add(startNode);
        costSoFar.put(makeKey(startNode.boardState, startRow, startCol), 0.0);

        while (!openSet.isEmpty()) {
            AStarNode current = openSet.poll();

            if (current.r == goalRow && current.c == goalCol) {
                path.clear();
                path.addAll(current.path);
                return true;
            }

            double currentCost = current.gCost;

            // Checking 4 directions for movements
            for (int[] dir : new int[][]{{-1, 0}, {1, 0}, {0, -1}, {0, 1}}) {
                char[][] newBoard = copyBoard(current.boardState);
                int nr = current.r;
                int nc = current.c;
                double slideCost = 0.0;
                int prevElev = scene.getElevation(nc, nr);

                // Sliding movement
                while (canMove(nr + dir[0], nc + dir[1], newBoard)) {
                    nr += dir[0];
                    nc += dir[1];
                    int currElev = scene.getElevation(nc, nr);
                    slideCost += (1.0 + Math.abs(currElev - prevElev));
                    prevElev = currElev;

                    // If GOAL is reached, then done
                    if (newBoard[nr][nc] == 'G') {
                        break;
                    }
                    newBoard[nr][nc] = '#';
                }

                if (nr == current.r && nc == current.c) {
                    continue;
                }

                double newG = currentCost + slideCost;
                double newH = heuristic(nr, nc, goalRow, goalCol);

                List<Step> newPath = new ArrayList<>(current.path);
                Step newStep = new Step(current.r, current.c, nr, nc, newBoard);
                newPath.add(newStep);

                AStarNode neighbor = new AStarNode(
                        nr, nc,
                        newG,
                        newH,
                        newBoard,
                        newPath
                );

                String neighborKey = makeKey(newBoard, nr, nc);
                double oldCost = costSoFar.getOrDefault(neighborKey, Double.POSITIVE_INFINITY);

                // Checking if there is more optimal path
                if (newG < oldCost) {
                    costSoFar.put(neighborKey, newG);
                    if (newBoard[nr][nc] == 'G') {
                        path = newPath;
                        return true;
                    }
                    openSet.add(neighbor);
                }
            }
        }

        return false;
    }

    // In heuristic function, the map is considered as a real world. The height of cells did not used as cost directly,
    // instead their differences is calculated and they added up together with Manhattan distance.
    private double heuristic(int r, int c, int gr, int gc) {
        double manhattan = Math.abs(gr - r) + Math.abs(gc - c);
        int e1 = scene.getElevation(c, r);
        int e2 = scene.getElevation(gc, gr);
        if (e1 < 0) e1 = 0;
        if (e2 < 0) e2 = 0;
        double elevDiff = Math.abs(e1 - e2);
        return manhattan + elevDiff;
    }

    private boolean canMove(int rr, int cc, char[][] brd) {
        int boardSize = board.getBoardSize();
        if (rr < 0 || rr >= boardSize || cc < 0 || cc >= boardSize) return false;
        char ch = brd[rr][cc];
        return (ch != '#' && ch != 'S') || ch == 'G';
    }

    private String makeKey(char[][] b, int r, int c) {
        StringBuilder sb = new StringBuilder();
        for (char[] row : b) {
            sb.append(row);
        }
        sb.append("(").append(r).append(",").append(c).append(")");
        return sb.toString();
    }

    private char[][] copyBoard(char[][] source) {
        int boardSize = board.getBoardSize();
        char[][] cp = new char[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            cp[i] = source[i].clone();
        }
        return cp;
    }

    private static class AStarNode implements Comparable<AStarNode> {
        int r, c;
        double gCost;
        double hCost;
        double fCost; // g+h
        char[][] boardState;
        List<Step> path;

        AStarNode(int rr, int cc, double g, double h, char[][] brd, List<Step> p) {
            this.r = rr;
            this.c = cc;
            this.gCost = g;
            this.hCost = h;
            this.fCost = g + h;
            this.boardState = brd;
            this.path = p;
        }

        @Override
        public int compareTo(AStarNode other) {
            return Double.compare(this.fCost, other.fCost);
        }
    }
}