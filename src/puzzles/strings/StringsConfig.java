package puzzles.strings;

import puzzles.common.solver.Configuration;

import java.util.Collection;
import java.util.LinkedList;


/**
 * The Strings configuration object for the strings puzzle. Implements Configuration
 *
 * @author Teju Rajbabu
 */
public class StringsConfig implements Configuration {
    /** Changing string */

    private String str1;
    /** Ending string */

    private String str2;
    /**
     * Creates a configuration of a String to represent string puzzle.
     * Pointer starts at 0
     * @param str1 Changing string
     * @param str2 Ending string
     */
    public StringsConfig(String str1, String str2){
        this.str1 = str1;
        this.str2 = str2;
    }
    @Override
    public boolean isSolution() {
        return this.str1.equals(this.str2);
    }

    @Override
    public Collection<Configuration> getNeighbors() {
        LinkedList<Configuration> successors = new LinkedList<Configuration>();
        for (int i = 0 ; i < str1.length() ; i++){
            StringsConfig child1 = new StringsConfig(increment(i), str2);
            StringsConfig child2 = new StringsConfig(decrement(i), str2);
            successors.add(child1);
            successors.add(child2);
        }
        return successors;
    }

    /**
     * Increases a character of string (chosen by pointer) by a letter.
     * If it is Z, then changes it to A.
     * @return the full string with changed character
     */
    public String increment(int pointer){
        String increment = "";
        char[] list = str1.toCharArray();
        if (list[pointer] == 'Z'){
            list[pointer] = 'A';
        } else {
            list[pointer] = (char) (list[pointer] + 1);
        }
        for (char letter: list){
            increment += letter;
        }
        return increment;
    }

    /**
     * Decreases a character of string (chosen by pointer) by a letter.
     * If it is A, then changes it to Z.
     * @return the full string with changed character
     */
    public String decrement(int pointer){
        String decrement = "";
        char[] list = str1.toCharArray();
        if (list[pointer] == 'A'){
            list[pointer] = 'Z';
        } else {
            list[pointer] = (char) (list[pointer] - 1);
        }
        for (char letter: list){
            decrement += letter;
        }
        return decrement;
    }
    @Override
    public boolean equals(Object other) {
        if (other instanceof Configuration){
            StringsConfig otherconfig = (StringsConfig) other;
            if (otherconfig.str1.equals(this.str1)){
                return true;
            } else {
                return false;
            }
        }
        return false;
    }

    @Override
    public int hashCode() {
        return this.str1.hashCode();
    }

    @Override
    public String toString() {
        return "Start: " + str1 + ", End: " + str2;
    }

    /**
     * gets the changing String
     * @return changing String
     */
    public String getStr1() {
        return str1;
    }
}
