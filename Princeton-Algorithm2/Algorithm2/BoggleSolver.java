import java.util.ArrayList;
import java.util.HashSet;



public class BoggleSolver
{
	
	private TST<Boolean> dictionaries;
	private int M,N;
	
	private static final int R = 26;        // letters
    private Node root;      // root of trie
    
    private static int offx[] = {-1,0,1,-1,1,-1,0,1};
    private static int offy[] = {-1,-1,-1,0,0,1,1,1};

    // 26-way trie node
    private static class Node {
        private Boolean val;
        private Node[] next = new Node[R];
    }
    
    private void put(String key, Boolean val) {
        root = put(root, key, val, 0);
    }

    private Node put(Node x, String key, Boolean val, int d) {
        if (x == null) x = new Node();
        if (d == key.length()) {
            x.val = val;
            return x;
        }
        char c = key.charAt(d);
        x.next[c-65] = put(x.next[c-65], key, val, d+1);
        return x;
    }
	
    // Initializes the data structure using the given array of strings as the dictionary.
    // (You can assume each word in the dictionary contains only the uppercase letters A through Z.)
    public BoggleSolver(String[] dictionary) 
    {
    	dictionaries = new TST<Boolean>();
    	for(int i = 0;i < dictionary.length;i++)
    		put(dictionary[i], true);
    }

    // Returns the set of all valid words in the given Boggle board, as an Iterable.
    public Iterable<String> getAllValidWords(BoggleBoard board)
    {
    	M = board.rows();
    	N = board.cols();
    	ArrayList<String> results = new ArrayList<String>();
    	boolean[][] path = new boolean[M][N];
    	
    	for(int i = 0;i < M;i++)
    	{
    		for(int j = 0;j < N;j++)
    		{
    			getWords(i, j, path, results, board, root, "");
    		}
    	}
    	
    	return results;
    }
    
    private void getWords(int row, int column, boolean[][] path, ArrayList<String> results, BoggleBoard board, Node node, String s)
    {
    	char ch = board.getLetter(row, column);
    	Node child = null;
    	String tmp = null;
    	if(ch != 'Q')
    	{	
    		child = node.next[ch-65];
    		tmp = s+ch;
    		if(child == null)
    		{
    			return;
    		} else if(child.val != null && s.length() >= 2)
    		{
    			if(!results.contains(tmp))
    				results.add(tmp);
    		}
    	} else {
    		child = node.next[ch-65];
    		tmp = s + ch + 'U';
    		if(child == null)
    		{
    			return;
    		}
    		child = child.next['U'-65];
    		if(child == null)
    		{
    			return;
    		}
    		if(child.val != null && s.length() >= 1)
    		{
    			if(!results.contains(tmp))
    				results.add(tmp);
    		}
    	}
    	
    	path[row][column] = true;
    	for(int k = 0;k < 8;k++)
    	{
    		int y = row + offy[k];
    		int x = column + offx[k];
    		if(x < 0 || y < 0 || x >= N || y >= M)
    			continue;
    		else if(path[y][x] == true)
    			continue;
    		else
    		{
    			getWords(y, x, path, results, board, child, tmp);
    		}
    	}
    	path[row][column] = false;
    }
    
    private boolean contains(String key) {
        Node x = get(root, key, 0);
        if (x == null) return false;
        if (x.val == null) return false;
        if (x.val == true) return true;
        return true;
    }


    private Node get(Node x, String key, int d) {
        if (x == null) return null;
        if (d == key.length()) return x;
        char c = key.charAt(d);
        return get(x.next[c-65], key, d+1);
    }

    
    
    // Returns the score of the given word if it is in the dictionary, zero otherwise.
    // (You can assume the word contains only the uppercase letters A through Z.)
    public int scoreOf(String word)
    {
    	if(!contains(word))
    		return 0;
    	
    	if(word.length() <= 2)
    		return 0;
    	else if(word.length() <= 4)
    		return 1;
    	else if(word.length() == 5)
    		return 2;
    	else if(word.length() == 6)
    		return 3;
    	else if(word.length() == 7)
    		return 5;
    	else
    		return 11; 	
    }
    
    
    public static void main(String[] args)
    {
    	In in = new In("dictionary-16q.txt");
        String[] dictionary = in.readAllStrings();
        BoggleSolver solver = new BoggleSolver(dictionary);
        BoggleBoard board = new BoggleBoard("board-16q.txt");
        int score = 0;
        solver.getAllValidWords(board);
        for (String word : solver.getAllValidWords(board))
        {
            StdOut.println(word);
            score += solver.scoreOf(word);
        }
        StdOut.println("Score = " + score);
    }
    
    
}