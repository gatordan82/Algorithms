package kdtree;

import edu.princeton.cs.algs4.Point2D;
import edu.princeton.cs.algs4.RectHV;

public class KdTree 
{
	private static Node root;
	private int N;
	
	public KdTree()
	{
		root = null;
		N = 0;
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
		if (!contains(p)) 
		{
			root.put(p);
			N++;
		}
	}
	
	public boolean contains(Point2D p)
	{
		return (root.get(p) != null);
	}
//	
//	public void draw()
//	{
//		
//	}
//	
//	public Iterable<Point2D> range(RectHV rect)
//	{
//		
//	}
//	
//	public Point2D nearest(Point2D p)
//	{
//		
//	}
	private static class Node
	{
		private Point2D p;   // the point
		private RectHV rect; // the axis-aligned rectangle corresponding to this node
		private Node lb;     // the left/bottom subtree
		private Node rt;     // the right/top subtree
		
		private Node(Point2D point, RectHV rectangle, Node leftBottom, Node rightTop)
		{
			p    = point;
			rect = rectangle;
			lb   = leftBottom;
			rt   = rightTop;
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
			if (qKey < nodeKey) return get(node.lb, q, vertical);
			else if (qKey > nodeKey) return get(node.rt, q, vertical);
			else return node;
		}
		
		public void put(Point2D q)
		{ 
			// Search for key. Update value if found; grow table if new.
			root = put(root, q, true);
		}
		
		private Node put(Node node, Point2D q, boolean vertical)
		{
			// Change key’s value to val if key in subtree rooted at x.
			// Otherwise, add new node to subtree associating key with val.
			if (node == null) return new Node(q, null, null);
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
			
//			if (qKey < nodeKey) return get(node.lb, q, vertical);
//			else if (qKey > nodeKey) return get(node.rt, q, vertical);
			
			if      (qKey < nodeKey) node.lb = put(node.lb, q, vertical);
			else if (qKey > nodeKey) node.rt = put(node.rt, q, vertical);
			else 
			{
				node.p = q;
			}
			return node;
		}
	}
		
	public static void main(String[] args) 
	{
		// TODO Auto-generated method stub

	}

}
