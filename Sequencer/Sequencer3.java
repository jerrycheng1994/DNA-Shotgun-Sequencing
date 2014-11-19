package Sequencer;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Scanner;

import SequencerLibrary.StringHolder;

import static SequencerLibrary.ShortReadLibrary.*;

/** Greedy without backtracking. This algorithm runs fastest because it
 *  never looks back. It is not exhaustive, and is not guaranteed to produce
 *  the correct results. It produces correct results given enough substrings 
 *  with significant overlap.
 *  
 *  @author Jerry Cheng
 *  */

public class Sequencer3 {
    
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
        String cur = "";
        while (shortReads.size() > 1) {
            cur = shortReads.remove(0);
            StringHolder max = findMaxOverlap(cur, shortReads);
            if (max.overlap == 0) {
                return;
            }
            shortReads.remove(max.val);
            if (max.front) {
                cur = max.val + cur.substring(max.overlap, cur.length());
            } else {
                cur = cur.substring(0, cur.length() - max.overlap) + max.val;
            }
            removeRedundantReads(cur, shortReads);
            shortReads.add(cur);
        }
        System.out.println(cur);
        System.out.println(cur.length());
    }
    
    public static StringHolder findMaxOverlap(String read, ArrayList<String> shortReads) {
        StringHolder best = new StringHolder(null, 0, false);
        for (String candidate : shortReads) {
          int backOverlap = findOverlap(read, candidate);
          if (backOverlap > best.overlap) {
              best = new StringHolder(candidate, backOverlap, false);
          }
          int frontOverlap = findOverlap(candidate, read);
          if (frontOverlap > best.overlap) {
              best = new StringHolder(candidate, frontOverlap, true);
          }
        }
        return best;
    }
    
}
