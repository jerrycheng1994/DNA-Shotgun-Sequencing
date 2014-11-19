package Sequencer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.PriorityQueue;
import java.util.Scanner;

import SequencerLibrary.StringHolder;
import SequencerLibrary.StringHolderComparator;
import SequencerLibrary.StringLengthComparator;

import static SequencerLibrary.ShortReadLibrary.*;

/** This algorithm iterates through all substrings. For each substring s1, the
 *  algorithm finds the substring s2 that has greatest overlap with s1, and 
 *  tacks s2 to the end of s1. It backtracks when necessary, and removes
 *  duplicates occasionally. It is exhaustive, and will find a solution if
 *  one exists. Finally, it returns the string of minimum length from the
 *  results generated from all substrings.
 *  
 *  @author Jerry Cheng
 *  */

public class Sequencer2 {
    
    private static StringHolderComparator shc = new StringHolderComparator();
    private static StringLengthComparator slc = new StringLengthComparator();
    private static PriorityQueue<String> results;
    
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
        results = new PriorityQueue<String>(11, slc);
        for (String read : shortReads) {
            ArrayList<String> newShortReads = new ArrayList<String>(shortReads);
            newShortReads.remove(read);
            removeRedundantReads(read, newShortReads);
            findSequenceHelper(read, newShortReads);
        }
        System.out.println(results.poll());
    }
    
    public static boolean findSequenceHelper(String soFar, ArrayList<String> shortReads) {
        PriorityQueue<StringHolder> queue = new PriorityQueue<StringHolder>(11, shc);
        for (String candidate: shortReads) {
            int overlap = findOverlap(soFar, candidate);
            if (overlap != 0) {
                StringHolder cur = new StringHolder(candidate, overlap);
                queue.add(cur);
            }
        }
        if (queue.isEmpty()) {
            boolean allContains = true;
            for (String candidate: shortReads) {
                if (!soFar.contains(candidate)) {
                    allContains = false;
                }
            }
            if (allContains) {
                results.add(soFar);
                return true;
            }
            return false;
        }
        while (!queue.isEmpty()) {
            StringHolder cur = queue.poll();
            String newSoFar = soFar.substring(0, soFar.length()-cur.overlap) + cur.val;
            ArrayList<String> newShortReads = new ArrayList<String>(shortReads);
            newShortReads.remove(cur.val);
            removeRedundantReads(cur.val, newShortReads);
            if (findSequenceHelper(newSoFar, newShortReads)) {
                return true;
            }
        }
        return false;
    }
    
}
