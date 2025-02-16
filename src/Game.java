// İlknur Nazlı Koşar - 21050111018
// Hatice Çam - 21050111042

import java.util.List;

public class Game {
    public static void main(String[] args) {
        for (int i = 1; i <= 10; i++) {
            String fileName = String.format("level%02d.txt", i);
            Board board = new Board(fileName);
            Scene scene = new Scene(board.getBoardSize(), 3);
            PathFinder pathFinder = new PathFinder(board, scene);

            System.out.println("Scene for " + fileName + ":\n" + scene);

            List<Step> path = pathFinder.findPath();
            if (path != null) {
                ImageSaver imageSaver = new ImageSaver(board, scene);
                imageSaver.savePathImages(path);
            }
            System.out.println();
        }
    }
}