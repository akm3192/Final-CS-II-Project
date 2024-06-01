package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/**
 * This is the clock Configuration, it holds the current clock formation, the start,end, current value of it.
 * It implements the configuration interface
 *
 * @author Ashley Miller
 */

public class ClockConfig implements Configuration {
    /**
     * This integer represents the first hour the clock start at
     */
    private static int start;
    /**
     * This integer represents the total number of hours on the clock.
     */
    private static int hours;
    /**
     *  * This integer represents the goal hour the clock should reach
     *
     */
    private static int end;
    /**
     * This integer represents the current hour
     */
    private int cur;
    /**
     * This hash map represents the neighbors of each config, should be the same among all clock
     */
    private static HashMap<ClockConfig, LinkedList<ClockConfig>> neighbors;
    public ClockConfig(int hours, int start,int end){
        this.hours= Integer.valueOf(hours);

        this.start= Integer.valueOf(start);
        this.end= Integer.valueOf(end);
        this.cur = start;
        neighbors= new HashMap<ClockConfig, LinkedList<ClockConfig>>();
    }

    /**
     * This constructor represents the copy constructor of the clokc configuration
     * It takesi n the currentTime, and sets the cur = to it
     * @param currentTime
     */
    public ClockConfig(int currentTime){
        this.cur = currentTime;
    }

    /**
     * This function adds all the neighbors of the clock to hashmap of neighbors (1 before and 1 after)
     *
     */
    public void addNeighbors(){
        LinkedList<ClockConfig> thisNeighbors= new LinkedList<ClockConfig>();
        if(this.cur!=1 &&cur!=hours){
            thisNeighbors.add(new ClockConfig(cur-1));
            thisNeighbors.add(new ClockConfig(cur+1));}
        else if(cur==hours){
            thisNeighbors.add(new ClockConfig(cur-1));
            thisNeighbors.add(new ClockConfig(1));
        }
        else if(cur==1){
            thisNeighbors.add(new ClockConfig(hours));
            thisNeighbors.add(new ClockConfig(cur+1));
        }

        neighbors.put(this,thisNeighbors);
    }

    @Override
    public boolean isSolution() {
        if (this.cur == this.end){return true;}
        return false;
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        this.addNeighbors();
        Collection<Configuration> allNeighbors = new LinkedList<Configuration>();
        for(Configuration neighbor: neighbors.get(this)){
            allNeighbors.add(neighbor);
        }
        return allNeighbors;
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof ClockConfig){
            ClockConfig otro = (ClockConfig) other;
            return otro.hashCode() == this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.cur;
    }

    @Override
    public String toString() {
        return String.valueOf(cur+ " ");
    }
}

