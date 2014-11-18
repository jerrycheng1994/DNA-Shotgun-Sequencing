import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;

/** This is an alternate implementation. See Sequencer for actual implementation. */

public class Sequencer3 {

    static class StringHolder {
        boolean front; //true if front, false if back
        String val;
        int overlap;
        
        public StringHolder(String val, int overlap, boolean front) {
            this.val = val;
            this.overlap = overlap;
            this.front = front;
        }
    }
    
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
    
    public static void removeRedundantReads(String read, ArrayList<String> shortReads) {
        Iterator<String> iter = shortReads.iterator();
        while (iter.hasNext()) {
            if (read.contains(iter.next())) {
                iter.remove();
            }
        }
    }
    
}
