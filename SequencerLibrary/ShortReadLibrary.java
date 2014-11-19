package SequencerLibrary;

import java.util.ArrayList;
import java.util.Iterator;

/** A collection of methods used to handle ShortReads. */

public class ShortReadLibrary {

    /** Given two strings FRONT and BACK, returns the number of characters that overlap
     *  between characters at the end of front and the start of back.*/
    public static int findOverlap(String front, String back) {
        int i = 0;
        int maxOverlap = 0;
        while (i < front.length() && i < back.length()) {
            int overlap = 0;
            int j = 0;
            while ( j <= i && front.charAt(front.length()+j-1-i) == back.charAt(j)) {
                overlap += 1;
                j += 1;
            }
            if (j-1 == i && overlap > maxOverlap) {
                maxOverlap = overlap;
            }
            i += 1;
        }
        return maxOverlap;
    }
    
    /** Given a string READ, removes any string in the ArrayList SHORTREADS that is
     *  a substring of READ. */
    public static void removeRedundantReads(String read, ArrayList<String> shortReads) {
        Iterator<String> iter = shortReads.iterator();
        while (iter.hasNext()) {
            if (read.contains(iter.next())) {
                iter.remove();
            }
        }
    }
    
}
