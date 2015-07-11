import java.util.Comparator;

public class Board{
	
	private final int N;
	private final int[] blocks;
	private int zeropos;
	
    public Board(int[][] blocks)           // construct a board from an N-by-N array of blocks
                                           // (where blocks[i][j] = block in row i, column j)
    {
    	N = blocks.length;
    	int count = 0;
    	this.blocks = new int[N*N];
    	for(int i = 0;i < N;i++)
    	{
    		for(int j = 0;j < N;j++)
    		{
    			if(blocks[i][j] == 0) zeropos = count+1;
    			this.blocks[count++] = blocks[i][j];
    		}
    	}
    }
    
    
    public int dimension()                 // board dimension N
    {
    	return N;
    }
    
    public int hamming()                   // number of blocks out of place
    {
    	int tmp = 1;
    	int res = 0;
    	for(int i = 0;i < N*N-1;i++)
    	{
    		res += (blocks[i] == tmp? 0 : 1);
    		tmp++;
    	}
    	return res;
    }
    
    public int manhattan()                 // sum of Manhattan distances between blocks and goal
    {
    	int res = 0;
    	for(int i = 0;i < N;i++)
    	{
    		for(int j = 0;j < N;j++)
    		{
    			int v = blocks[i*N+j];
    			if(v == 0) continue;
    			int xx = (v-1) / N;
    			int yy = v - 1 - xx*N;
    			int tmp = res;
    			res += Math.abs(xx-i);
    			res += Math.abs(yy-j);
    			
    		}
    	}
    	return res;
    }
    
    public boolean isGoal()                // is this board the goal board?
    {
    	return this.hamming() == 0;
    }
    
    public Board twin()                    // a board that is obtained by exchanging two adjacent blocks in the same row
    {
    	int[][] twinBlocks = new int[N][N];
    	int count = 0;
    	for(int i = 0;i < N;i++)
    		for(int j = 0;j < N;j++)
    			twinBlocks[i][j] = this.blocks[count++];
    	
    	int xx = (zeropos-1) / N;
		xx = (xx == 0? 1 : 0);

		int temp = twinBlocks[xx][0];
		twinBlocks[xx][0] = twinBlocks[xx][1];
		twinBlocks[xx][1] = temp;
    	Board theTwin = new Board(twinBlocks);
    	return theTwin;
    }
    

	public boolean equals(Object y)        // does this board equal y?
    {
		if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        Board that = (Board) y;
        if (this.N != that.N) return false;
        for(int i = 0;i < N*N;i++)
        	if(blocks[i] != that.blocks[i])
        		return false;
        return true;
    }
    
    public Iterable<Board> neighbors()     // all neighboring boards
    {
    	Stack<Board> items = new Stack<Board>();
    	int xx = (zeropos-1) / N;
		int yy = zeropos - 1 - xx*N;
		
		int[][] tempBlocks = new int[N][N];
		int count = 0;
    	for(int i = 0;i < N;i++)
    	{
    		for(int j = 0;j < N;j++)
    		{
    			tempBlocks[i][j] = blocks[count++];
    		}
    	}
    	
    	if(xx > 0)
    	{
    		tempBlocks[xx][yy] = tempBlocks[xx-1][yy];
    		tempBlocks[xx-1][yy] = 0;
    		Board bro = new Board(tempBlocks);
    		items.push(bro);
    		tempBlocks[xx-1][yy] = tempBlocks[xx][yy];
    		tempBlocks[xx][yy] = 0;
    	}
    	
    	if(xx < N-1)
    	{
    		tempBlocks[xx][yy] = tempBlocks[xx+1][yy];
    		tempBlocks[xx+1][yy] = 0;
    		Board bro = new Board(tempBlocks);
    		items.push(bro);
    		tempBlocks[xx+1][yy] = tempBlocks[xx][yy];
    		tempBlocks[xx][yy] = 0;
    	}
    	
    	if(yy > 0)
    	{
    		tempBlocks[xx][yy] = tempBlocks[xx][yy-1];
    		tempBlocks[xx][yy-1] = 0;
    		Board bro = new Board(tempBlocks);
    		items.push(bro);
    		tempBlocks[xx][yy-1] = tempBlocks[xx][yy];
    		tempBlocks[xx][yy] = 0;
    	}
    	
    	if(yy < N-1)
    	{
    		tempBlocks[xx][yy] = tempBlocks[xx][yy+1];
    		tempBlocks[xx][yy+1] = 0;
    		Board bro = new Board(tempBlocks);
    		items.push(bro);
    		tempBlocks[xx][yy+1] = tempBlocks[xx][yy];
    		tempBlocks[xx][yy] = 0;
    	}
    	
    	return items;
    }
    
    public String toString()               // string representation of this board (in the output format specified below)
    {
    	StringBuilder s = new StringBuilder();
        s.append(N);
        for (int i = 0; i < N*N; i++) {
        	if(i % N == 0) s.append("\n");
        	s.append(String.format("%2d ", blocks[i]));
        }
        s.append("\n");
        return s.toString();
    }
    
    public static void main(String[] args) // unit tests (not graded)
    {
    	In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();

        Board initial = new Board(blocks);
        System.out.println(initial);

        for(Board e : initial.neighbors())
        	System.out.println(e);
    }
}