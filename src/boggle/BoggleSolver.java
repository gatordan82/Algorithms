package boggle;

import java.util.HashSet;

import boggle.TrieSTBoggle.Node;
import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.SeparateChainingHashST;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.TrieSET;
import edu.princeton.cs.algs4.TrieST;

public class BoggleSolver 
{
	private static final int asciiShift = 65;
	private static final int minWordLength = 3;
	
	private TrieSTBoggle<String> dict;
	private Bag<Integer>[] adj;
	private int M;
	private int N;
	private SeparateChainingHashST<Integer, Character> letters;
	private HashSet<String> validWords;
	private StringBuilder recursiveWord;
	
	
	/**
	 * 
	 * @param dictionary
	 */
	public BoggleSolver(String[] dictionary)
	{
		dict = new TrieSTBoggle<String>();
		for (int i = 0; i < dictionary.length; i++)
			dict.put(dictionary[i], dictionary[i]);
	}
	
	/**
	 * 
	 * @param board
	 * @return queue of words discovered on the board 
	 */
	public Iterable<String> getAllValidWords(BoggleBoard board)
	{
		validWords = new HashSet<String>();
		
		M = board.rows();
		N = board.cols();
		mapLetters(board, M, N);
		buildGraph(M, N);
		for (int i = 0; i < M * N; i++)
		{
			char currentLetter = letters.get(i);
			Node current = dict.root().next[currentLetter - asciiShift];
			dfs(i, current);
		}
		
		return validWords;		
	}
	
	private void mapLetters(BoggleBoard board, int rows, int cols)
	{
		letters = new SeparateChainingHashST<Integer, Character>();
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++)
			{
				int x = i * rows + j;
				letters.put(x, board.getLetter(i, j));
			}
	}
	
	private void buildGraph(int rows, int cols)
	{
		if (adj != null && rows == M && rows == N) return;
		
		M = rows;
		N = cols;
		adj = (Bag<Integer>[]) new Bag[M * N];

		for (int x = 0; x < M * N; x++)
		{
			adj[x] = new Bag<Integer>();
			// left
			if (x % N != 0) 
			{
				adj[x].add(x - 1);
				// top-left
				if (x > N - 1)       adj[x].add(x - N - 1);
				// bottom-left
				if (x < (M - 1) * N) adj[x].add(x + N - 1);
			}
			
			// right
			if (x % N != N - 1)
			{
				adj[x].add(x + 1);
				// top-right
				if (x > N - 1)       adj[x].add(x - N + 1);
				// bottom-right
				if (x < (M - 1) * N) adj[x].add(x + N + 1);
			}
			// top
			if (x > N - 1)       adj[x].add(x - N);
			// bottom
			if (x < (M - 1) * N) adj[x].add(x + N);
		}
	}
	
	private void dfs(int x, Node current)
	{
		boolean[] marked = new boolean[M * N];
		doDFS(marked, x, current, 1);
	}
	
	private void doDFS(boolean[] marked, int x, Node current, int depth)
	{
		marked[x] = true;

		String currentWord = (String) current.val;
		if (current.val != null  
			&& currentWord.length() >= minWordLength 
			&& dict.contains(currentWord)
			&& !validWords.contains(current.val))
		{
				validWords.add((String) current.val);
		}
		
		for (Integer i : adj[x])
		{	
			char nextLetter = letters.get(i);
			if (!marked[i] && current.next[nextLetter - asciiShift] != null)
			{
					current = current.next[nextLetter - asciiShift];
					doDFS(marked, i, current, ++depth);
			}
		}
			
	}
	
	
	/**
	 * 
	 * @param word
	 * @return score of word, based on length
	 */
	public int scoreOf(String word)
	{
		if (!dict.contains(word)) return 0;

		switch (word.length())
		{
			case 0: case 1: case 2:
				return 0;
			case 3: case 4:
				return 1;
			case 5:
				return 2;
			case 6:
				return 3;
			case 7:
				return 5;
			default:
				return 11;		
		}
	}
	
	/**
	 * 
	 * @param args
	 */
	public static void main(String[] args) 
	{
		In in = new In(args[0]);
		String[] dictionary = in.readAllStrings();
		long startTime = System.currentTimeMillis();
		BoggleSolver solver = new BoggleSolver(dictionary);

		BoggleBoard board   = new BoggleBoard(args[1]);
		int score = 0;
		for (String word : solver.getAllValidWords(board))
		{
			StdOut.println(word);
			score += solver.scoreOf(word);
		}
		StdOut.println("Score = " + score);
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("Time to execute: " + totalTime);
	}

}
