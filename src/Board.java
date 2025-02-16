import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class Board {
    private final int BOARD_SIZE = 15;
    private final int CELL_SIZE = 20;

    private char[][] board;
    private String levelName;

    public Board(String file) {
        this.board = readFromFile(file);
        this.levelName = file.substring(0, file.indexOf('.'));
    }

    public int getBoardSize() {
        return BOARD_SIZE;
    }

    public int getCellSize() {
        return CELL_SIZE;
    }

    public char[][] getBoardState() {
        return copyBoard(board);
    }

    public String getLevelName() {
        return levelName;
    }

    public int[] findChar(char ch) {
        for (int i = 0; i < BOARD_SIZE; i++) {
            for (int j = 0; j < BOARD_SIZE; j++) {
                if (board[i][j] == ch) {
                    return new int[]{i, j};
                }
            }
        }
        return null;
    }

    private char[][] readFromFile(String fileName) {
        char[][] arr = new char[BOARD_SIZE][BOARD_SIZE];
        try (BufferedReader br = new BufferedReader(new FileReader(fileName))) {
            String line;
            int row = 0;
            while (row < BOARD_SIZE && (line = br.readLine()) != null) {
                for (int col = 0; col < Math.min(line.length(), BOARD_SIZE); col++) {
                    arr[row][col] = line.charAt(col);
                }
                row++;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return arr;
    }

    private char[][] copyBoard(char[][] source) {
        char[][] cp = new char[BOARD_SIZE][BOARD_SIZE];
        for (int i = 0; i < BOARD_SIZE; i++) {
            cp[i] = source[i].clone();
        }
        return cp;
    }
}