import java.util.Random;

public class Scene {
    public final int size;
    private final int[][] elevations;
    private static Random rand = new Random();

    public Scene(int size) {
        this.size = validSize(size);
        this.elevations = createElevationGrid(this.size);
    }

    public Scene(int size, int pyramidCount) {
        this.size = validSize(size);
        this.elevations = createElevationGrid(this.size);
        generatePyramids(pyramidCount);
    }

    public int getElevation(int x, int y) {
        if (x >= 0 && x < size && y >= 0 && y < size) {
            return elevations[y][x];
        }
        return -1;
    }

    public static int[][] createElevationGrid(int size) {
        int[][] arr = new int[size][];
        for (int i=0; i<size; i++) {
            arr[i] = new int[size];
        }
        return arr;
    }

    public static int validSize(int size) {
        return Math.min(Math.max(size, 8), 60);
    }

    public void generatePyramids(int pyramidCount) {
        pyramidCount = Math.min(Math.max(pyramidCount, 1), 8);

        final int band = 4;
        final int r1 = -band;
        final int r2 = size + band;
        int topLevel = rand.nextInt(Math.max(1, size/4)) + 4;

        for (int i = 0; i < pyramidCount; i++) {
            int x_center = rand.nextInt(r2 - r1) + r1;
            int y_center = rand.nextInt(r2 - r1) + r1;

            for (int y = 0; y < elevations.length; y++) {
                int[] row = elevations[y];
                int dy = Math.abs(y_center - y);

                for (int x = 0; x < row.length; x++) {
                    int dx = Math.abs(x_center - x);
                    int height = Math.max(topLevel - (dx+dy), 0);
                    row[x] = Math.min(row[x] + height, 9);
                }
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for (int y=0; y<elevations.length; y++) {
            for (int x=0; x<elevations[y].length; x++) {
                int val = elevations[y][x];
                if (val==0) sb.append(".");
                else sb.append((char)('0'+val));
            }
            sb.append("\n");
        }
        return sb.toString();
    }
}