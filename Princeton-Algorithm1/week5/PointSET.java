import java.util.Iterator;
import java.util.TreeSet;

public class PointSET {
	
	private TreeSet<Point2D> ts;
	
	public PointSET()                               // construct an empty set of points
	{
		ts = new TreeSet<Point2D>();
	}
   
	public boolean isEmpty()                      // is the set empty? 
	{
		return ts.isEmpty();
	}
   
	public int size()                         // number of points in the set 
	{
		return ts.size();
	}
   
	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if(p == null) throw new java.lang.NullPointerException();
		if(!contains(p)) ts.add(p);
	}
   
	public boolean contains(Point2D p)            // does the set contain point p? 
	{
		if(p == null) throw new java.lang.NullPointerException();
		return ts.contains(p);
	}
   
	public void draw()                         // draw all points to standard draw 
	{
		for(Point2D p : ts)
			p.draw();
	}
   
   public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
   {
	   if(rect == null) throw new java.lang.NullPointerException();
	   Queue<Point2D> q = new Queue<Point2D>();
       for (Point2D p : ts) {
           if (rect.contains(p))
               q.enqueue(p);
       }
       return q;
   }
   
   public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
   {
	   if(p == null) throw new java.lang.NullPointerException();
	   if(isEmpty()) return null;
	   double minDistance = Double.MAX_VALUE;
	   Point2D res = null;
	   for(Point2D pp : ts)
	   {
		   double v = pp.distanceSquaredTo(p);
		   if(v < minDistance)
		   {
			   minDistance = v;
			   res = pp;
		   }
	   }
	   return res;
   }
   
   public static void main(String[] args)                  // unit testing of the methods (optional) 
   {
	   Point2D p1 = new Point2D(0.1,0.2);
	   Point2D p2 = new Point2D(0.8,0.9);
	   Point2D p3 = new Point2D(0.5,0.2);
	   Point2D p4 = new Point2D(0.5,0.5);
	   Point2D p5 = new Point2D(0.2,0.7);
	   
	   PointSET ps = new PointSET();
	   ps.insert(p1);
	   ps.insert(p2);
	   ps.insert(p3);
	   ps.insert(p4);
	   ps.insert(p5);
	   
	   StdDraw.setPenColor(StdDraw.BLACK);
	   StdDraw.setPenRadius(.01);
	   ps.draw();
	   System.out.println(ps.nearest(new Point2D(0.4,0.4)));
   }
}