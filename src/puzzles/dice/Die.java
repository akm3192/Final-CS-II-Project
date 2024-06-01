package puzzles.dice;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Set;

import static java.lang.Integer.parseInt;

/**
 * This class represents a die from a specified file which will be ued to represent one character place in the
 * start, end and current string in the clock config.
 * @author Ashley Miller
 */
public class Die {
    /**
     * This static integer represents the next id for the next die created
     */
    private static int nextId=0;
    /**
     * This represents the id of the object
     */
    private int id;
    /**
     * This String represents the filename in which the die is created from
     */
    private String fileName;
    /**
     * This hashMap represents the faces and their neighbors as a set of characters
     */
    private HashMap<Character, Set<Character>> adjacencyList;
    /**
     * This integer represents the number of faces on the die
     */
    private int numFaces;

    /**
     * This constructs a die utilizing a file, creates
     * @param filename - file where the data is extracted from to construct the die
     */
    public Die(String filename) {
        id =nextId;
        nextId++;
        try (BufferedReader in = new BufferedReader(new FileReader(filename))) {
            numFaces = parseInt(in.readLine());
            adjacencyList = new HashMap<Character, Set<Character>>();
            this.fileName= filename;
            in.readLine();
            String[] fields;
            String face = "";
            for (int i = 0; i < Integer.valueOf(this.numFaces); i++) {
                fields = in.readLine().split(" ");
                HashSet<Character> neighborFace = new HashSet<Character>();
                for (int j = 0; j < fields.length; j++) {
                    if (j == 0) {
                        face = fields[0];
                    } else {
                        neighborFace.add(fields[j].charAt(0));
                    }
                    adjacencyList.put(face.charAt(0),neighborFace);
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public String toString() {
        String adjList = "";
        for(Character charac : adjacencyList.keySet()){
            adjList+="\t"+charac+"="+adjacencyList.get(charac)+"\n";
        }
        return "Die #"+id+": File: "+this.fileName+", Faces: "+this.numFaces+"\n"+adjList;
    }

    /**
     * This function retrieves the neighboring faces of the current face represented by character, ch
     * @param ch the face that we will retrieve the neighbors of.
     * @return a set of character that represent the neighboring faces of the character, ch
     */
    public Set<Character> getFaces(char ch){
        return adjacencyList.get(ch);
    }
}

