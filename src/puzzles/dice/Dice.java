package puzzles.dice;

import puzzles.common.solver.Solver;

import java.util.LinkedList;

/**
 * This program represents a dice problem, it obtains the arguments and passes them through to a dice config
 * It also utilizes solver to take in that config and solve the puzzle, printing out the steps to reach the solution.
 * @author Ashley
 */
public class Dice {
    /**
     * This main method implements the dice, creates the dice config and solves the problem.
     * @param args - the configuration arguments
     */
    public static void main(String[] args) {
        if (args.length < 3) {
            System.out.println("Usage: java Dice start end die1 die2...");
        } else {
            String path="";
            Solver solvit = new Solver();
            //need a list of dice from the arguments

            DiceConfig dice =new DiceConfig(args);

            String diceOutput = "";
            for(Die die:dice.getDices() ){
                diceOutput+=die;
            }

            System.out.print(diceOutput+ "Start: "+args[0]+", End: "+ args[1]+ "\n"+solvit.solve(dice));


        }
    }

}