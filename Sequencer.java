import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

/** This is the best implementation, and was the one used to generate the outputs. */

public class Sequencer {
    
    private static StringHolderComparator shc = new StringHolderComparator();
    
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

    static class StringHolderComparator implements Comparator<StringHolder> {
        @Override
        public int compare(StringHolder sh1, StringHolder sh2) {
            if (sh1.overlap > sh2.overlap) {
                return -1;
            } else if (sh1.overlap < sh2.overlap) {
                return 1;
            }
            return 0;
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
