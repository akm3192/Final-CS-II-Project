package puzzles.astro.solver;

import puzzles.astro.model.AstroConfig;
import puzzles.common.solver.Solver;

import java.io.IOException;

public class Astro {
    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java Astro filename");
        }
        else{
            try{
                AstroConfig astro = new AstroConfig(args[0]);
                System.out.print(astro);
                Solver solve = new Solver();
                String path = solve.solve(astro);
                System.out.println(path);
            }
            catch (IOException e){}

        }
    }
}
