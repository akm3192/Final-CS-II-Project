package puzzles.hoppers.model;

import puzzles.common.Coordinates;

import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashSet;

/**
 * This class represents a Frog Object, the boards in the current configs can have many of these.
 * They are essential to finding if a frog can jump or not/finding solutions to a configuration
 *
 * @author Ashley Miller
 */
public class Frog {
    /**
     * This variable represents an integer that holds the id for the frog created
     */
    private int thisId;
    /**
     * This represents the coordinates of the current frog
     */
    private Coordinates curLocation;
    /**
     * This variable represents what type of frog the frog is, can either be Green or red in most cases
     */
    private Character typeFrog;
    /**
     * This variable represents if the frog is active or if it has been kicked out of the board/jumped over
     */
    private boolean active;
    /**
     * This static integer variable represents the id for the next frog created
     */
    private static int nextId = 0;
    /**
     * This HashMap holds the current neighbors of the frog, valid and invalid coordinates, where the keys are neighbor coordinates
     * and the value is the direction the coordinate is in relative to the frog's current location
     */
    private HashMap<Coordinates, HoppersConfig.HOPPER_DIRECTIONS> neighbors;

    /**
     * This is the constructor of Frog, it assigns the curlocation to the location entered, and assigns the type frog
     * variable to the character also inputted
     *
     *      it sets teh frog to active and assigns it an id while updating the id for the next frog
     *      then it calls the update neighbors function so the neighbors that this frog has are updated and accurate
     *
     * @param location the current location teh frog is at represented as a coordinates object
     * @param typeFrog the type of frog this frog will be
     */
    public Frog(Coordinates location, Character typeFrog){
        this.curLocation = location;
        this.typeFrog=typeFrog;
        active = true;
        this.thisId = this.nextId;
        this.nextId++;
        this.updateNeighbors();
    }

    /**
     * This function updates the location of a frog, by setting theglobal variable curlocation to the specified coordinates
     * in the parameter
     * @param newLoc
     */
    public void updateLocation(Coordinates newLoc){
        this.curLocation=newLoc;
    }

    /**
     * This function sets the activity sttaus to false, when the frog has been jumper :(
     */
    public void outOfGame(){active = false;}

    /**
     * This is a getter function, it gets the frogs current location
     * @return the curlocation coordinates
     */
    public Coordinates getCurLocation(){return curLocation;}

    /**
     * This function is a getter function, it returns what kind of frog this frog is
     * @return character representing what frog this frog is.
     */
    public Character getTypeFrog(){return typeFrog;}

    /**
     * This function serves as a getter function, it returns a hash map that has the keys as coordinates and the values
     * as direction the coordinates are relative to the frog.
     * @return
     */
    public HashMap<Coordinates, HoppersConfig.HOPPER_DIRECTIONS> getNeighbors(){

        return this.neighbors;
    }

    /**
     * This function updates the neighbors of the frogs current location, by cretaing a new hash map with coordinates as the keys
     * and the directions relative to the frog those coordinates are
     *
     * if the frog's location is an even row, add 4 extra coordinates(top, bottom, left and right) by using their respective
     * functions
     *
     * if it is in an odd or even row, add these 4 coordinates to the map and their respective directions (top left,
     * bottom left, top right and bottom right)
     *
     * then set the nighbors map variable to this new map
     */
    public void updateNeighbors(){
        HashMap<Coordinates, HoppersConfig.HOPPER_DIRECTIONS> coordDirection = new HashMap<Coordinates, HoppersConfig.HOPPER_DIRECTIONS>();
        if(this.curLocation.row()%2==0){
            coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.LEFT), HoppersConfig.HOPPER_DIRECTIONS.LEFT);
            coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.RIGHT), HoppersConfig.HOPPER_DIRECTIONS.RIGHT);
            coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.TOP), HoppersConfig.HOPPER_DIRECTIONS.TOP);
            coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.BOTTOM), HoppersConfig.HOPPER_DIRECTIONS.BOTTOM);
        }
        coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.BOTTOM_LEFT), HoppersConfig.HOPPER_DIRECTIONS.BOTTOM_LEFT);
        coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.BOTTOM_RIGHT), HoppersConfig.HOPPER_DIRECTIONS.BOTTOM_RIGHT);
        coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.TOP_LEFT), HoppersConfig.HOPPER_DIRECTIONS.TOP_LEFT);
        coordDirection.put(getNeighbor(curLocation,HoppersConfig.HOPPER_DIRECTIONS.TOP_RIGHT), HoppersConfig.HOPPER_DIRECTIONS.TOP_RIGHT);
        neighbors=coordDirection;
    }

    /**
     * This function returns the neighbor in the specified direction, through calling upon various helper functions,
     * depending on what the direction is. If the direction is not valid, return null, as the neighbor coordinate will not
     * exist
     * @param coordinate -  the coordinate you want to find the neighbor of
     * @param direc - the direction the requested neighbor will be in
     * @return the coordinates of the specified coordinate in the direction specified
     */
    public Coordinates getNeighbor(Coordinates coordinate, HoppersConfig.HOPPER_DIRECTIONS direc){
        int row = coordinate.row();
        int col = coordinate.col();
        switch(direc){
            case HoppersConfig.HOPPER_DIRECTIONS.LEFT:
                return getLeft(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.RIGHT:
                return getRight(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.BOTTOM:
                return getBottom(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.TOP:
                return getTop(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.TOP_LEFT:
                return getTop_left(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.TOP_RIGHT:
                return getTop_right(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.BOTTOM_LEFT:
                return getBottom_left(coordinate);
            case HoppersConfig.HOPPER_DIRECTIONS.BOTTOM_RIGHT:
                return getBottom_right(coordinate);
        }
        return null;
    }
    /**
     * This function returns the coordinate to the left of the coordinate. It calculates this by subtracting two to the
     * enetred coordinates column
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the left coordinate relative to the coordinate entered
     */
    public Coordinates getLeft(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row,col-2);
    }
    /**
     * This function returns the coordinate to the right of the coordinate. It calculates this by adding two to the
     * enetred coordinates column
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the right coordinate relative to the coordinate entered
     */
    public Coordinates getRight(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row,col+2);
    }
    /**
     * This function returns the coordinate to the bottom of the coordinate. It calculates this by adding two to the
     * enetred coordinates row
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the bottom coordinate relative to the coordinate entered
     */
    public Coordinates getBottom(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row+2,col);
    }
    /**
     * This function returns the coordinate to the bottom left of the coordinate. It calculates this by adding one to the
     * enetred coordinates row and subtracting one from its row
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the bottom left coordinate relative to the coordinate entered
     */
    public Coordinates getBottom_left(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row+1,col-1);
    }
    /**
     * This function returns the coordinate to the bottom right of the coordinate. It calculates this by adding one to the
     * enetred coordinates row and adding one to its row
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the bottom right coordinate relative to the coordinate entered
     */
    public Coordinates getBottom_right(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row+1,col+1);
    }
    /**
     * This function returns the coordinate to the top of the coordinate. It calculates this by subtracting two from the
     * enetred coordinates row
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the top coordinate relative to the coordinate entered
     */
    public Coordinates getTop(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row-2,col);
    }
    /**
     * This function returns the coordinate to the top left of the coordinate. It calculates this by subtracting one from the
     * enetred coordinates row and subtracting one from its column
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the top left coordinate relative to the coordinate entered
     */
    public Coordinates getTop_left(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row-1,col-1);
    }

    /**
     * This function returns the coordinate to the top right of the coordinate entered. It calculates this by subtracting one from the
     * enetred coordinates row and adding one to its column
     *
     * @param coordinate - the coordinate you want to find the neighbor of
     * @return the top right coordinate relative to the coordinate entered
     */
    public Coordinates getTop_right(Coordinates coordinate){
        int row = coordinate.row();
        int col = coordinate.col();
        return new Coordinates(row-1,col+1);
    }
}
