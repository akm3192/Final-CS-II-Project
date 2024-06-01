package puzzles.hoppers.model;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.common.solver.Solver;

import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

/**
 *This file contains the model for the Hopper game. It is the brains of the operation, containing the load, loaded, hint,
 * new game etc. It also holds the current configuration for the views to pull from.
 * @author Ashley Miller
 */
public class HoppersModel {
    /** the collection of observers of this model */
    private final List<Observer<HoppersModel, String>> observers = new LinkedList<>();
    /**
     * This variable holds a Coordinate, that represents the first selected coordinate when a user wants a frog to jump
     */
    private Coordinates selectedStart;
    /**
     * This variable holds a Coordinate, that represents the coordinate of a landing spot for a frog when a user wants a frog to jump
     */
    private Coordinates selectedLand;


    /** the current configuration */
    private HoppersConfig currentConfig;
    /**
     * The starting configuration (a ocnfiguration loaded from a file most likely)
     */
    private HoppersConfig start;
    /**
     * This string variable represents the filename in which the configuration pulls data from ie. the frogs and the game board
     */
    private String fileName;



    /**
     * The view calls this to add itself as an observer.
     *
     * @param observer the view
     */
    public void addObserver(Observer<HoppersModel, String> observer) {
        this.observers.add(observer);
    }

    /**
     * The model's state has changed (the counter), so inform the view via
     * the update method
     */
    private void notifyObservers(String msg) {
        for (var observer : observers) {
            observer.update(this, msg);
        }
    }

    /**
     * This is the constructor of the Hopper Model, it uses the filename to load the game. Load is another function in the
     * file that calls Hopper Config
     * @param filename string that represents the name of the file in which the gameboard is based off of
     * @throws IOException when the filename does not exist
     */
    public HoppersModel(String filename) throws IOException {
            try {
                this.fileName = filename;
                this.load(filename);

            }
            catch(IOException e){
                System.out.println(e.getMessage());
                System.exit(0);
            }

    }
    private void init(){selectedLand=null;
        selectedStart=null;}

    /**
     * This function notified the observers that the file has been loaded and calls init to reset the selected landing
     * and starting coordinates
     */
    public void loaded(){notifyObservers("Loaded: "+ this.fileName);
    init();}

    /**
     * This function creates a new game based on the starting configuration. It sets the current configuration to the
     * starting one, and notifies the obserevers
     */
    public void newGame(){
        try{load(this.fileName);}
        catch (IOException e){}
        notifyObservers("Puzzle reset!");
    }

    /**
     * This function serves to load the gameboard/game based on the file it is told to read, if it cannot read the file,
     * it throws an error. It loads the game by creating a hopperconfig, and storing it as the current and start configuration
     * It then calls loaded, which tells the view to update
     * @param filename a string containing the filename of the file from which data will be pulled from to create a config and board
     * @throws IOException throws an exception when teh file is not found
     */
    public void load(String filename) throws IOException{
        try{this.fileName = filename;
        this.currentConfig = new HoppersConfig(filename);
        start = currentConfig;
        loaded();
        //need to make init and below called in ptui?

        }

    catch(IOException e){
            throw new IOException(e.getMessage());
    }

    }

    /**
     * This function is serves as the brains behind the select button/command.
     * If the model's selected start is null, then the coordinate inputted becomes the new selected start
     *      then it checks to see if it is valid on 2 basis:
     *          1)It is valid if the coordinate is on the board
     *          2)the coordinate a key in the current config's map of current frogs .
     *
     *      if the starting selected cell is valid, the model notifies the observers, if it is not, it also notifies the
     *      observers and resets the selected start and landing variables to null, so the coordinates are not saved for future
     *      moves.
     *if the selectedstart is not null, it means that the selected cell is the landing cell
     *      so the landing cell is set to the inputted coordinate
     *      then it sets a Frog variable, start frog to the frog of currentconfig at the strating coordinate
     *
     *      then the function calls Frogs, getNeighbors function and parses through each neighbor:
     *          if the neighbor is valid and is a frog, active in the current congiuration, set it to another Frog variable
     *          called jumped.
     *
     *          Then get the direction that the jumped frog is from the start
     *          call the startfrog's get neighbor, passing that direction into it. If the resulting coordinate is the
     *          same as the selected landing coordinate and is a valid cell, and the jumped frog is not red, then
     *          set the current configuration to a new Hopper configuration as specified
     *          Then notify the observers and reset the selected start and landing values
     *
     *  else, tell the observers that the frog can't jump from the specified landing and starting coordinates
     *  then reset the selected and landing coordinates by calling init()
     *
     *
     * @param coordinate coordinate representing the cell/spot that the user selected
     */
    public void select(Coordinates coordinate) {
        //if the first select is empty, make it full, by seeing if the cell is valid
        if(selectedStart==null){
            selectedStart = coordinate;
            //if the cell selected is valid and a frog
            if(currentConfig.isCellValid(selectedStart)&&currentConfig.getCurFrogs().containsKey(selectedStart)){
                notifyObservers("Selected "+selectedStart);
            }
            else{
                notifyObservers("No frog at "+selectedStart);
                init();
            }
        }
        else{
            //if the selected start is none, it means we need to change the second selected
            selectedLand= coordinate;
            //if the landing cell is on the board and not invalid & the cell is not a frog
            if(currentConfig.isCellValid(selectedLand)&&!currentConfig.getCurFrogs().containsKey(selectedLand)){

                Frog frogStart = currentConfig.getCurFrogs().get(selectedStart);

                for(Coordinates nbrCoord:frogStart.getNeighbors().keySet()){
                    //check if the current neighbor is valid
                    if(currentConfig.isCellValid(nbrCoord)&&currentConfig.getCurFrogs().containsKey(nbrCoord)){

                        HoppersConfig.HOPPER_DIRECTIONS going = frogStart.getNeighbors().get(nbrCoord);
                        Frog jumped = currentConfig.getCurFrogs().get(nbrCoord);
                        if(jumped.getNeighbor(jumped.getCurLocation(),going).equals(selectedLand)){
                           if(jumped.getTypeFrog()!=currentConfig.RED) {

                               currentConfig = new HoppersConfig(currentConfig, selectedStart, selectedLand, jumped.getCurLocation());
                               notifyObservers("Jumped from " + selectedStart + "  to " + selectedLand);
                               this.init();
                           }
                           else{notifyObservers("Can't jump from "+selectedStart+"  to "+ selectedLand);
                               init();}


                        }

                    }

                }

                //currently we are not updating the configurations and actually making a frog jump, so the output is off, need to fix that
            }
            else{
                notifyObservers("Can't jump from "+selectedStart+"  to "+ selectedLand);
                init();
            }
        }
    }

    /**
     * This function reveals the next configuration/step to getting closer to the solution. If the current configuration
     * is unsolvable, the model tells the view to represent that.
     *
     * It calls upon a solver and retrieves the next step in the sequence/path to obtaining the solution.
     * If there is a next step, the model notifies all observers.
     *
     * if the current configuration is the solution, the model notifies the observers that the player has reached the end
     *
     * if the configuration path of the solver is less than 1, it notifies the view that there is no solution.
     */

    public void hint(){
        Solver nextConfig = new Solver();
        nextConfig.solve(currentConfig);
        if(nextConfig.getConfigPath().size()>1){
            currentConfig = (HoppersConfig) nextConfig.getConfigPath().get(1);
            notifyObservers("Next step!");
        }
        else if(currentConfig.isSolution()){notifyObservers("You've reached the end!");}
        else{
            notifyObservers("No Solution!");
        }

    }
    @Override
    public String toString() {
        StringBuilder result = new StringBuilder(" ");
        result.append(" ");
        for (int col = 0; col < currentConfig.getBoard()[0].length; col++) {
            result.append(String.format("%" + (2) + "d", col));
        }
        result.append("\n  ");
        for (int col = 0; col < currentConfig.getBoard()[0].length; col++) {
            result.append("-".repeat( 2));
        }
        result.append(System.lineSeparator());
        for (int row = 0; row < currentConfig.getBoard().length; ++row) {
            result.append(String.format("%d|", row));
            for (int col = 0; col < currentConfig.getBoard()[0].length; ++col) {
                char value = this.currentConfig.getBoard()[row][col];

                    result.append(String.format("%" + (2)+ "s", value));
                }
            result.append(System.lineSeparator());
            }
        return result.toString();
    }

    /**
     * This function returns the current configuration that the model is holding
     * @return the current Hopperconfiguration
     */
    public HoppersConfig getCurrentConfig() {
        return currentConfig;
    }
}
