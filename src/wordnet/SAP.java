

//import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.BreadthFirstDirectedPaths;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;

public class SAP
{
	private final int infinity = Integer.MAX_VALUE;
	private Digraph dg;
//	private int[] lengthV;
//	private int[] lengthW;
//	private Bag<Integer> valsUpdated;
	
	public SAP(Digraph G)
	{
		if (G == null) throw new NullPointerException();
		
		dg = new Digraph(G);
//		for (int i = 0; i < V; i++)
//		{
//			lengthV[i] = -1;
//			lengthW[i] = -1;
//		}
	}
	
	public int length(int v, int w)
	{
//		if (v == null || w == null) throw new NullPointerException();
//		for (int i : valsUpdated)
//		{
//			lengthV[i] = -1;
//			lengthW[i] = -1;
//		}
		int len = infinity;
		
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dg, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dg, w);
		
		for (int vertex = 0; vertex < dg.V(); vertex++)
		{
			if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex))
				if (bfsV.distTo(vertex) + bfsW.distTo(vertex) < len)
					len = bfsV.distTo(vertex) + bfsW.distTo(vertex);
		}
		if (len == infinity) return -1;
		else return len;
	}
	
	public int ancestor(int v, int w)
	{
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dg, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dg, w);
		
		int len = infinity;
		int ancestorVertex = -1;
		
		for (int vertex = 0; vertex < dg.V(); vertex++)
			if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) 
				if (bfsV.distTo(vertex) + bfsW.distTo(vertex) < len)
				{
					ancestorVertex = vertex;
					len = bfsV.distTo(vertex) + bfsW.distTo(vertex);
				}
		
		return ancestorVertex;
	}
	
	public int length(Iterable<Integer> v, Iterable<Integer> w)
	{
		int len = infinity;
		
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dg, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dg, w);
		
		for (int vertex = 0; vertex < dg.V(); vertex++)
		{
			if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex))
				if (bfsV.distTo(vertex) + bfsW.distTo(vertex) < len)
					len = bfsV.distTo(vertex) + bfsW.distTo(vertex);
		}
		if (len == infinity) return -1;
		else return len;
	}
	
	public int ancestor(Iterable<Integer> v, Iterable<Integer> w)
	{
		BreadthFirstDirectedPaths bfsV = new BreadthFirstDirectedPaths(dg, v);
		BreadthFirstDirectedPaths bfsW = new BreadthFirstDirectedPaths(dg, w);
		
		int len = infinity;
		int ancestorVertex = -1;
		
		for (int vertex = 0; vertex < dg.V(); vertex++)
			if (bfsV.hasPathTo(vertex) && bfsW.hasPathTo(vertex)) 
				if (bfsV.distTo(vertex) + bfsW.distTo(vertex) < len)
				{
					ancestorVertex = vertex;
					len = bfsV.distTo(vertex) + bfsW.distTo(vertex);
				}
		
		return ancestorVertex;	
	}
	
	
	public static void main(String[] args)
	{
			In in = new In(args[0]);
			Digraph G = new Digraph(in);
			SAP sap = new SAP(G);
//			
//			Stack<Integer> v = new Stack<Integer>();
//			Stack<Integer> w = new Stack<Integer>();
//			v.push(3);
//			v.push(5);
//			w.push(9);
//			int length = sap.length(v, w);
//			int ancestor = sap.ancestor(v, w);
//			StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
//			
			while (!StdIn.isEmpty()) 
			{
				int v = StdIn.readInt();
				int w = StdIn.readInt();
				int length = sap.length(v, w);
				int ancestor = sap.ancestor(v, w);
				StdOut.printf("length = %d, ancestor = %d\n", length, ancestor);
			}
	}

}
