import java.util.Comparator;
import java.util.HashMap;

public class Solver {
	
	private Board destination;
	private HashMap<Board,Boolean> twins;
	private HashMap<Board,Board> previous;
	private HashMap<Board,Integer> totalMoves;
	
    public Solver(Board initial)           // find a solution to the initial board (using the A* algorithm)
    {
    	if(initial == null) throw new java.lang.NullPointerException();
    	MinPQ<Board> pq = new MinPQ<Board>(new boardOrder());
    	twins = new HashMap<Board,Boolean>(); 
    	previous = new HashMap<Board,Board>(); 
    	totalMoves = new HashMap<Board,Integer>(); 
    	
    	totalMoves.put(initial, 0);
    	twins.put(initial, false);
    	previous.put(initial, null);
    	pq.insert(initial);
    	
    	Board theTwin = initial.twin();
    	twins.put(theTwin, true);
    	previous.put(theTwin, null);
    	totalMoves.put(theTwin, 0);
    	pq.insert(theTwin);
    	
    	
    	while(!pq.isEmpty())
    	{
    		Board now = pq.delMin();
    		Board pre = previous.get(now);
    		int m = totalMoves.get(now);
    		
    		if (now.isGoal()) {
				destination = now;
				break;
			}

    		for(Board b : now.neighbors())
    		{
    			if(b.equals(pre)) continue;
    			previous.put(b, now);
    			totalMoves.put(b, m+1);
    			pq.insert(b);
    		}
    	}
    }

    private class boardOrder implements Comparator<Board>
    {
    	public int compare(Board q1, Board q2)
    	{
    		int f1 = totalMoves.get(q1) + q1.manhattan();
            int f2 = totalMoves.get(q2) + q2.manhattan();
            if(f1 < f2) return -1;
            else if(f1 == f2) return 0;
            return 1;
    	}
    }
    
    public boolean isSolvable()            // is the initial board solvable?
    {
    	Board temp = destination;
    	while(previous.get(temp) != null)
    		temp = previous.get(temp);
    	return !twins.get(temp);
    }
    
    public int moves()                     // min number of moves to solve initial board; -1 if unsolvable
    {
    	if(!isSolvable()) return -1;
    	return totalMoves.get(destination);
    }
    
    public Iterable<Board> solution()      // sequence of boards in a shortest solution; null if unsolvable
    {
    	if(!isSolvable()) return null;
    	Stack<Board> items = new Stack<Board>();
    	Board temp = destination;
    	while(temp != null)
    	{
    		items.push(temp);
    		temp = previous.get(temp);
    	}
    	return items;
    }
    
    public static void main(String[] args) // solve a slider puzzle (given below)
    {
    	// create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        int[][] blocks = new int[N][N];
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        Board initial = new Board(blocks);

        // solve the puzzle
        Solver solver = new Solver(initial);
        
        
        // print solution to standard output
        if (!solver.isSolvable())
            StdOut.println("No solution possible");
        else {
            StdOut.println("Minimum number of moves = " + solver.moves());
            for (Board board : solver.solution())
                StdOut.println(board);
        }
    }
}