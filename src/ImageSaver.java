import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.List;
import javax.imageio.ImageIO;

public class ImageSaver {
    private Board board;
    private Scene scene;

    public ImageSaver(Board board, Scene scene) {
        this.board = board;
        this.scene = scene;
    }

    public void savePathImages(List<Step> path) {
        createFolder(board.getLevelName());
        for (int i = 0; i < path.size(); i++) {
            saveBoardAsImage(path.get(i).boardState, path.get(i), i);
        }
    }

    private void saveBoardAsImage(char[][] boardState, Step step, int index) {
        int boardSize = board.getBoardSize();
        int cellSize = board.getCellSize();
        BufferedImage image = new BufferedImage(
                boardSize * cellSize,
                boardSize * cellSize,
                BufferedImage.TYPE_INT_RGB
        );
        Graphics g = image.getGraphics();

        for (int r = 0; r < boardSize; r++) {
            for (int c = 0; c < boardSize; c++) {
                int px = c * cellSize;
                int py = r * cellSize;

                // OUTLINES as RED
                if (r == 0 || r == boardSize - 1 || c == 0 || c == boardSize - 1) {
                    g.setColor(Color.RED);
                    g.fillRect(px, py, cellSize, cellSize);
                    continue;
                }

                // Player as YELLOW
                if (r == step.playerX && c == step.playerY) {
                    g.setColor(Color.YELLOW);
                    g.fillRect(px, py, cellSize, cellSize);
                    continue;
                }

                // ELEVATIONS as GREY
                char cell = boardState[r][c];
                int elev = scene.getElevation(c, r);
                if (elev < 0) elev = 0;
                int grey = (int) (255.0 * (elev / 9.0));
                grey = Math.max(0, Math.min(255, grey));

                switch (cell) {
                    case 'S':
                        g.setColor(Color.GREEN);
                        break;
                    case 'G':
                        g.setColor(Color.BLUE);
                        break;
                    case '#':
                        g.setColor(new Color(128, 0, 128));
                        break;
                    default:
                        g.setColor(new Color(grey, grey, grey));
                }
                g.fillRect(px, py, cellSize, cellSize);
            }
        }

        try {
            File outFile = new File(board.getLevelName() + "/level" + String.format("%04d", index) + ".png");
            System.out.println(outFile.getAbsolutePath());
            ImageIO.write(image, "png", outFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void createFolder(String name) {
        File dir = new File(name);
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }
}