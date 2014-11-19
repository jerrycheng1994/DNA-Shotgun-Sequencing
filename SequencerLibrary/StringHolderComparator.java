package SequencerLibrary;

import java.util.Comparator;

/** A StringHolderComparator is a Comparator that compares the overlap of
 *  two Strings held inside two StringHolders. It is used for when building a
 *  PriorityQueue and orders Strings according to decreasing overlap.
 *  
 *  @author Jerry Cheng
 *  */

public class StringHolderComparator implements Comparator<StringHolder>{
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
