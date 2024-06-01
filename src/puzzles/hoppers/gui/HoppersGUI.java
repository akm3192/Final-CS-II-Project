package puzzles.hoppers.gui;

import javafx.geometry.Pos;
import java.io.*;
import javax.swing.*;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;
import puzzles.common.Coordinates;
import puzzles.common.Observer;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import javafx.application.Application;
import javafx.stage.Stage;

import java.io.IOException;

public class HoppersGUI extends Application implements Observer<HoppersModel, String> {
    /** The resources directory is located directly underneath the gui package */
    private final static String RESOURCES_DIR = "resources/";

    // for demonstration purposes
    /**
     * This Image variable holds the image of the red frog
     */
    private Image redFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"red_frog.png"));
    /**
     * This Image variable holds the image of the lily pad
     */
    private Image lilyPad = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"lily_pad.png"));
    /**
     * This Image variable holds the image of the green frog
     */
    private Image greenFrog = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"green_frog.png"));
    /**
     * This Image variable holds the image of the water
     */
    private Image water = new Image(getClass().getResourceAsStream(RESOURCES_DIR+"water.png"));

    /** The size of all icons, in square dimension */
    private final static int ICON_SIZE = 75;
    /** This label controls the top of the javafx*/
    /**
     * This label serves as the message on the top of the screen, giving messages to the user
     */
    private Label messageBoard;
    /**
     * This string represents the current message to be displayed
     */
    private String curMssg="";
    /**
     * This represents teh current model that the GUI is connected to
     */
    private HoppersModel model;
    /**
     * This represents the name of the file the model and the model's current configuration is based off of
     */
    private String filename;
    /**
     * This represents the game board, a gridpane of buttons in javaFx
     */
    private GridPane gameBoard;


    /**
     * This initializes the GUI, setting the filename to the input the user provided
     * It creates a new model with the filename, if it throws an error, it catches it
     */
    public void init() {
        String filename = getParameters().getRaw().get(0);
        this.filename=filename;
        try{this.model = new HoppersModel(filename);}
        catch(IOException e){

        }
    }

    @Override
    public void start(Stage stage) throws Exception {
        //creates the border pan, that will hold the other items
        BorderPane border = new BorderPane();
        //creates the gridpane in the center that represents the game board
        this.gameBoard = new GridPane();
        Button cell;
        //creates the grid pane, by calling upon the board of the currentconfiguration in the model, parsing through and
        // assigning the resprective image based on teh character held at the specified row and column
        for(int row =0;row<model.getCurrentConfig().getBoard().length;row++){

            for(int col =0;col<model.getCurrentConfig().getBoard()[0].length;col++){
                cell= new Button();
                Coordinates coordi = new Coordinates(row, col);
                cell.setOnAction(event ->{this.model.select(coordi);});
                cell.setGraphic(new ImageView(lilyPad));
                if(this.model.getCurrentConfig().getBoard()[row][col]== this.model.getCurrentConfig().EMPTY) {

                    cell.setGraphic(new ImageView(lilyPad));
                    cell.setMinSize(ICON_SIZE, ICON_SIZE);
                    cell.setMaxSize(ICON_SIZE, ICON_SIZE);

                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]== this.model.getCurrentConfig().GREEN){

                    cell.setGraphic(new ImageView(greenFrog));
                    cell.setMinSize(ICON_SIZE, ICON_SIZE);
                    cell.setMaxSize(ICON_SIZE, ICON_SIZE);

                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]== this.model.getCurrentConfig().RED){

                    cell.setGraphic(new ImageView(redFrog));
                    cell.setMinSize(ICON_SIZE, ICON_SIZE);
                    cell.setMaxSize(ICON_SIZE, ICON_SIZE);

                }
                else if(this.model.getCurrentConfig().getBoard()[row][col]== this.model.getCurrentConfig().INVALID){

                    cell.setGraphic(new ImageView(water));
                    cell.setMinSize(ICON_SIZE, ICON_SIZE);
                    cell.setMaxSize(ICON_SIZE, ICON_SIZE);

                }

                gameBoard.add(cell,col,row );


            }

        }

        border.setCenter(gameBoard);
        gameBoard.setAlignment(Pos.CENTER);
        this.messageBoard = new Label(curMssg);
        messageBoard.setPrefWidth(this.model.getCurrentConfig().getBoard()[0].length*(water.getWidth()));

        messageBoard.setAlignment(Pos.CENTER);
        messageBoard.setTextAlignment(TextAlignment.CENTER);
        border.setTop(messageBoard);

        HBox outer = new HBox();
        outer.setAlignment(Pos.CENTER);
        outer.setPrefWidth(this.model.getCurrentConfig().getBoard()[0].length*water.getWidth());
        HBox inner = new HBox();
        Button load = new Button("Load");
        load.setOnAction(event ->{try{
            JFileChooser fileChooser= new JFileChooser();
                   if( fileChooser.showOpenDialog(null)==0){
                       this.filename =fileChooser.getSelectedFile().getName();
                   }
            this.model.load(this.filename);}
        catch(IOException e){}});
        Button reset = new Button("Reset");
        reset.setOnAction(event ->{this.model.newGame();});

        Button hint = new Button("Hint");
        hint.setOnAction(event ->{this.model.hint();});

        inner.getChildren().add(load);
        inner.getChildren().add(reset);
        inner.getChildren().add(hint);
        outer.getChildren().add(inner);
        border.setBottom(outer);


        Scene scene = new Scene(border);
        stage.setScene(scene);
        stage.setTitle("HoppersGUI");
        this.model.addObserver(this);
        this.model.loaded();

        stage.show();
    }

    @Override
    public void update(HoppersModel hoppersModel, String msg) {
        this.messageBoard.setText(msg);
        for(int row =0;row<model.getCurrentConfig().getBoard().length;row++){

            for(int col =0;col<model.getCurrentConfig().getBoard()[0].length;col++){
                char val = model.getCurrentConfig().getBoard()[row][col];
                Button replace = new Button();
                Coordinates coordi = new Coordinates(row, col);
                replace.setOnAction(event ->{this.model.select(coordi);});
                this.gameBoard.add(replace, col,row);
                if(val== this.model.getCurrentConfig().EMPTY){
                    replace.setGraphic(new ImageView(lilyPad));
                    replace.setMinSize(ICON_SIZE, ICON_SIZE);
                    replace.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
                if(val== this.model.getCurrentConfig().INVALID){
                    replace.setGraphic(new ImageView(water));
                    replace.setMinSize(ICON_SIZE, ICON_SIZE);
                    replace.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
                if(val== this.model.getCurrentConfig().RED){
                    replace.setGraphic(new ImageView(redFrog));
                    replace.setMinSize(ICON_SIZE, ICON_SIZE);
                    replace.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
                if(val== this.model.getCurrentConfig().GREEN){
                    replace.setGraphic(new ImageView(greenFrog));
                    replace.setMinSize(ICON_SIZE, ICON_SIZE);
                    replace.setMaxSize(ICON_SIZE, ICON_SIZE);
                }
            }
        }
    }

    /**
     * this serves as the main function of the GUI
     * If the input is not long enough, the program will display a message, else, it launches javaFx
     * @param args
     */
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java HoppersPTUI filename");
        } else {
            Application.launch(args);
        }
    }
}
