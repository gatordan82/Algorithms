package wordnet;

import edu.princeton.cs.algs4.Digraph;
import edu.princeton.cs.algs4.DirectedCycle;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

public class WordNet
{
	private Digraph dg;
	private SAP sap;
	private LinearProbingHashST<String, Integer> nounsToSynsets;
	private LinearProbingHashST<Integer, String> idToSynsets;
	
	public WordNet(String synsets, String hypernyms)
	{
		if (synsets == null || hypernyms == null)
			throw new NullPointerException();
		
		readSynsets(synsets);
		readHypernyms(hypernyms);
		
		// Check if the digraph has a cycle
		DirectedCycle dc = new DirectedCycle(dg);
		if (dc.hasCycle()) throw new IllegalArgumentException();
		
		sap = new SAP(dg);
	}
	
	private void readSynsets(String synsets)
	{
		nounsToSynsets = new LinearProbingHashST<String, Integer>();
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
				nounsToSynsets.put(nouns[i], id);
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
		
		int a = nounsToSynsets.get(nounA);
		int b = nounsToSynsets.get(nounB);
		
		if (a == -1 || b == -1) throw new IllegalArgumentException();

		return sap.length(a, b);
	}
	
	public String sap(String nounA, String nounB)
	{
		if (nounA == null || nounB == null)
			throw new NullPointerException();
		
		int a = nounsToSynsets.get(nounA);
		int b = nounsToSynsets.get(nounB);
		
		if (a == -1 || b == -1) throw new IllegalArgumentException();
		
		int ancestor = sap.ancestor(a, b);
		
		return idToSynsets.get(ancestor);
	}
	
	
	
	public static void main(String[] args)
	{
		String path = ".\\resources\\wordnet\\";
		String synsetsTest   = "synsets.txt";
		String hypernymsTest = "hypernyms.txt";
		WordNet wn = new WordNet(path + synsetsTest, path + hypernymsTest);
//		for (String s : wn.nouns()) System.out.println(s);
		System.out.println("Is gluten a noun? " + wn.isNoun("gluten"));
		System.out.println("Distance from white_marlin to mileage is " + wn.distance("white_marlin", "mileage"));
	}

}
