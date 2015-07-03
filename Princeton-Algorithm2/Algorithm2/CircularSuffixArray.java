
public class CircularSuffixArray {
	
	private final int length;
	private int[] index;
	
	private final int CUTOFF =  15;   // cutoff to insertion sort

    private void sort(String a) {
        sort(a, 0, a.length()-1, 0);
    }

    private int charAt(String s, int d) { 
        if (d >= s.length()) 
        	return s.charAt(d-s.length());
        return s.charAt(d);
    }

    // 3-way string quicksort a[lo..hi] starting at dth character
    private void sort(String a, int lo, int hi, int d) { 

        // cutoff to insertion sort for small subarrays
        if (hi <= lo + CUTOFF) {
            insertion(a, lo, hi, d);
            return;
        }

        int lt = lo, gt = hi;
        int v = charAt(a, index[lo]+d);
        int i = lo + 1;
        while (i <= gt) {
            int t = charAt(a, index[i]+d);
            if      (t < v) exch(lt++, i++);
            else if (t > v) exch(i, gt--);
            else	i++;
        }
        //StdOut.println("first partition:");
        //for(int n = 0;n < a.length();n++)
        	//StdOut.print(index[n]+ " ");
        //StdOut.println("pivotc = "+lo + " " + lt + " " + gt + " " + hi);
        // a[lo..lt-1] < v = a[lt..gt] < a[gt+1..hi]. 
        sort(a, lo, lt-1, d);
        if (v >= 0) sort(a, lt, gt, d+1);
        sort(a, gt+1, hi, d);
    }

    // sort from a[lo] to a[hi], starting at the dth character
    private void insertion(String a, int lo, int hi, int d) {
        for (int i = lo; i <= hi; i++)
            for (int j = i; j > lo && less(a, index[j], index[j-1], d); j--)
                exch(j, j-1);
    }

    // exchange a[i] and a[j]
    private void exch(int i, int j) {
        int temp = index[i];
        index[i] = index[j];
        index[j] = temp;
    }

    // is v less than w, starting at character d
    // DEPRECATED BECAUSE OF SLOW SUBSTRING EXTRACTION IN JAVA 7
    // private static boolean less(String v, String w, int d) {
    //    assert v.substring(0, d).equals(w.substring(0, d));
    //    return v.substring(d).compareTo(w.substring(d)) < 0; 
    // }

    // is v less than w, starting at character d
    private boolean less(String a, int j, int k, int d) {
        for (int i = d; i < a.length() - d; i++) {
            if (charAt(a,j+i) < charAt(a,k+i)) return true;
            if (charAt(a,j+i) > charAt(a,k+i)) return false;
        }
        return false;
    }
	
    public CircularSuffixArray(String s)  // circular suffix array of s
    {
    	if(s == null)
    		throw new java.lang.NullPointerException();
    	
    	length = s.length();
    	this.index = new int[length];
        for (int i = 0; i < length; i++)
            index[i] = i;
        sort(s);
    	
    }
    
    public int length()                   // length of s
    {
    	return length;
    }
    
    public int index(int i)               // returns index of ith sorted suffix
    {
    	if(i < 0 || i > length-1)
    		throw new java.lang.IndexOutOfBoundsException();
    	return index[i];
    }
    
    public static void main(String[] args)// unit testing of the methods (optional)
    {
    	String s = "ABRACADABRA!";
    	
    	CircularSuffixArray cs = new CircularSuffixArray(s);
    	for(int i = 0;i < cs.length();i++)
    	{
    		StdOut.println(cs.index(i));
    	}
    	
    }
    
}
