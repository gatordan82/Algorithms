package wordnet;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

public class Outcast
{
	private WordNet wn;
	
	
	public Outcast(WordNet wordnet)
	{
		wn = wordnet;
	}
	
	public String outcast(String[] nouns)
	{
		if (nouns == null) throw new NullPointerException();
		
		int outcastDistance = 0;
		String outcast = "";
		int dist = 0;
		for (String nounI : nouns)
		{
			for (String nounJ : nouns)
			{
				if (nounJ != nounI)
					dist += wn.distance(nounI,  nounJ);
			}
			if (dist > outcastDistance)
			{
				outcastDistance = dist;
				outcast = nounI;
			}
			dist = 0;
		}
		
		return outcast;
	}
	
	public static void main(String[] args)
	{
		WordNet wordnet = new WordNet(args[0], args[1]);
		Outcast outcast = new Outcast(wordnet);
		for (int t = 2; t < args.length; t++) 
		{
			In in = new In(args[t]);
			String[] nouns = in.readAllStrings();
			StdOut.println(args[t] + ": " + outcast.outcast(nouns));
		}
	}

}
