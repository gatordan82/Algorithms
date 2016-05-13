package kdtree;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;
import edu.princeton.cs.algs4.StdDraw;
import edu.princeton.cs.algs4.Queue;

public class KdTree 
{
	private Node root;
	private int N;
	private RectHV boundary;
	private Point2D closest;
	private double closestDistance;
	
	public KdTree()
	{
		N = 0;
		boundary = new RectHV(0, 0, 1, 1);
	}
	
	public boolean isEmpty()
	{
		return root == null;
	}
	
	public int size()
	{
		return N;
	}
	
	public void insert(Point2D p)
	{
		if (p == null) throw new NullPointerException();
		if (root == null) 
		{
			root = new Node(p, new RectHV(0, 0, 1, 1), null, null, true);
			N++;
		}
		else 
		{
			root.put(p);
		}
		
	}
	
	public boolean contains(Point2D p)
	{
		if (p == null) throw new NullPointerException();
		if (root == null) return false;
		return (root.get(p) != null);
	}
	
	private void drawPoint(Point2D p)
	{
		StdDraw.setPenColor(StdDraw.BLACK);
		StdDraw.setPenRadius(0.01);
		p.draw();
	}
	
	private void drawHorzLine(double xMin, double yMin, double xMax, double yMax)
	{
		StdDraw.setPenColor(StdDraw.BLUE);
		StdDraw.setPenRadius();
		StdDraw.line(xMin, yMin, xMax, yMax);
	}
	
	private void drawVertLine(double xMin, double yMin, double xMax, double yMax)
	{
		StdDraw.setPenColor(StdDraw.RED);
		StdDraw.setPenRadius();
		StdDraw.line(xMin, yMin, xMax, yMax);
	}
	
	private void drawPointLine(Node node)
	{
		drawPoint(node.p);
		if (node.isVertical)
			drawVertLine(node.p.x(), node.rect.ymin(), 
					     node.p.x(), node.rect.ymax());
		else 
			drawHorzLine(node.rect.xmin(), node.p.y(), 
					     node.rect.xmax(), node.p.y());
	}
	
	private void visitNode(Queue<Node> queue, Node node)
	{
		if (node == null) return;
		queue.enqueue(node);
		visitNode(queue, node.lb);
		visitNode(queue, node.rt);			
	}
	
	public void draw()
	{
		Queue<Node> queue = new Queue<Node>();
		visitNode(queue, root);
		for (Node node : queue) 
		{
			drawPointLine(node);
		}
	}
	
	public Iterable<Point2D> range(RectHV rangeRect)
	{
		if (rangeRect == null) throw new NullPointerException();
		Queue<Point2D> queue = new Queue<Point2D>();
		rangeSearchNodes(queue, root, rangeRect);
		return queue;
	}
	
	private void rangeSearchNodes(Queue<Point2D> queue, Node node, RectHV rangeRect)
	{
		if (node == null) return;
		if (rangeRect.contains(node.p)) queue.enqueue(node.p);
		if (node.lb != null && node.lb.rect.intersects(rangeRect))
		{
			rangeSearchNodes(queue, node.lb, rangeRect);
		}
			
		if (node.rt != null && node.rt.rect.intersects(rangeRect))
		{
			rangeSearchNodes(queue, node.rt, rangeRect);
		}
				
	}
	
	
	private void searchNodes(Point2D q, Node node)
	{
		if (node == null) return;
		double distance = q.distanceSquaredTo(node.p);
		StdDraw.setPenColor(StdDraw.GREEN);
		StdDraw.line(q.x(), q.y(), node.p.x(), node.p.y());
		if (closest == null || distance < closestDistance) 
		{
			closest = node.p;
			closestDistance = distance;
		}			
		
		if (node.isVertical)
		{
			double boxDistance = Math.pow(node.p.x() - q.x(), 2.0);
			if (q.x() < node.p.x())
			{
				if (closestDistance > boxDistance) 
				{
					searchNodes(q, node.lb);
				}
			}
			else 
			{
				if (closestDistance > boxDistance)
				{
					searchNodes(q, node.rt);
				}
			}
		}
		else 
		{
			double boxDistance = Math.pow(node.p.y() - q.y(), 2.0);
			if (q.y() < node.p.y())
			{
				if (closestDistance > boxDistance)
				{
					searchNodes(q, node.lb);
				}
			}
			else
			{
				if (closestDistance > boxDistance)
				{
					searchNodes(q, node.rt);
				}
			}
		}

	}
	
	public Point2D nearest(Point2D q)
	{
		if (q == null) throw new NullPointerException();
		
		searchNodes(q, root);
		return closest;	
	}
	
	
	private class Node
	{
		private Point2D p;   // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this node
		private Node lb;     // the left/bottom subtree
		private Node rt;     // the right/top subtree
		private boolean isVertical;
		
		private Node(Point2D point, RectHV rectangle, Node leftBottom, Node rightTop, boolean isVert)
		{
			p    = point;
			rect = rectangle;
			lb   = leftBottom;
			rt   = rightTop;
			isVertical = isVert;
		}
		
		private Node(Point2D point, Node leftBottom, Node rightTop)
		{
			p  = point;
			lb = leftBottom;
			rt = rightTop;		
		}
	
		public Node get(Point2D q)
		{
			return get(root, q, true);
		}
		
		private Node get(Node node, Point2D q, boolean vertical)
		{ 
			// Return value associated with key in the subtree rooted at x;
			// return null if key not present in subtree rooted at x.
			if (node == null) return null;
			
			double qKey;
			double nodeKey;
			if (vertical)
			{
				qKey = q.x();
				nodeKey = node.p.x();
			}
			else 
			{
				qKey = q.y();
				nodeKey = node.p.y();
			}
			vertical = !vertical;
			if      (qKey < nodeKey) return get(node.lb, q, vertical);
			else if (qKey > nodeKey) return get(node.rt, q, vertical);
			else return node;
		}
		
		public void put(Point2D q)
		{ 
			// Search for key. Update value if found; grow table if new.
			put(root, boundary, q, true);
		}
		
		private Node put(Node node, RectHV boundingBox, Point2D q, boolean vertical)
		{

			if (node == null) 
			{
				N++;
				return new Node(q, boundingBox, null, null, vertical);
			}
			double qKey;
			double nodeKey;
			RectHV nextBox;
			if (vertical)
			{
				qKey = q.x();
				nodeKey = node.p.x();
				
			}
			else 
			{
				qKey = q.y();
				nodeKey = node.p.y();
			}
						
			if (qKey < nodeKey)
			{
				if (vertical)
					 nextBox = new RectHV(boundingBox.xmin(), 
										  boundingBox.ymin(),
										  node.p.x(),
										  boundingBox.ymax());
				else nextBox = new RectHV(boundingBox.xmin(), 
										  boundingBox.ymin(),
										  boundingBox.xmax(),
										  node.p.y());
				vertical = !vertical;
				node.lb = put(node.lb, nextBox, q, vertical);
			}
			else if (qKey > nodeKey || (q.x() == node.p.x() && q.y() != node.p.y()))
			{
				if (vertical)
					 nextBox = new RectHV(node.p.x(), 
										  boundingBox.ymin(),
										  boundingBox.xmax(),
										  boundingBox.ymax());
				else nextBox = new RectHV(boundingBox.xmin(), 
										  node.p.y(),
										  boundingBox.xmax(),
										  boundingBox.ymax());
				vertical = !vertical;
				node.rt = put(node.rt, nextBox, q, vertical);
			}
			else 
			{
				
				node.p = q;
			}
			return node;
		}
	}
	
    public static void main(String[] args) {
        String filename = args[0];
        In in = new In(filename);

        KdTree kdtree = new KdTree();
        while (!in.isEmpty()) 
        {
            double x = in.readDouble();
            double y = in.readDouble();
            Point2D p = new Point2D(x, y);
            kdtree.insert(p);
        }
        kdtree.draw();
        Point2D nearPt = kdtree.nearest(new Point2D(0.6, 0.8));
        System.out.println(nearPt);
//        while (true) kdtree.draw();
    }
	
//	public static void main(String[] args) 
//	{
//		KdTree kdt = new KdTree();
//		Point2D q1 = new Point2D(0.5, 0.5);
//		Point2D q2 = new Point2D(0.25, 0.25);
//		Point2D q3 = new Point2D(0.75, 0.75);
//		Point2D q4 = new Point2D(0.35, 0.35);
//		kdt.insert(q1);
//		kdt.insert(q2);
//		kdt.insert(q3);
//		kdt.insert(q4);
//		kdt.draw();
//		System.out.println("KdTree contains point (0.5, 0.5) " + kdt.contains(q1));
//		System.out.println("KdTree contains point (0.25, 0.25) " + kdt.contains(q2));
//		System.out.println("KdTree contains point (0.35, 0.35) " + kdt.contains(q4));
//	}

}
