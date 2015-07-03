public class MoveToFront {
    // apply move-to-front encoding, reading from standard input and writing to standard output
    public static void encode()
    {
    	int R = 256;
    	char[] indexs = new char[R];
    	for(int i = 0;i < R;i++)
    		indexs[i] = (char) i;
    	while (!BinaryStdIn.isEmpty())
        {
            char c = BinaryStdIn.readChar();
            char first = indexs[0];
            int i = 0;
            if(first == c)
            	BinaryStdOut.write((char)i);
            else
            {
            	for(int j = 0;j < R;j++)
            	{
            		char second = indexs[j+1];
            		if(second == c){
            			indexs[j+1] = first;
            			indexs[0] = second;
            			i = j+1;
            			break;
            		} else {
            			char tmp = first;
            			first = indexs[j+1];
            			indexs[j+1] = tmp;
            		}
            	}
            	BinaryStdOut.write((char)i);
            }
        }
    	BinaryStdOut.flush();
    }

    // apply move-to-front decoding, reading from standard input and writing to standard output
    public static void decode()
    {
    	int R = 256;
    	char[] indexs = new char[R];
    	for(int i = 0;i < R;i++)
    		indexs[i] = (char) i;
    	
    	while (!BinaryStdIn.isEmpty())
        {
    		char c = BinaryStdIn.readChar();
    		int index = (int)c;
    		BinaryStdOut.write(indexs[index]);
    		
    		char tmp = indexs[0];
    		for(int i = 0;i < index;i++)
    		{
    			char t = indexs[i+1];
    			indexs[i+1] = tmp;
    			tmp = t;
    		}
    		indexs[0] = tmp;
        }
    	
    	BinaryStdOut.flush();

    }

    // if args[0] is '-', apply move-to-front encoding
    // if args[0] is '+', apply move-to-front decoding
    public static void main(String[] args)
    {
    	MoveToFront mt = new MoveToFront();
    	if (args[0].equals("-"))
        {
            mt.encode();
        }
        else if (args[0].equals("+"))
        {
            mt.decode();
        }
        else
        {
            throw new RuntimeException("Illegal command line argument");
        }
    }
}
