package puzzles.dice;

import puzzles.clock.ClockConfig;
import puzzles.common.solver.Configuration;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
/**
 * This is the Dice Configuration, it holds all the dies, and the current string/ faces they hold. It recieves the
 * data to create the dice/neighbors from the die class and the main in the dice class
 *It implements the configuration interface
 * @author Ashley Miller
 */
public class DiceConfig implements Configuration {
    /**
     * This String represents the current string/ all the die and their faces combined.
     */
    private String cur;
    /**
     * This String represents the goal string/ all the die and their faces combined.
     */
    private static String end;
    /**
     String represents the starting string/ all the die and their faces combined.*/
    private static String start;
    /**
     * This List represents all the die being used in this config.
     */
    private static List<Die> dices;
    /**
     * Represents all the neighbors for each config with a hashmap
     */
    private static HashMap<Configuration, HashSet<Configuration>> neighbors;

    /**
     * retrieves the die being used to represent each character in the string
     * @return the global list of die
     */

    public static List<Die> getDices() {
        return dices;
    }

    /**
     * This constructs a diceconfig when it is the basis, ie. coming from an input source
     * @param fields
     */
    public DiceConfig(String[] fields){
        neighbors = new HashMap<Configuration, HashSet<Configuration>>();
        dices= new LinkedList<Die>();
        this.start = fields[0];
        cur = fields[0];
        end= fields[1];
        for(int i=2;i< fields.length;i++){
            String filename = "die-"+fields[i]+".txt";
            dices.add(new Die(filename));
        }
    }

    /**
     * The copy constructor of the Dice config
     * @param prev the config we want to copy from
     * @param nextCur the cur value of the copy string
     */
    public DiceConfig(DiceConfig prev, String nextCur){
        this.cur = nextCur;
    }

    @Override
    public boolean isSolution() {
        return cur.equals(end);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        for(int i =0;i<dices.size();i++) {
            for (Character face : dices.get(i).getFaces(cur.charAt(i))) {
                StringBuilder nextCur = new StringBuilder(cur);
                nextCur.setCharAt(i, face);
                if(!neighbors.containsKey(this)){neighbors.put(this, new HashSet<Configuration>());}
                neighbors.get(this).add(new DiceConfig(this, nextCur.substring(0)));

            }
        }

        return neighbors.get(this);
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof DiceConfig){
            DiceConfig otro = (DiceConfig) other;
            return otro.hashCode()==this.hashCode();
        }
        return false;
    }

    @Override
    public int hashCode() {
        String code="";
        for(int i =0;i<cur.length();i++){
            if(cur.charAt(i)<=47||cur.charAt(i)>=57){
                code+=(int)cur.charAt(i);
            }
            else{
                code+=cur.charAt(i);
            }
        }
        return Integer.valueOf(code);
    }

    @Override
    public String toString() {

        return String.valueOf(cur);
    }
}

