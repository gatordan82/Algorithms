package percolation;

import edu.princeton.cs.algs4.WeightedQuickUnionUF;

/**
 * @author Dan
 *
 */

public class Percolation 
{
	private boolean[][] grid;
	private boolean[] connectedToBottom;
	private int size;
	private WeightedQuickUnionUF uf;
	private int virtualTop;
	
	public Percolation(int N)
	{
		if (N <= 0) throw new IllegalArgumentException("Size of Percolation grid isn't > 0");
		
		grid     = new boolean[N][N];
		connectedToBottom = new boolean[N * N + 1];
		size     = N;
		uf       = new WeightedQuickUnionUF(N * N + 1);
		virtualTop = N * N;
	}
	
    private void validateCoords(int i, int j)
    {
        if (i < 1 || i > size)
        {
            throw new IndexOutOfBoundsException("Index i is not between 1 and " + size);
        }
        if (j < 1 || j > size)
        {
            throw new IndexOutOfBoundsException("Index j is not between 1 and " + size);
        }
    }
    
    private int coordsToUF(int i, int j)
    {
        return (i - 1) * size + (j - 1);
    }
	
	public void open(int i, int j)
	{
	    validateCoords(i, j);
	    
		//open the site
		if (!isOpen(i, j)) 
		{
		    grid[i - 1][j - 1] = true;
		    
		    //connect any open, adjacent neighbors
	        int p = coordsToUF(i, j);
	        
            if (i == 1)
            {
                uf.union(p, virtualTop);
            }
	        
	        if (i == size) 
            {
	            connectedToBottom[uf.find(p)] = true;
            }
	               
	        //neighbor to the left
	        if (j > 1) unionIfOpen(i, j - 1, p);
	        //neighbor to the right
	        if (j < size) unionIfOpen(i, j + 1, p);
	        //neighbor above
	        if (i > 1) unionIfOpen(i - 1, j, p);
	        //neighbor below
            if (i < size) unionIfOpen(i + 1, j, p);
		}
	}
	
    private void unionIfOpen(int i, int j, int p)
    {
        int q = coordsToUF(i, j);
        
        if (isOpen(i, j) && !uf.connected(p, q))
        {
            int rootQ = uf.find(q);
            int rootP = uf.find(p);
            if (connectedToBottom[rootQ] || connectedToBottom[rootP])
            {
                connectedToBottom[rootP] = true;
                connectedToBottom[rootQ] = true;
            }
            uf.union(p, q);
        }       
    }
	
	public boolean isOpen(int i, int j)
	{
	    validateCoords(i, j);
	    
		return grid[i - 1][j - 1];
	}
	
	public boolean isFull(int i, int j)
	{
	    validateCoords(i, j);
	    int p = coordsToUF(i, j);

		return uf.connected(p, virtualTop);
	}
	
	public boolean percolates()
	{
	    int root = uf.find(virtualTop);
	    return connectedToBottom[root];
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) 
	{

	}

}
