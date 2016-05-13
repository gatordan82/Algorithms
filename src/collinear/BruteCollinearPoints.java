package collinear;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdDraw;

public class BruteCollinearPoints 
{
	private int n;
	private ArrayList<LineSegment> al = new ArrayList<LineSegment>();
	
	public BruteCollinearPoints(Point[] points)
	{
	    if (points == null) throw new NullPointerException();
	    
	    int N = points.length;
	    Point[] pts = new Point[N];
	    for (int i = 0; i < N; i++)
	    {
	        pts[i] = points[i];
	    }
	    
		Arrays.sort(pts);
		
		long cnt = 0;
		
		for (int i = 0; i < N; i++)
		{			
			for (int j = i + 1; j < N; j++)
			{
				Point a = pts[i];
				Point b = pts[j];
				if (a == null || b == null) throw new NullPointerException();
				if (a.compareTo(b) == 0) throw new IllegalArgumentException();
				
				double slopeAB = a.slopeTo(b);
				
				for (int k = j + 1; k < N; k++)
				{
					Point c = pts[k];
					if (c == null) throw new NullPointerException();
					if (a.compareTo(c) == 0 || 
						b.compareTo(c) == 0) 
						throw new IllegalArgumentException();
					
					double slopeAC = a.slopeTo(c);
					if (slopeAB != slopeAC) continue;
					
					for (int l = k + 1; l < N; l++)
					{					    
						++cnt;
						if (cnt % 1000000 == 0) System.out.println("Reached inner loop " + cnt + " times.");	
						
						Point d = pts[l];
						if (d == null) throw new NullPointerException();
						if (a.compareTo(d) == 0 || 
							b.compareTo(d) == 0 || 
							c.compareTo(d) == 0) 
							throw new IllegalArgumentException();
						
						double slopeAD = a.slopeTo(d);
						if (slopeAB == slopeAD)
						{
							al.add(new LineSegment(a, d));
							n++;
						}
						else continue;
					}
				}
			}
		}	
	}
	
	public int numberOfSegments()
	{
		return n;
	}
	
	public LineSegment[] segments()
	{
		if (al != null)
		{
			LineSegment[] lsa = new LineSegment[n];
			return al.toArray(lsa);
		}
		else return null;
	}
	
	
	public static void main(String[] args) 
	{
		// read the N points from a file
		System.out.println(args[0]);
		In in = new In(args[0]);
		int N = in.readInt();
		Point[] points = new Point[N];
		
		for (int i = 0; i < N; i++) 
		{
			int x = in.readInt();
			int y = in.readInt();
			points[i] = new Point(x, y);
		}
		
		// draw the points
		StdDraw.show(0);
		StdDraw.setXscale(0, 32768);
		StdDraw.setYscale(0, 32768);
		
		for (Point p : points) 
		{
			p.draw();
		}
		StdDraw.show();
		
		// print and draw the line segments
		BruteCollinearPoints collinear = new BruteCollinearPoints(points);
		
		for (LineSegment segment : collinear.segments()) 
		{
			StdOut.println(segment);
			segment.draw();
		}
	}

}
