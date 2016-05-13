package collinear;

import java.util.ArrayList;
import java.util.Arrays;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.StdOut;

public class FastCollinearPoints 
{
    private int n;
    private ArrayList<LineSegment> al = new ArrayList<LineSegment>();
    private final int minLength = 4;
    
    public FastCollinearPoints(Point[] points)
    {
        if (points == null) throw new NullPointerException();
        
        int N = points.length;
        
        Point[] pointsToSort = new Point[N];
        for (int i = 0; i < N; i++)
        {
            if (points[i] == null) throw new NullPointerException();
            pointsToSort[i] = points[i];
        }

        Arrays.sort(pointsToSort);
        
        for (int i = 0; i < N; i++)
        {
        	Point p = points[i];

        	Arrays.sort(pointsToSort);
        	Arrays.sort(pointsToSort, p.slopeOrder());
            
            
       	
        	double[] slopes = new double[N];
        	for (int j = 0; j < N; j++)
        	{
        	    if (j != i && p.compareTo(points[j]) == 0) throw new IllegalArgumentException();
        	    slopes[j] = p.slopeTo(pointsToSort[j]);   
        	}
        	
        	int count = 0;
        	int startIndex = 0;
        	for (int j = 2; j < N; j++)
        	{
        	    double prevSlope = slopes[j - 1];
        	    double slope     = slopes[j];
        	    
        	    if (slope == prevSlope) 
        	    {
        	        if (count == 0)
        	        {
        	            startIndex = j - 1;
        	            count++;
        	        }
        	        count++;
        	    }

        	    if (slope != prevSlope)
        	    {
        	        if (count >= minLength - 1 && p.compareTo(pointsToSort[startIndex]) < 0)
        	        {
        	            al.add(new LineSegment(p, pointsToSort[j - 1]));
        	            n++;
        	            startIndex = 0;
        	            count = 0;
        	            continue;
        	        }
        	        count = 0;
        	    }

        	    if (j == N - 1 && count >= minLength - 1 && p.compareTo(pointsToSort[startIndex]) < 0)
        	    {
                    al.add(new LineSegment(p, pointsToSort[N - 1]));
                    n++;
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
		FastCollinearPoints collinear = new FastCollinearPoints(points);
		
		for (LineSegment segment : collinear.segments()) 
		{
			StdOut.println(segment);
			segment.draw();
		}

	}

}
