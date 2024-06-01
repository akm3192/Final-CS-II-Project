package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.solver.Configuration;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

/**
 * This file serves as a Configuration for Hopper, it hold information about the board.
 * It holds the current board status, the current frogs on the board, it can also check if a cell is currently considered
 * valid or not.
 *
 * The model uses it to represent the current state of the game and solver may use it to find the best path to a solution
 * @author Ashley Miller
 */

public class HoppersConfig implements Configuration{
    /**
     * This character represents a character meant to represent an empty spot on the board that a frog can jump to
     */
    public final Character EMPTY = '.';
    /**
     * This character represents a character meant to represent a red frog on the board that jumps over other frogs
     */
    public final Character RED = 'R';
    /**
     * This character represents a character meant to represent a green frog on the board that a frog can jump over or
     * that can jump over other frogs
     */
    public final Character GREEN = 'G';
    /**
     * This character represents an invalid spot on the board that no frog can jump to
     */
    public final Character INVALID ='*';
    /**
     * neighbors represents a LinkedList of the related neighbors to the current configuration (configurations with one frog
     * jumping )
     */
    private HashMap<Configuration, LinkedList<Configuration>> neighbors;
    /**
     * This Map keeps track of the spots on the board that have a frog, and the frog that is there
     */
    private Map<Coordinates,Frog> curFrogs;

    /**
     * These enums represent the directions that a frog can jump depending on where they are
     */
    public static enum HOPPER_DIRECTIONS{
        RIGHT, LEFT, TOP, BOTTOM, BOTTOM_RIGHT, BOTTOM_LEFT, TOP_LEFT, TOP_RIGHT
    }

    /**
     * This integer variable represents the number of rows on the board, and is the same across all related configs
     */
    private static int rows;
    /**
     * This integer variable represents the number of columns on the board, and is the same across all related configs
     */
    private static int col;
    /**
     * This native array of characters represents the current board that is occuring
     */
    private char[][] board;

    /**
     * This constructor creates a Hopper config from a given file
     * It reads in the file, creates the appropriate board and sets the row and column to a specified number
     * If the file is not found, report the error
     * @param filename the name of the file in which the data for the board will be extracted from
     * @throws IOException if the specified file does not exist, should throw an error
     */
    public HoppersConfig(String filename) throws IOException {
        try(BufferedReader in = new BufferedReader(new FileReader(filename));)
        {
            curFrogs= new HashMap<Coordinates, Frog>();
            String[] fields = in.readLine().strip().split(" ");
            rows = Integer.valueOf(fields[0]);
            col = Integer.valueOf(fields[1]);
            this.board = new char[this.rows][this.col];
            for (int i = 0; i < this.rows; i++) {
                fields = in.readLine().strip().split(" ");
                for(int j = 0; j<this.col;j++){
                    board[i][j] = fields[j].charAt(0);
                    if((fields[j].charAt(0)==GREEN || fields[j].charAt(0)==RED)){
                        Coordinates coord = new Coordinates(i,j);
                        Frog newFroggie = new Frog(coord,fields[j].charAt(0));
                        curFrogs.put(coord,newFroggie);
                    }
                }
            }
        }
        catch(FileNotFoundException e){
            throw new IOException("File was not found!");
        }
    }

    /**
     * This represents the copy configuration, one with a next step/ the next move is ocnsidered valid.
     * @param makeCopy represents the previous config that we will be making a copy of
     * @param start represents the starting coordinate that the frog is jumping from/the frog that will jump
     *              over another frog
     * @param end coordinate that represents where the frog will land, should be an empty cell on the board
     * @param jumpIt coordinate that represents the spot on the board that the starting frog will jump over
     *               it should be another frog.
     */

    public HoppersConfig(HoppersConfig makeCopy,Coordinates start, Coordinates end, Coordinates jumpIt){
        this.curFrogs = new HashMap<Coordinates, Frog>();
        this.board = new char[rows][col];
        for (int i = 0; i < this.rows; i++) {
            this.board[i]=Arrays.copyOf(makeCopy.getBoard()[i],makeCopy.getBoard()[i].length);
        }
        for(Coordinates coord: makeCopy.curFrogs.keySet()){
            this.curFrogs.put(coord, new Frog(coord,makeCopy.curFrogs.get(coord).getTypeFrog()));
        }
        Frog jumper = curFrogs.get(start);
        jumper.updateLocation(end);
        this.board[jumpIt.row()][jumpIt.col()]=EMPTY;
        this.board[end.row()][end.col()]=jumper.getTypeFrog();
        this.board[start.row()][start.col()]=EMPTY;
        curFrogs.get(jumpIt).outOfGame();
        curFrogs.remove(jumpIt);
        curFrogs.remove(start);
        curFrogs.put(end, jumper);
        curFrogs.get(end).updateNeighbors();



    }

    /**
     * This function checks to see if the specified coordinate is within the grid and is not an invalid spot
     * @param coordinate the coordinate that will be checked if it is valid
     * @return if the coordinate is within the grid
     */
    public boolean isCellValid(Coordinates coordinate){
        int curCol = coordinate.col();
        int curRow =coordinate.row();
        if(curRow<0 || curCol <0){return false;}
        if(this.rows<=curRow||this.col<=curCol){return false;}
        if(this.board[coordinate.row()][coordinate.col()]==INVALID){return false;}
        return true;
    }

    @Override
    public boolean isSolution() {

    if (curFrogs.size()==1) {
        return true;
    }
        return false;
    }


    @Override
    public Collection<Configuration> getNeighbors() {
        neighbors = new HashMap<Configuration, LinkedList<Configuration>>();
        LinkedList<Configuration> curneighbors = new LinkedList<Configuration>();
        for(Frog froggie: curFrogs.values()){
            HashMap<Coordinates, HoppersConfig.HOPPER_DIRECTIONS> frogNeighbors=froggie.getNeighbors();
            for(Coordinates nbr: frogNeighbors.keySet()){
                if(isCellValid(nbr)){
                    if(board[nbr.row()][nbr.col()]==GREEN){
                        Coordinates landing =froggie.getNeighbor(nbr,frogNeighbors.get(nbr));
                        if(isCellValid(landing)&&board[landing.row()][landing.col()]==EMPTY){
                            //if the landing spot is empty
                            //should jump the spot
                            //make a new config reflecting this jump
                            HoppersConfig configNeighbor = new HoppersConfig(this, froggie.getCurLocation(),landing, nbr);
                            curneighbors.add(configNeighbor);
                        }

                    }
                }

            }
        }
        neighbors.put(this,curneighbors);
        return curneighbors;
    }
    
    @Override
    public boolean equals(Object other) {
        if(other instanceof HoppersConfig){
            HoppersConfig otro =(HoppersConfig) other;
            return Arrays.deepEquals(this.getBoard(),otro.getBoard());
        }
        return false;
    }
    //arrays.deepEquals();
    @Override
    public int hashCode() { return Arrays.deepHashCode(this.board); }

    @Override
    public String toString() {
        String boardString = "";
        for(int i =0;i<rows;i++){
            char[] row = board[i];
            for (int j = 0;j<col;j++){
                char col = board[i][j];
                if(i==1){

                }
                boardString += col+" ";
            }
            boardString += "\n";
        }
        return boardString;

    }

    /**
     * This function serves as an accessor to the current board in the configuration
     * @return native 2-d array of characters
     */

    public char[][] getBoard() {
        return board;
    }

    /**
     * This function serves as an accessor to the active frogs on the board
     * @return a Map with coordinate as keys (where the frog currently is. The values represent the )
     */
    public Map<Coordinates,Frog> getCurFrogs(){
        return this.curFrogs;
    }
}

