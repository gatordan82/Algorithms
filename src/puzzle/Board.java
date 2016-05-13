package puzzle;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.ResizingArrayStack;
import java.util.Iterator;

public class Board
{
    private int N;
    private int emptyI;
    private int emptyJ;
    
    // Current board layout
    private final int[][] tiles;
    
    public Board(int[][] blocks)
    {
        if (blocks == null) throw new NullPointerException();
        
        N = blocks.length;
        
        tiles = new int[N][N];
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
            {
                int toAdd = blocks[i][j];
                tiles[i][j] = toAdd;
                if (toAdd == 0)
                {
                    emptyI = i;
                    emptyJ = j;
                }
            }
    }
    
    public int dimension()
    {
        return N;
    }
    
    private int goalVal(int i, int j)
    {
        if (i != N - 1 || j != N - 1) return (j + 1) + i * N;
        else return 0;
    }
    
    public int hamming()
    {
        int count = 0;
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != goalVal(i, j) && tiles[i][j] != 0) count++;
        
        return count;
    }
    
    public int manhattan()
    {
        int count = 0;
        
        for (int i = 0; i < N; i++)
        {
            for (int j = 0; j < N; j++)
            {
                int y = tiles[i][j];
                if (y != 0)
                {
                    int jp = (y - 1) % N;
                    int ip = (y - jp - 1) / N;
                    
                    count += Math.abs(i - ip) + Math.abs(j - jp);
                }
            }
        }
        
        return count;
    }
    
    public boolean isGoal()
    {
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (tiles[i][j] != goalVal(i, j)) return false;
        
        return true;
    }
    
    public Board twin()
    {
    	int swapI;
        if (emptyI == N - 1) swapI = emptyI - 1;
        else swapI = emptyI + 1;
        
        int[][] twinTiles = new int[N][N];
        for (int i = 0; i < N; i++)
    		for (int j = 0; j < N; j++)
    			twinTiles[i][j] = tiles[i][j];
        
        int temp = twinTiles[swapI][0];
        twinTiles[swapI][0] = twinTiles[swapI][1];
        twinTiles[swapI][1] = temp;
        
        return new Board(twinTiles);
    }
    
    public boolean equals(Object y)
    {
        if (y == this) return true;
        if (y == null) return false;
        if (y.getClass() != this.getClass()) return false;
        
        Board that = (Board) y;
        
        if (this.N != that.N) return false;

        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                if (this.tiles[i][j] != that.tiles[i][j]) return false;
        
        return true;
    }
    
    public Iterable<Board> neighbors()
    {
        return new BoardIterator();
    }
    
    private class BoardIterator implements Iterable<Board>
    {
        private ResizingArrayStack<Board> stack;
    	private int[][] currentTiles;
    	private final int[][] swaps = {{0, 1}, {1, 0}, {0, -1}, {-1, 0}};
        
        private BoardIterator()
        {
        	stack = new ResizingArrayStack<Board>();
        	currentTiles = new int[N][N];
        	for (int i = 0; i < N; i++)
        		for (int j = 0; j < N; j++)
        			currentTiles[i][j] = tiles[i][j];
        }
        
        public Iterator<Board> iterator() 
        {
        	for (int i = 0; i < swaps.length; i++)
        	{
	            int dI = swaps[i][0];
	            int dJ = swaps[i][1];
	            
	            int iSwap = emptyI + dI;
	            int jSwap = emptyJ + dJ;
	            
	            if (iSwap >= 0 && iSwap < N && jSwap >= 0 && jSwap < N) 
	            {
	            	int swapVal = currentTiles[iSwap][jSwap];
	            	currentTiles[emptyI][emptyJ] = swapVal;
	            	currentTiles[iSwap][jSwap]   = 0;
	            	
	            	stack.push(new Board(currentTiles));
	            	
	            	currentTiles[emptyI][emptyJ] = 0;
	            	currentTiles[iSwap][jSwap]   = swapVal;
	            }
        	}
        	
        	return stack.iterator();
        }
    }
    
    public String toString()
    {
        StringBuilder s = new StringBuilder();
        s.append(N + "\n");
        for (int i = 0; i < N; i++) 
        {
            for (int j = 0; j < N; j++) 
            {
                s.append(String.format("%2d ", tiles[i][j]));
            }
            s.append("\n");
        }
        
        return s.toString();
    }

    public static void main(String[] args)
    {
        // create initial board from file
        In in = new In(args[0]);
        int N = in.readInt();
        final int[][] blocks = new int[N][N];
        
        for (int i = 0; i < N; i++)
            for (int j = 0; j < N; j++)
                blocks[i][j] = in.readInt();
        
        Board initial = new Board(blocks); 
        
        System.out.println("Initial Board");
        System.out.println(initial);
        
        System.out.println("Is goal board? " + initial.isGoal());
        
        for (Board board : initial.neighbors())
        {
        	
        	System.out.println("Neighbor board");
        	System.out.println(board.toString());
        	System.out.println("Twin board");
        	System.out.println(board.twin().toString());
        	System.out.println(board.isGoal());
        }
        
//        Board checkSame = new Board(blocks);
//        
//        int[][] checkArray = blocks.clone();
//        checkArray[0][0] = 1;
//        checkArray[0][1] = 8;
//        Board checkDiff = new Board(checkArray);
//        
//        System.out.println(initial.equals(checkSame));
//        System.out.println(initial.equals(checkDiff));
//        
//        System.out.println("Hamming distance is: " + initial.hamming() + "\n");
//        System.out.println("Manhattan distance is: " + initial.manhattan() + "\n");
        
        

    }

}
