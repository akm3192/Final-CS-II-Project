package puzzles.clock;

import puzzles.common.solver.Solver;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**This program represents a clock class in java, it holds the clock config, calls upon the solver to
 * find the solution and path to the end configuration.
 * @author Ashley
 */
public class Clock {
    /**
     * this int represents the hours in which the clock has
     */
    private static int hours;
    /**
     * This integer represents where the clock starts from
     */
    private static int start;
    /**
     * This integer represents where the clock is supposed to end
     */
    private static int end;

    /**
     * This main program checks to see if there are any solutions to the problem/inpput. It takes the configuration arguments and
     * creates a config, also creates a solver which is then used to find the shortest path to the end configuration.
     * @param args - the data for the start, end and # of hours the clock has
     */
    public static void main(String[] args) {
        if (args.length != 3) {
            System.out.println("Usage: java Clock hours start end");
        } else {
            hours = Integer.valueOf(args[0]);
            start = Integer.valueOf(args[1]);
            end = Integer.valueOf(args[2]);
            ClockConfig config = new ClockConfig(hours, start,end);

            Solver solvit = new Solver();
            System.out.println("Hours: "+ hours+" Start: "+ start+ " End: "+end);
            String path = solvit.solve(config);

            System.out.print(path);

        }
    }


}