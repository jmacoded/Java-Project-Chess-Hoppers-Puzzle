package puzzles.common.solver;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;


/**
 * The Solver for different game modes
 *
 * @author Teju Rajbabu & Jamie Antal
 */

public class Solver {
    /** Hashmap of Configuration keys and Configuration values*/
    private HashMap<Configuration, Configuration> map;
    /** Linked list of configurations, the queue*/

    private LinkedList<Configuration> queue;
    /** Array list of configurations, makes the list of the shortest path */
    private ArrayList<Configuration> shortestlist;
    /** Total number of configurations made */

    private int TotalConfigs;
    /** Total number of unique configurations made */

    private int UniqueConfigs;

    /**
     * Starts up the Solver application to solve either Clock or String puzzle
     * @param Config configuration taken from either Clock or String
     */

    public Solver(Configuration Config) {
        queue = new LinkedList<Configuration>();
        map = new HashMap<Configuration, Configuration>();
        ArrayList<Configuration> fliplist = new ArrayList<Configuration>();
        shortestlist = new ArrayList<Configuration>();
        queue.add(Config);
        map.put(Config, null);
        TotalConfigs = 1;
        UniqueConfigs = 1;
        while (!queue.isEmpty()){
            Configuration removed = queue.remove(0);
            if (removed.isSolution()){
                while (map.get(removed) != null){
                    fliplist.add(removed);
                    removed = map.get(removed);
                }
                fliplist.add(removed);
                for (int i = fliplist.size(); i-- > 0;){
                    shortestlist.add(fliplist.get(i));
                }
                break;
            } else {
                for (Configuration fig: removed.getNeighbors()){
                    TotalConfigs += 1;
                    if (!map.containsKey(fig)){
                        UniqueConfigs += 1;
                        queue.add(fig);
                        map.put(fig, removed);
                    }
                }
            }
        }
    }

    /**
     * gets the list of shortest path to result
     * @return ArrayList of configurations of shortest path
     */
    public ArrayList<Configuration> getShortestlist(){
        return shortestlist;
    }

    /**
     * gets number of total configurations
     * @return integer total configurations made
     */
    public int getTotalConfigs() {
        return TotalConfigs;
    }

    /**
     * gets number of unique configurations
     * @return integer unique configurations made
     */
    public int getUniqueConfigs(){
        return UniqueConfigs;
    }

}
