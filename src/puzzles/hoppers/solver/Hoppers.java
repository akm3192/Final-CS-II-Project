package puzzles.hoppers.solver;

import puzzles.common.solver.Solver;
import puzzles.hoppers.model.HoppersConfig;
import puzzles.hoppers.model.HoppersModel;

import java.io.IOException;

public class Hoppers {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Hoppers filename");
        }
        else{
            try{
                HoppersConfig config = new HoppersConfig(args[0]);
                System.out.print(config);
                Solver solveIt = new Solver();
                String path = solveIt.solve(config);

                System.out.print(path);
            }
            catch(IOException e){}
        }
    }
}
