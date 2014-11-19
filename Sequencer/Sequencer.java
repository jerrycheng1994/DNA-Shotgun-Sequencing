package Sequencer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import SequencerLibrary.StringHolder;
import SequencerLibrary.StringHolderComparator;

import static SequencerLibrary.ShortReadLibrary.*;

/** Greedy with backtracking. This algorithm takes a random substring from the input, 
 *  finds the substring with the greatest overlap, and joins the two, continuously
 *  doing this until it finds the word, or has to backtrack. It is exhaustive and will
 *  find a solution if one exists.
 *  
 *  @author Jerry Cheng
 *  */

public class Sequencer {
    
    private static StringHolderComparator shc = new StringHolderComparator();

    public static void main(String args[]) throws FileNotFoundException {
        Scanner input;
        ArrayList<String> shortReads;
        for (String arg : args) {
            File file = new File("Dataset/" + arg);
            input = new Scanner(file);
            shortReads = new ArrayList<String>();
            while (input.hasNextLine()) {
                shortReads.add(input.nextLine());
            }
            findSequence(shortReads);
        }
    }
    
    public static void findSequence(ArrayList<String> shortReads) {
        ArrayList<String> newShortReads = new ArrayList<String>(shortReads);
        String read = newShortReads.remove(0);
        findSequenceHelper(read, newShortReads);
    }
    
    public static boolean findSequenceHelper(String soFar, ArrayList<String> shortReads) {
        removeRedundantReads(soFar, shortReads);
        PriorityQueue<StringHolder> queue = new PriorityQueue<StringHolder>(11, shc);
        for (String candidate: shortReads) {
            int backOverlap = findOverlap(soFar, candidate);
            if (backOverlap != 0) {
                StringHolder cur = new StringHolder(candidate, backOverlap, false);
                queue.add(cur);
            }
            int frontOverlap = findOverlap(candidate, soFar);
            if (frontOverlap != 0) {
                StringHolder cur = new StringHolder(candidate, frontOverlap, true);
                queue.add(cur);
            }
        }
        if (queue.isEmpty()) {
            if (shortReads.isEmpty()) {
                System.out.println(soFar);
                System.out.println(soFar.length());
                return true;
            }
            return false;
        }
        while (!queue.isEmpty()) {
            StringHolder cur = queue.poll();
            String newSoFar;
            if (cur.front) {
                newSoFar = cur.val + soFar.substring(cur.overlap, soFar.length());
            } else {
                newSoFar = soFar.substring(0, soFar.length() - cur.overlap) + cur.val;
            }
            ArrayList<String> newShortReads = new ArrayList<String>(shortReads);
            newShortReads.remove(cur.val);
            if (findSequenceHelper(newSoFar, newShortReads)) {
                return true;
            }
        }
        return false;
    }
    
}
