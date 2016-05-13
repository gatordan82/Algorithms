package puzzle;

import java.util.Iterator;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.MinPQ;
import edu.princeton.cs.algs4.ResizingArrayStack;

public class Solver
{
    private boolean solvable;
	private MinPQ<SearchNode> nodePQ;
    private SearchNode current;
    private int movesToSolve;
    private Board initialBoard;
    private ResizingArrayStack<Board> solutionStack;
    
//    private int delMinCounter;
//    private int insertCounter;
        
    public Solver(Board initial)
    {
    	initialBoard = initial;
    	nodePQ = new MinPQ<SearchNode>();
    	
    	current = new SearchNode(0, initial, null);
    	nodePQ.insert(current);
    	
    	SearchNode initialTwin = new SearchNode(0, initial.twin(), null);
    	nodePQ.insert(initialTwin);
        
//    	delMinCounter = 0;
//    	insertCounter = 0;
    	
        while (!current.board.isGoal())
        {
        	enqueueNeighbors(current);
//        	System.out.println("Size of PQ is " + nodePQ.size());

//        	int moves = current.moves;
//        	int manhattan = current.board.manhattan();
//        	int priority = moves + manhattan;
//    		System.out.println("Original board child");
//    		System.out.println("priority  = " + priority);
//    		System.out.println("moves     = " + moves);
//        	System.out.println("manhattan = " + manhattan);
//        	System.out.println("Deqeuing board\n" + current.board);


        	current = nodePQ.delMin();
//        	delMinCounter++;
        }
        
//        System.out.println("Number of delMin(): " + delMinCounter);
//        System.out.println("Number of inserts: " + insertCounter);
        
        movesToSolve = current.moves;
        
		solutionStack = new ResizingArrayStack<Board>();
		
		while (current.previous != null)
		{
			solutionStack.push(current.board);
			current = current.previous;
		}
		solutionStack.push(current.board);
		
		solvable = solutionStack.peek().equals(initialBoard);
        
    }
    
    private void enqueueNeighbors(SearchNode sn)
    {
    	
    	for (Board b : sn.board.neighbors())
    	{
    		if (current.previous != null 
    			&& b.equals(current.previous.board)) continue;
//       		insertCounter++;
    		nodePQ.insert(new SearchNode(sn.moves + 1, b, sn));
    	}
    }
    
    public boolean isSolvable()
    {
        return solvable;
    }
    
    public int moves()
    {
        if (solvable) return movesToSolve;
        else return -1;
    }
    
    public Iterable<Board> solution()
    {
        if (solvable) return new SolutionIterator();
        else return null;
    }
    
    private class SolutionIterator implements Iterable<Board>
    {
    	public Iterator<Board> iterator()
    	{
    		return solutionStack.iterator();
    	}
    }
    
    private class SearchNode implements Comparable<SearchNode>
    {
    	private int moves;
    	private Board board;
    	private SearchNode previous;
    	
    	private SearchNode(int numMoves, 
    					   Board blocks, 
    					   SearchNode prev)
		{
			moves    = numMoves;
			board    = blocks;
			previous = prev;
		}
    	
    	   	  	
    	public int compareTo(SearchNode that)
    	{
    		int thisMan = this.board.manhattan();
    		int thatMan = that.board.manhattan();
    		
    		int thisPriority = this.moves + thisMan;
    		int thatPriority = that.moves + thatMan;
    		
    		if (thisPriority < thatPriority) return -1;
    		else if (thisPriority > thatPriority) return 1;
    		else return Integer.signum(thisMan - thatMan);
    	}
    	

    }
    
    public static void main(String[] args)
    {
    	// create initial board from file
    	In in = new In(args[0]);
    	int N = in.readInt();
    	int[][] blocks = new int[N][N];
    	for (int i = 0; i < N; i++)
    		for (int j = 0; j < N; j++)
    			blocks[i][j] = in.readInt();
    	Board initial = new Board(blocks);
    	
    	// solve the puzzle
    	Solver solver = new Solver(initial);
    	
    	// print solution to standard output
    	if (!solver.isSolvable())
    		StdOut.println("No solution possible");
    	else 
    	{
	    	StdOut.println("Minimum number of moves = " + solver.moves());
	    	for (Board board : solver.solution())
	    		StdOut.println(board);
    	}
    }

}
