package SequencerLibrary;

/** A StringHolder is a wrapper class for a String, which contains
 *  extra information.
 *  
 *  @author Jerry Cheng
 *  */

public class StringHolder {

    public boolean front; //true if front, false if back
    public String val;
    public int overlap;
    
    public StringHolder(String val, int overlap, boolean front) {
        this.val = val;
        this.overlap = overlap;
        this.front = front;
    }
    
    public StringHolder(String val, int overlap) {
        this.val = val;
        this.overlap = overlap;
    }
    
}
