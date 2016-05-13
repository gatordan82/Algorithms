package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.ResizingArrayStack;
import edu.princeton.cs.algs4.StdDraw;

import java.util.Iterator;
import java.util.TreeSet;

public class PointSET 
{
	private TreeSet<Point2D> tree;

	public PointSET()
	{
		tree = new TreeSet<Point2D>();
	}
	
	public boolean isEmpty()
	{
		return tree.size() == 0;
	}
	
	public int size()
	{
		return tree.size();
	}
	
	public void insert(Point2D p)
	{
		if (p == null) throw new NullPointerException();
		if (!tree.contains(p)) tree.add(p);
	}
	
	public boolean contains(Point2D p)
	{
		if (p == null) throw new NullPointerException();
		return tree.contains(p);
	}
	
	public void draw()
	{
		for (Point2D p : tree) p.draw();
	}
	
	public Iterable<Point2D> range(RectHV rect)
	{
		if (rect == null) throw new NullPointerException();
		return new RangeIterator(rect);
	}
	
	private class RangeIterator implements Iterable<Point2D>
	{
		private ResizingArrayStack<Point2D> stack;
		private RectHV r;
		
		private RangeIterator(RectHV rect)
		{
			stack = new ResizingArrayStack<Point2D>();
			r = rect;
			
			for (Point2D p : tree)
				if (r.contains(p)) stack.push(p);
		}
			
		public Iterator<Point2D> iterator()
		{
			return stack.iterator();
		}
	}
	
	public Point2D nearest(Point2D p)
	{
		if (p == null) throw new NullPointerException();
		
		if (tree.size() == 0) return null;
		Point2D nearest = null;
		double nearestDistance = Double.POSITIVE_INFINITY;
		
		for (Point2D q : tree)
		{
			double distance = p.distanceTo(q);
			if (nearest == null || distance < nearestDistance) 
			{
				nearest = q;
				nearestDistance = distance;
			}
		}
		
		return nearest;
	}
		
	public static void main(String[] args) 
	{
		PointSET brute = new PointSET();
		brute.insert(new Point2D(0.1, 0.1));
		brute.insert(new Point2D(0.2, 0.2));
		brute.insert(new Point2D(0.3, 0.3));
		brute.insert(new Point2D(0.4, 0.4));
		RectHV rr = new RectHV(0.15, 0.15, 0.5, 0.5);
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius();
        rr.draw();

        // draw the range search results for brute-force data structure in red
        StdDraw.setPenRadius(.03);
        StdDraw.setPenColor(StdDraw.RED);
        for (Point2D p : brute.range(rr))
            p.draw();
        StdDraw.clear();
        StdDraw.setPenColor(StdDraw.BLACK);
        StdDraw.setPenRadius(0.01);
        brute.draw();

	}
	
}
