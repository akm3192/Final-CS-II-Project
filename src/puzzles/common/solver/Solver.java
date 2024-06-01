package puzzles.common.solver;

import java.io.ObjectInputFilter;
import java.util.HashMap;
import java.util.LinkedList;

/**
 * This file represents the solver, a class that achieves to solve a configuration by reaching a specified solution
 * The configuration should be telling the solver if it is the solution it is looking for.
 * This utilizes Breadth - First Search in order to find the shortest path from the start to the goal config.
 */
public class Solver {
    /**
     * This LinkedList of String represents the path from the starting point to the end point
     */
    private LinkedList<String> path;
    private LinkedList<Configuration> configPath;
    /**
     * This integer represents the total # of configs created while solving the problem.
     */
    private int total_config;

    /**
     * The constructor of the solver, initializes the total config to 0
     */
    public Solver(){
        total_config=0;
    }

    /**
     *This method begins the process of solving the configuration by creating the predecessor map for the graph
     * The predessor map contains the current config as the key and the previous one as a value. If the neighbor/config is not
     * in the map already, then it adds it to it and puts the neighbor in the queue
     * @param config
     * @return String value, containing the path from the start to the end, calls upon construct path to do this.
     */
    public String solve(Configuration config){
        Configuration start = config;
        total_config++;
        Configuration end = null;
        HashMap<Configuration,Configuration> predecessorMap = new HashMap<Configuration,Configuration>();
        LinkedList<Configuration> queue = new LinkedList<Configuration>();
        queue.add(start);
        predecessorMap.put(start,start);
        while(!queue.isEmpty()) {

            Configuration current = queue.pop();
            if (current.isSolution()) {
                end = current;
                break;
            }
            for(Configuration nbr: current.getNeighbors()){
                total_config++;
                if(!predecessorMap.containsKey(nbr)){
                    predecessorMap.put(nbr,current);
                    queue.add(nbr);
                }
            }
        }

        return constructPath(predecessorMap,end,start);
    }

    /**
     * This function utilizes the predecessor map and configurations to create the path from the start to the end node.
     * @param predecessorMap - map of the configs(key) and their previous config (value)
     * @param end - the end config/goal
     * @param start - the strating config
     * @return string value of the path from the strat to the end.
     */
    public String constructPath(HashMap<Configuration,Configuration> predecessorMap,Configuration end, Configuration start){

        this.path = new LinkedList<>();
        this.configPath = new LinkedList<Configuration>();
        Configuration cur = end;
        if(cur==null){return toString(predecessorMap);}
        while (cur != start) {
            path.addFirst(cur.toString());
            configPath.addFirst(cur);
            cur = predecessorMap.get(cur);
        }
        path.addFirst(start.toString());
        configPath.addFirst(start);

        return toString(predecessorMap);
    }

    /**
     * returns the Linked List of configurations representing the path to the solution
     * @return the cunfigpath
     */
    public LinkedList<Configuration> getConfigPath() {
        return configPath;
    }

    /**
     * This function returns a string which represents the solver, outputs the total, & unique # of configs.
     * It also displays the path in  astep by step basis.
     * @param  map The predecessor map, the configs(key) and their previous config (value)
     * @return a string representing the path from the starting ocnfig to the ending configuration
     */
    public String toString(HashMap<Configuration,Configuration> map){
        String output="Total Configs: "+ total_config+"\nUnique Configs: "+map.size()+"\n";
        int stepsNum=0;
        if (!path.isEmpty()){
            for(String item : path){
                output += "Steps "+stepsNum+": \n"+item+"\n";
                stepsNum++;
            }}
        else{
            output+="No Solution";
        }
        return output;
    }


}
