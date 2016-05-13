

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
			if (dist >= outcastDistance)
			{
				outcastDistance = dist;
				outcast = nounI;
			}
		}
		
		return outcast;
	}
	
	public static void main(String[] args)
	{
		// TODO Auto-generated method stub

	}

}
