import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.PriorityQueue;
import java.util.Scanner;

/** This is an alternate implementation. See Sequencer for actual implementation.*/

public class Sequencer2 {
    
    private static StringHolderComparator shc = new StringHolderComparator();
    private static StringLengthComparator slc = new StringLengthComparator();
    private static PriorityQueue<String> results;
    
    static class StringHolder {
        String val;
        int overlap;
        
        public StringHolder(String val, int overlap) {
            this.val = val;
            this.overlap = overlap;
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
    
    static class StringLengthComparator implements Comparator<String> {
        @Override
        public int compare(String s1, String s2) {
            if (s1.length() < s2.length()) {
                return -1;
            } else if (s1.length() > s2.length()) {
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
