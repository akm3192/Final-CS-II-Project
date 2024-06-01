package puzzles.hoppers.ptui;

import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;
import java.util.Scanner;

/**
 * This file contains the PTUI and its functions/model to run the game smoothly
 * It serves as the view/controller part of a MVC
 *
 * @author Ashley Miller
 */
public class HoppersPTUI implements Observer<HoppersModel, String> {
    /**
     * This holds the current model that will be used in the main program
     */
    private HoppersModel model;

    /**
     * This function initializes the PTUI by creating the model, and adding itself as an observer to it, it then cals
     * the model's loaded() method(which notifies the observer that the model has loaded the file) and displays the help
     * features
     * @param filename the name of the file in which model uses to create itself and the currentconfig/starting config
     * @throws IOException throws an IOException if there is no file found with the given name
     */

    public void init(String filename) throws IOException {
        this.model = new HoppersModel(filename);
        this.model.addObserver(this);
        //need it to see
        this.model.loaded();
        displayHelp();
    }

    @Override
    public void update(HoppersModel model, String data) {
        // for demonstration purposes
        System.out.println(data);
        System.out.println(model);
    }

    /**
     * This function displays the different commands that the user can use to interact with the game
     */

    private void displayHelp() {
        System.out.println( "h(int)              -- hint next move" );
        System.out.println( "l(oad) filename     -- load new puzzle file" );
        System.out.println( "s(elect) r c        -- select cell at r, c" );
        System.out.println( "q(uit)              -- quit the game" );
        System.out.println( "r(eset)             -- reset the current game" );
    }

    /**
     * This is the run function, when it is called, the PTUI runs, asking for user input until the user quits (does so
     * by typing a word that starts with "q")
     *
     * if the user types an invalid command, the program just displays the help message again until they input a valid command
     *
     * There are 5 commands:
     *      q - quit, system exits if the user types this in.
     *      r- reset, calls the model's new game function
     *      l- load, calls teh model's load function and if it throws an error, catches it and displays the error message
     *      s- select, calls the model's select function so the user can select/jump to a spot pn the board
     *      h - hint, calls the model's hint function to display to the user the next step/advance the puzzle.
     *
     *
     *
     */
    public void run() {
        Scanner in = new Scanner( System.in );
        for ( ; ; ) {
            System.out.print( "> " );
            String line = in.nextLine();
            String[] words = line.split( "\\s+" );
            if (words.length > 0) {
                if (words[0].startsWith( "q" )) {
                    break;
                }
                else if(words[0].startsWith( "r" )){
                    model.newGame();
                }
                else if(words[0].startsWith( "l" )){
                    try{model.load(words[1]);}
                    catch (IOException e){

                    }
                }
                else if(words[0].startsWith( "s" )){
                    model.select(new Coordinates(words[1],words[2]));
                }
                else if(words[0].startsWith( "h" )){
                    model.hint();
                }
                else {
                    displayHelp();
                }
            }
        }
    }

    /**
     * This is the main function, it creates the PTUI and calls init and run when given a valid filename
     * @param args - input of the text file to be used to create the board/game
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            try {
                HoppersPTUI ptui = new HoppersPTUI();
                ptui.init(args[0]);
                ptui.run();
            } catch (IOException ioe) {
                System.out.println(ioe.getMessage());
            }
        }
    }
}
