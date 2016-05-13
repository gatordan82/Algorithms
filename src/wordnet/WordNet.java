package wordnet;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Topological;

public class WordNet
{
	private Digraph dg;
	private SAP sap;
	private LinearProbingHashST<String, Bag<Integer>> nounsToSynsets;
	private LinearProbingHashST<Integer, String> idToSynsets;
	
	public WordNet(String synsets, String hypernyms)
	{
		if (synsets == null || hypernyms == null)
			throw new NullPointerException();
		
		readSynsets(synsets);
		readHypernyms(hypernyms);
		
		// Check if the digraph has a cycle
		Topological topo = new Topological(dg);
		if (!topo.hasOrder()) throw new IllegalArgumentException();

		// Check if there is exactly 1 root
		int vOut = 0;
		for (int v = 0; v < dg.V(); v++)
			if (dg.outdegree(v) == 0) vOut++;

		if (vOut != 1) throw new IllegalArgumentException();
		
		sap = new SAP(dg);
	}
	
	private void readSynsets(String synsets)
	{
		nounsToSynsets = new LinearProbingHashST<String, Bag<Integer>>();
		idToSynsets    = new LinearProbingHashST<Integer, String>();
		
		In in = new In(synsets);
		while (!in.isEmpty())
		{
			String   line   = in.readLine();
			String[] fields = line.split(",");
			int id = Integer.parseInt(fields[0]);
			idToSynsets.put(id, fields[1]);
			
			String[] nouns = fields[1].split(" ");
			for (int i = 0; i < nouns.length; i++)
			{
				if (nounsToSynsets.get(nouns[i]) == null)
				{
					nounsToSynsets.put(nouns[i], new Bag<Integer>());
					nounsToSynsets.get(nouns[i]).add(id);
				}
				else
					nounsToSynsets.get(nouns[i]).add(id);					
			}
		}
	}
	
	private void readHypernyms(String hypernyms)
	{
		dg = new Digraph(idToSynsets.size());
		In in = new In(hypernyms);
		while (!in.isEmpty())
		{
			String line = in.readLine();
			String[] fields = line.split(",");
			int id = Integer.parseInt(fields[0]);
			for (int i = 1; i < fields.length; i++)
				dg.addEdge(id, Integer.parseInt(fields[i]));
		}
	}
	
	public Iterable<String> nouns()
	{
		return nounsToSynsets.keys();
	}
	
	public boolean isNoun(String word)
	{
		if (word == null) throw new NullPointerException();
		
		return nounsToSynsets.contains(word);
	}
	
	public int distance(String nounA, String nounB)
	{
		if (nounA == null || nounB == null)
			throw new NullPointerException();
		
		Bag<Integer> a = nounsToSynsets.get(nounA);
		Bag<Integer> b = nounsToSynsets.get(nounB);
		
		if (a == null || b == null) throw new IllegalArgumentException();

		return sap.length(a, b);
	}
	
	public String sap(String nounA, String nounB)
	{
		if (nounA == null || nounB == null)
			throw new NullPointerException();
		
		Bag<Integer> a = nounsToSynsets.get(nounA);
		Bag<Integer> b = nounsToSynsets.get(nounB);
		
		if (a == null || b == null) throw new IllegalArgumentException();
		
		int ancestor = sap.ancestor(a, b);
		
		return idToSynsets.get(ancestor);
	}
	
	
	
	public static void main(String[] args)
	{
		String path = ".\\resources\\wordnet\\";
//		String synsetsTest   = "synsets.txt";
//		String hypernymsTest = "hypernyms.txt";
		String synsetsTest   = "synsets6.txt";
		String hypernymsTest = "hypernyms6InvalidCycle+Path.txt";
		WordNet wn = new WordNet(path + synsetsTest, path + hypernymsTest);
//		for (String s : wn.nouns()) System.out.println(s);
		System.out.println("There are " + wn.nounsToSynsets.size() + " nouns.");
		System.out.println("There are " + wn.dg.V() + " vertices, and " + wn.dg.E() + " edges.");
		System.out.println("Is gluten a noun? " + wn.isNoun("gluten"));
		System.out.println("Distance from white_marlin to mileage is " + wn.distance("white_marlin", "mileage"));
		System.out.println("Distance from Black_Plague to black_marlin is " + wn.distance("Black_Plague", "black_marlin"));
	}

}
