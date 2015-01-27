public class Percolation {
	
   public Percolation(int N)               // create N-by-N grid, with all sites blocked
   {
	   if(N <= 0) throw new java.lang.IllegalArgumentException();
	   this.N = N;
	   wq = new WeightedQuickUnionUF(N*N);
	   mark = new boolean[N+1][N+1];
	   flag = new boolean[N*N];
	   flag2 = new boolean[N*N];
	   isOK = false;
   }
   
   
   public void open(int i, int j)          // open site (row i, column j) if it is not open already
   {
	   if(i < 1 || i > N ) throw new java.lang.IndexOutOfBoundsException();
	   if(j < 1 || j > N ) throw new java.lang.IndexOutOfBoundsException();
	   if(mark[i][j] == true) return;
	   mark[i][j] = true;
	   boolean fff = false, ttt = false;
	   if(i == N) ttt = true;
	   if(i == 1) fff = true;
	   if(i > 1 && mark[i-1][j] == true) {
		   int id = wq.find(convert(i-1,j));
		   fff |= flag[id];
		   ttt |= flag2[id];
		   wq.union(convert(i,j), convert(i-1,j));
	   }
	   if(i < N && mark[i+1][j] == true && !wq.connected(convert(i,j), convert(i+1,j))) {
		   int id = wq.find(convert(i+1,j));
		   fff |= flag[id];
		   ttt |= flag2[id];
		   wq.union(convert(i,j), convert(i+1,j));
	   }
	   if(j > 1 && mark[i][j-1] == true && !wq.connected(convert(i,j), convert(i,j-1))) {
		   int id = wq.find(convert(i,j-1));
		   fff |= flag[id];
		   ttt |= flag2[id];
		   wq.union(convert(i,j), convert(i,j-1));
	   }
	   if(j < N && mark[i][j+1] == true && !wq.connected(convert(i,j), convert(i,j+1))) {
		   int id = wq.find(convert(i,j+1));
		   fff |= flag[id];
		   ttt |= flag2[id];
		   wq.union(convert(i,j), convert(i,j+1));
	   }
	   
	   int id = wq.find(convert(i,j));
	   flag[id] = fff;
	   flag2[id] = ttt;
	   if(ttt && fff) isOK = true;
   }
   
   
   public boolean isOpen(int i, int j)     // is site (row i, column j) open?
   {
	   if(i < 1 || i > N ) throw new java.lang.IndexOutOfBoundsException();
	   if(j < 1 || j > N ) throw new java.lang.IndexOutOfBoundsException();
	   return mark[i][j] == true;
   }
   
   
   public boolean isFull(int i, int j)     // is site (row i, column j) full?
   {
	   if(i < 1 || i > N ) throw new java.lang.IndexOutOfBoundsException();
	   if(j < 1 || j > N ) throw new java.lang.IndexOutOfBoundsException();
	   if(mark[i][j] == false) return false;
	   if(i == 1) return true;
	   return flag[wq.find(convert(i,j))];
   }
   
   
   public boolean percolates()             // does the system percolate?
   {
	   return isOK;
   }
   
   private int convert(int i, int j)
   {
	   return (i-1)*N+(j-1);
   }
   
   private int N;
   private boolean isOK;
   private WeightedQuickUnionUF wq;
   private boolean mark[][];
   private boolean flag[];
   private boolean flag2[];
   
   public static void main(String args[])
   {
	   Percolation p = new Percolation(3);
	   p.open(1, 1);
	   p.open(2, 2);
	   p.open(3, 1);
	   StdOut.println(p.percolates());
   }
}

