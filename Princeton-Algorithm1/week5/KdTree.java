
public class KdTree {
	
	private static class Node {
		private Point2D p;      // the point
		private RectHV rect;    // the axis-aligned rectangle corresponding to this node
		private Node lb;        // the left/bottom subtree
		private Node rt;        // the right/top subtree
	}
	
	private int size;
	private Node root;

	public KdTree()                               // construct an empty set of points 
	{
		size = 0;
		root = null;
	}
	
	public boolean isEmpty()                      // is the set empty? 
	{
		if(size == 0) return true;
		return false;
	}
	
	public int size()                         // number of points in the set 
	{
		return size;
	}
	
	public void insert(Point2D p)              // add the point to the set (if it is not already in the set)
	{
		if(p == null) throw new java.lang.NullPointerException();
		size++;
		Node subnode = new Node();
		subnode.p = p;
		subnode.lb = null;
		subnode.rt = null;
		int level = 0;
		if(root == null)
		{
			root = subnode;
			RectHV rect = new RectHV(0.0,0.0,1.0,1.0);
			root.rect = rect;
			return;
		}
		else {
			Node temp = root;
			while(temp != null)
			{
				if(temp.p.equals(p)) 
				{
					size--;
					return;
				}
				if(level % 2 == 0)
				{
					if(p.x() < temp.p.x())
					{
						if(temp.lb == null)
						{
							temp.lb = subnode;
							double xmin = temp.rect.xmin();
							double xmax = temp.p.x();
							double ymin = temp.rect.ymin();
							double ymax = temp.rect.ymax();
							subnode.rect = new RectHV(xmin, ymin, xmax, ymax);
							return;
						} else {
							temp = temp.lb;
						}
					} else {
						if(temp.rt == null)
						{
							temp.rt = subnode;
							double xmin = temp.p.x();
							double xmax = temp.rect.xmax();
							double ymin = temp.rect.ymin();
							double ymax = temp.rect.ymax();
							subnode.rect = new RectHV(xmin, ymin, xmax, ymax);
							return;
						} else {
							temp = temp.rt;
						}
					}
				} else {
					if(p.y() < temp.p.y())
					{
						if(temp.lb == null)
						{
							temp.lb = subnode;
							double xmin = temp.rect.xmin();
							double xmax = temp.rect.xmax();
							double ymin = temp.rect.ymin();
							double ymax = temp.p.y();
							subnode.rect = new RectHV(xmin, ymin, xmax, ymax);
							return;
						} else {
							temp = temp.lb;
						}
					} else {
						if(temp.rt == null)
						{
							temp.rt = subnode;
							double xmin = temp.rect.xmin();
							double xmax = temp.rect.xmax();
							double ymin = temp.p.y();
							double ymax = temp.rect.ymax();
							subnode.rect = new RectHV(xmin, ymin, xmax, ymax);
							return;
						} else {
							temp = temp.rt;
						}
					}
				}
				level++;
			}
			
			
		}
		
	}
	
	public boolean contains(Point2D p)            // does the set contain point p? 
	{
		if(p == null) throw new java.lang.NullPointerException();
		Node temp = root;
		int level = 0;
		while(temp != null)
		{
			if(temp.p.equals(p))
				return true;
			if(level % 2 == 0)
			{
				if(p.x() < temp.p.x())
				{
					temp = temp.lb;
				} else {
					temp = temp.rt;
				}
			} else {
				if(p.y() < temp.p.y())
				{
					temp = temp.lb;
				} else {
					temp = temp.rt;
				}
			}
			level++;
		}
		return false;
	}
	
	public void draw()                         // draw all points to standard draw 
	{
		root.rect.draw();
		drawhelper(root, 0);
	}
	
	private void drawhelper(Node root, int level)
	{
		if(root == null) return;
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		root.p.draw();
		
		if(level % 2 == 0)
		{
			StdDraw.setPenColor(StdDraw.RED);
			StdDraw.setPenRadius();
			double x = root.p.x();
			double y1 = root.rect.ymin();
			double y2 = root.rect.ymax();
			Point2D p1 = new Point2D(x,y1);
			Point2D p2 = new Point2D(x,y2);
			p1.drawTo(p2);
		} else {
			StdDraw.setPenColor(StdDraw.BLUE);
			StdDraw.setPenRadius();
			double x1 = root.rect.xmin();
			double x2 = root.rect.xmax();
			double y = root.p.y();
			Point2D p1 = new Point2D(x1,y);
			Point2D p2 = new Point2D(x2,y);
			p1.drawTo(p2);
		}
		drawhelper(root.lb, level+1);
		drawhelper(root.rt, level+1);
	}
	
	public Iterable<Point2D> range(RectHV rect)             // all points that are inside the rectangle 
	{
		if(rect == null) throw new java.lang.NullPointerException();
		Queue<Point2D> q = new Queue<Point2D>();
		
		rangehelper(root, q, rect);
		return q;
	}
	
	private void rangehelper(Node root, Queue<Point2D> q, RectHV rect)
	{
		if(root == null) return;
		if(!root.rect.intersects(rect)) return;
		if(rect.contains(root.p))
			q.enqueue(root.p);
		rangehelper(root.lb, q, rect);
		rangehelper(root.rt, q, rect);
	}
	
	public Point2D nearest(Point2D p)             // a nearest neighbor in the set to point p; null if the set is empty 
	{
		if(p == null) throw new java.lang.NullPointerException();
		if(isEmpty()) return null;
        Point2D retp = null;
        double mindis = Double.MAX_VALUE;
        Queue<Node> queue = new Queue<Node>();
        queue.enqueue(root);
        while (!queue.isEmpty()) {
            Node x = queue.dequeue();
            double dis = p.distanceSquaredTo(x.p);
            if (dis < mindis) {
                retp = x.p;
                mindis = dis; 
            }
            if (x.lb != null && x.lb.rect.distanceSquaredTo(p) < mindis) 
                queue.enqueue(x.lb);
            if (x.rt != null && x.rt.rect.distanceSquaredTo(p) < mindis) 
                queue.enqueue(x.rt);
        }
        return retp;
	}

	
	public static void main(String[] args)                  // unit testing of the methods (optional) 
	{
		Point2D p1 = new Point2D(0.7,0.2);
		Point2D p2 = new Point2D(0.5,0.4);
		Point2D p3 = new Point2D(0.2,0.3);
		Point2D p4 = new Point2D(0.4,0.7);
		Point2D p5 = new Point2D(0.9,0.6);
		Point2D p6 = new Point2D(0.0,0.0);
		Point2D p7 = new Point2D(0.2,0.6);
		   
		KdTree ps = new KdTree();
		ps.insert(p1);
		ps.insert(p2);
		ps.insert(p3);
		ps.insert(p4);
		ps.insert(p5);
		
		System.out.println(ps.contains(p1));
		System.out.println(ps.contains(p2));
		System.out.println(ps.contains(p3));
		System.out.println(ps.contains(p4));
		System.out.println(ps.contains(p5));
		System.out.println(ps.contains(p6));
		System.out.println(ps.contains(p7));
		
		
		ps.draw();
		
		RectHV rect = new RectHV(.3, .1, .8, .9);
		rect.draw();
		for(Point2D p : ps.range(rect))
		{
			System.out.println(p);
		}
		
		Point2D p = new Point2D(0.3, 0.3);
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(.01);
		p.draw();
		System.out.println(ps.nearest(p));
	}
}
