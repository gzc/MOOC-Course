/*************************************************************************
 * Name:
 * Email:
 *
 * Compilation:  javac Point.java
 * Execution:
 * Dependencies: StdDraw.java
 *
 * Description: An immutable data type for points in the plane.
 *
 *************************************************************************/

import java.util.Comparator;

public class Point implements Comparable<Point> {

    // compare points by slope
    public final Comparator<Point> SLOPE_ORDER = new SlopeOrder();       // YOUR DEFINITION HERE

    private final int x;                              // x coordinate
    private final int y;                              // y coordinate

    // create the point (x, y)
    public Point(int x, int y) {
        /* DO NOT MODIFY */
        this.x = x;
        this.y = y;
    }

    // plot this point to standard drawing
    public void draw() {
        /* DO NOT MODIFY */
        StdDraw.point(x, y);
    }

    // draw line between this point and that point to standard drawing
    public void drawTo(Point that) {
        /* DO NOT MODIFY */
        StdDraw.line(this.x, this.y, that.x, that.y);
    }

    // slope between this point and that point
    public double slopeTo(Point that) {
        double res;
        if(this.x == that.x && this.y == that.y) res = Double.NEGATIVE_INFINITY;
        else if(this.x == that.x) res = Double.POSITIVE_INFINITY;
        else if(this.y == that.y) res = +0.0;
        else res = (this.y - that.y)*1.0/(this.x - that.x);
        return res;
    }

    // is this point lexicographically smaller than that one?
    // comparing y-coordinates and breaking ties by x-coordinates
    public int compareTo(Point that) {
        if(this.y < that.y) return -1;
        if(this.y == that.y && this.x < that.x) return -1;
        if(this.y == that.y && this.x == that.x) return 0;
        return 1;
    }

    // return string representation of this point
    public String toString() {
        /* DO NOT MODIFY */
        return "(" + x + ", " + y + ")";
    }
    
    private class SlopeOrder implements Comparator<Point>
    {
    	public int compare(Point q1, Point q2)
    	{
    		double f1 = slopeTo(q1);
    		double f2 = slopeTo(q2);
    		if(f1 < f2) return -1;
    		else if(f1 == f2) return 0;
    		return 1;
    	}
    }

    // unit test
    public static void main(String[] args) {
        /* YOUR CODE HERE */
    }
}