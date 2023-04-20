package puzzles.clock;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;

public class ClockConfig implements Configuration {
    /** hour to start at or current hour */
    private int start;
    /** hour to end at */

    private static int end;
    /** number of hours on clock from 1 */

    private static int hours;

    /**
     * Creates a configuration of Clock to represent clock puzzle.
     * @param hours number of hours on clock from 1
     * @param start hour to start at or current hour
     * @param end hour to end at
     */

    public ClockConfig(int hours, int start, int end) {
        this.start = start;
        this.end = end;
        this.hours = hours;
    }

    @Override
    public boolean isSolution() {
        return (this.start == this.end);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> successors = new LinkedList<Configuration>();
        ClockConfig child1 = new ClockConfig(hours, decrement(start), end);
        ClockConfig child2 = new ClockConfig(hours, increment(start), end);
        successors.add(child1);
        successors.add(child2);
        return successors;
    }

    /**
     * increased hour by one. If it is at max hours, then it turns it back to 1
     * @param hour given integer hour
     * @return incremented hour
     */
    public int increment(Integer hour){
        if (hour == hours){
            return 1;
        } else {
            return hour + 1;
        }
    }

    /**
     * decreased hour by one. If it is at 1 hour, then it turns it to max hours.
     * @param hour given integer hour
     * @return decremented hour
     */
    public int decrement(Integer hour){
        if (hour == 1){
            return hours;
        } else {
            return hour - 1;
        }
    }

    @Override
    public boolean equals(Object other) {
        if (other instanceof Configuration) {
            ClockConfig otherconfig = (ClockConfig) other;
            if (otherconfig.start == this.start){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.start;
    }

    @Override
    public String toString() {
        return "Hours: " + hours + " Start: " + start + " End: " + end;
    }

    /**
     * gets starting or current hour
     * @return starting or current hour
     */
    public int getStart(){
        return this.start;
    }
}
