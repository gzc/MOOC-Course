import java.util.ArrayList;
import java.util.Arrays;

public class Fast {
	
	private static boolean test(double f1, double f2)
	{		
		if(f1 == Double.NEGATIVE_INFINITY && f2 == Double.NEGATIVE_INFINITY)
			return true;
		else if(f1 == Double.POSITIVE_INFINITY && f2 == Double.POSITIVE_INFINITY)
			return true;
		else if(Math.abs(f1-f2) < 1e-8)
			return true;
		return false;
	}
	
	private static boolean check(Point[] points, int high, Point p, double f)
	{
		for(int i = 0;i < high;i++)
		{
			double slope = p.slopeTo(points[i]);
			if(test(slope, f)) return false;
		}
		return true;
	}
	
	private static void apply(Point[] points, int k)
	{
		Arrays.sort(points);
		points[0].drawTo(points[k-1]);
		for(int i = 0;i < k;i++)
		{
			if(i == k - 1) System.out.println(points[i]);
			else System.out.print(points[i] + " -> ");
		}
	}
	
	public static void main(String[] args)
	{
		String filename = args[0];
		In in = new In(filename);
		int times = in.readInt();
		Point[] points = new Point[times];
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		for(int i = 0;i < times;i++)
		{
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
			points[i].draw();
		}
		
		for(int i = 0;i < times;i++)
		{
			Point p = points[i];
			Arrays.sort(points, i, times, p.SLOPE_ORDER);
			int j = i+1;
			int start = j;
			double f = 0.0;
			boolean first = true;
			
			while(j < times)
			{
				if(first)
				{
					f = p.slopeTo(points[j]);
					start = j;
					first = false;
				} else {
					double v = p.slopeTo(points[j]);
					if(test(f, v))
					{
						if(j == times - 1)
						{
							if(j - start >= 2 && check(points, i, p, f)) 
							{
								int size = j-start+2;
								Point[] tmp = new Point[size];
								int m = 0;
								tmp[m++] = p;
								for(;start <= j;start++)
								{
									tmp[m++] = points[start];
								}
								apply(tmp, size);
							}
							start = j;
							f = v;
						}
					} else {
						if(j - start >= 3 && check(points, i, p, f)) 
						{
							int size = j - start + 1;
							Point[] tmp = new Point[size];
							int m = 0;
							tmp[m++] = p;
							for(;start < j;start++)
							{
								tmp[m++] = points[start];
							}
							apply(tmp, size);
						}
						start = j;
						f = v;
					}
				}
				
				j++;
			}
			
		}
	}
}
