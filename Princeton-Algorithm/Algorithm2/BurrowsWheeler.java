import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class BurrowsWheeler {
    // apply Burrows-Wheeler encoding, reading from standard input and writing to standard output
    public static void encode()
    {
        String s = BinaryStdIn.readString();
        CircularSuffixArray csa = new CircularSuffixArray(s);
        for(int i = 0;i < s.length();i++)
        {
        	int index = csa.index(i);
        	if(index == 0) {
        		BinaryStdOut.write(i);
        		break;
        	}
        }
        for(int i = 0;i < s.length();i++)
        {
        	int index = csa.index(i);
        	if(index != 0) {
        		BinaryStdOut.write(s.charAt(index-1));
        	}
        	else
        		BinaryStdOut.write(s.charAt(s.length()-1));
        }
        BinaryStdOut.flush();
    }

    // apply Burrows-Wheeler decoding, reading from standard input and writing to standard output
    public static void decode()
    {
    	int first = BinaryStdIn.readInt();
    	String s = BinaryStdIn.readString();
    	
    	int N = s.length();
    	int R = 256;
    	int[] count = new int[R+1];
    	char[] aux = new char[N];
    	int[] next = new int[N];
    	for(int i = 0;i < N;i++)
    		count[s.charAt(i)+1]++;
    	for(int r = 0;r < R;r++)
    		count[r+1] += count[r];
    	for(int i = 0;i < N;i++) {
    		int tmp = count[s.charAt(i)]++;
    		aux[tmp] = s.charAt(i);
    		next[tmp] = i;
    	}
    	
    	for(int i = 0;i < N;i++)
    	{
    		BinaryStdOut.write(aux[first]);
    		first = next[first];
    	}

    	BinaryStdOut.flush();
    }

    // if args[0] is '-', apply Burrows-Wheeler encoding
    // if args[0] is '+', apply Burrows-Wheeler decoding
    public static void main(String[] args)
    {
    	BurrowsWheeler bw = new BurrowsWheeler();

    	if (args[0].equals("-"))
        {
            bw.encode();
        }
        else if (args[0].equals("+"))
        {
            bw.decode();
        }
        else
        {
            throw new RuntimeException("Illegal command line argument");
        }
    }
}