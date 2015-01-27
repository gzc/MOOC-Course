public class PercolationStats {
	
   public PercolationStats(int N, int T)     // perform T independent experiments on an N-by-N grid
   {
	   if(N <= 0 || T <= 0)
		   throw new java.lang.IllegalArgumentException();
	   this.T = T;
	   this.N = N;
	   result = new double[T];
	   perform();
   }
   
   public double mean()                      // sample mean of percolation threshold
   {
	   return m;
   }
   
   public double stddev()                    // sample standard deviation of percolation threshold
   {
	   return dev;
   }
   
   public double confidenceLo()              // low  endpoint of 95% confidence interval
   {
	   return lo;
   }
   
   public double confidenceHi()              // high endpoint of 95% confidence interval
   {
	   return ho;
   }
   
   private void perform()
   {
	   for(int k = 0;k < T;k++)
	   {
		   int r = 0;
		   Percolation per = new Percolation(N);
		   
		   while(!per.percolates())
		   {
			   int x = StdRandom.uniform(1, N*N+1);
			   int i = (x-1)/N+1;
			   int j = x - N*(i-1);
			   if(!per.isOpen(i, j)) 
			   {
				   per.open(i, j);
				   r++;
			   }
		   }
		   result[k] = r*1.0/(N*N);
	   }
	   
	   double sum = 0;
	   for(int i = 0;i < T;i++)
		   sum += result[i];
	   m = sum/T;
	   
	   sum = 0;
	   for(int i = 0;i < T;i++)
		   sum = sum + (result[i]-m)*(result[i]-m);
	   dev = Math.sqrt(sum/(T-1));
	   
	   lo = m - 1.96*dev/Math.sqrt(T);
	   ho = m + 1.96*dev/Math.sqrt(T);
   }
   
   private double result[];
   private int N,T;
   private double m;
   private double dev;
   private double lo;
   private double ho;

   public static void main(String[] args)    // test client (described below)
   {
	   PercolationStats p = new PercolationStats(Integer.parseInt(args[0]), Integer.parseInt(args[1]));
	   StdOut.println("mean				= " + p.mean());
	   StdOut.println("stddev				= " + p.stddev());
	   StdOut.println("95% confidence interval		= " + p.confidenceLo() + ", " + p.confidenceHi());
   }
}