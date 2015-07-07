import java.util.ArrayList;
import java.util.Arrays;

public class Brute {
	
	private static boolean colinear(Point p1, Point p2, Point p3, Point p4)
	{
		double f1 = p1.slopeTo(p2);
		double f2 = p1.slopeTo(p3);
		double f3 = p1.slopeTo(p4);
		
		if(f1 == Double.NEGATIVE_INFINITY && f2 == Double.NEGATIVE_INFINITY && f3 == Double.NEGATIVE_INFINITY)
			return true;
		else if(f1 == Double.POSITIVE_INFINITY && f2 == Double.POSITIVE_INFINITY && f3 == Double.POSITIVE_INFINITY)
			return true;
		else if(Math.abs(f1-f2) < 1e-8 && Math.abs(f1-f3) < 1e-8 && Math.abs(f2-f3) < 1e-8)
			return true;
		return false;
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
		Arrays.sort(points);
	   
		for(int i = 0;i <= times-4;i++)
		{
			for(int j = i+1;j <= times-3;j++)
			{	
				for(int k = j+1;k <= times-2;k++)
				{
					for(int c = k+1;c <= times-1;c++)
					{
						Point p1 = points[i];
						Point p2 = points[j];
						Point p3 = points[k];
						Point p4 = points[c];
						if(colinear(p1, p2, p3, p4))
						{
							p1.drawTo(p4);
							System.out.println(p1 + " -> " + p2 + " -> " + p3 + " -> " + p4);
						}
					}
				}
			}
		}
	}
   
}