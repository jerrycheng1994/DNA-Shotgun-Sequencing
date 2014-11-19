package SequencerLibrary;

import java.util.Comparator;

/** A StringLengthComparator compares Strings in terms of length.
 * 
 * @author Jerry Cheng
 * */

public class StringLengthComparator implements Comparator<String>{
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
