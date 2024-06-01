package puzzles.astro.model;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

/**
 * Configuration representation for Astro, where the board information is held and initialized.
 *
 * @author Laura Rodriguez
 */

public class AstroConfig implements Configuration{
    /** the number of rows on the board **/
    private int row;

    /** the number of columns on the board **/
    private int col;

    /** the goal row number **/
    private int goalRow;

    /** the goal column number **/
    private int goalCol;

    /** row number of the astronaut **/
    private int astroRow;

    /** column number of the astronaut **/
    private int astroCol;

    /** native array representing the current board **/
    private char [][] board;

    /** list containing the coordinates of all robots on the board **/
    private List<int []> bots;

    /** list of all the neighbor configurations **/
    private List<Configuration> neighbors;

    /** list containing all valid directions both robots and astronaut can move in **/
    private int [][] direction = {{-1, 0}, {1, 0}, {0, -1}, {0, 1}};

    /**
     * Astro constructor where the game board is initialized according to a file.
     * The file gets read and creates the game board.
     * If the given file if not found, an error is thrown.
     *
     * @param filename name of the file that holds board data
     * @throws IOException if no file is found, error gets
     */
        public AstroConfig(String filename) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            String[] fields = reader.readLine().strip().split(" ");
            row = Integer.parseInt(fields[0]);
            col = Integer.parseInt(fields[1]);
            board = new char[row][col];

            String[] goal = reader.readLine().strip().split(" ");
            goalRow = Integer.parseInt(goal[1].split(",")[0]);
            goalCol = Integer.parseInt(goal[1].split(",")[1]);

            String[] astronaut = reader.readLine().strip().split(" ");
            astroRow = Integer.parseInt(astronaut[1].split(",")[0]);
            astroCol = Integer.parseInt(astronaut[1].split(",")[1]);

            int robots = Integer.parseInt(reader.readLine());
            bots = new ArrayList<>();
            for (int i = 0; i < robots; i++) {
                String[] robot = reader.readLine().strip().split(" ");
                int roboRow = Integer.parseInt(robot[1].split(",")[0]);
                int roboCol = Integer.parseInt(robot[1].split(",")[1]);
                bots.add(new int[]{roboRow, roboCol});
            }
        }
        catch (IOException e){
            throw new IOException("File was not found!");
        }
    }

    @Override
    public boolean isSolution() {
        return astroRow == goalRow && astroCol == goalCol;
    }

    @Override
    public Collection<Configuration> getNeighbors(){
            neighbors = new ArrayList<>();

            for (int[] dir : direction){
                int newRow = astroRow + dir[0];
                int newCol = astroCol + dir[1];

                if (isValid(newRow, newCol)){
                    if ((newRow != astroRow || newCol != astroCol) && !(newRow == goalRow && newCol == goalCol)){
                        try{AstroConfig neigh = new AstroConfig(this.toString());
                            neigh.board[astroRow][astroCol] = '.';
                            neigh.board[newRow][newCol] = 'A';
                            neigh.astroRow = newRow;
                            neigh.astroCol = newCol;
                            neighbors.add(neigh);
                        }
                        catch (IOException e){}
                    }
                }
                newRow += dir[0];
                newCol += dir[1];
            }

        return neighbors;
    }

    /**
     * This is a helper function that checks if a specified board space is a valid
     * space or an invalid space
     *
     * @param r row number of board  space
     * @param c column number of board space
     * @return whether that board space is valid
     */
    private boolean isValid(int r, int c) {
        if (r < 0 || r >= row || c < 0 || c >= col || board[r][c] == 'A') {
            return false;
            }

        for (int[] robot : bots) {
            if (robot[0] == r && robot[1] == c) {
                return false;
            }
        }

        for (int[] dir : direction) {
            int newRow = r + dir[0];
            int newCol = c + dir[1];
            while (newRow >= 0 && newRow < row && newCol >= 0 && newCol < col && board[newRow][newCol] != 'A') {
                for (int[] robot : bots) {
                    if (robot[0] == newRow && robot[1] == newCol) {
                        return false;
                    }
                }
                newRow += dir[0];
                newCol += dir[1];
            }
        }
        return true;
    }

    @Override
    public boolean equals(Object other) {
            if (other instanceof AstroConfig){
                AstroConfig oth = (AstroConfig) other;
                return Arrays.deepEquals(this.getBoard(), oth.getBoard());
            }
        return false;
    }

    /**
     * An accessor for the current board
     *
     * @return native 2D array
     */
    public char[][] getBoard(){
            return board;
    }

    @Override
    public int hashCode() {
        return Arrays.deepHashCode(board);
    }

    @Override
    public String toString() {
        StringBuilder string = new StringBuilder();
        string.append("\n");
        for (int i = 0; i < row; i++){
            for (int j = 0; j < col; j++){
                if (i == goalRow && j == goalCol){
                    string.append("* ");
                }
                else if (i == astroRow && j == astroCol){
                    string.append("A ");
                }
                else{
                    boolean present = false;
                    for (int k = 0; k < bots.size(); k++) {
                        int[] robo = bots.get(k);
                        if (i == robo[0] && j == robo[1]) {
                            string.append((char)('B' + k)).append(" ");
                            present = true;
                            break;
                        }
                    }
                    if (!present){
                        string.append(". ");
                    }
                }
            }
            string.append("\n");
        }
        return string.toString();
    }
}
