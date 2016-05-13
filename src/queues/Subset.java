package queues;

import edu.princeton.cs.algs4.StdIn;
import edu.princeton.cs.algs4.StdOut;
import edu.princeton.cs.algs4.StdRandom;

public class Subset 
{

	public static void main(String[] args) 
	{
		int k = Integer.parseInt(args[0]);
		int i = 0;
		
		RandomizedQueue<String> rq = new RandomizedQueue<String>();
		for (; i < k; i++)
		{
			rq.enqueue(StdIn.readString());
		}
		
		while (!StdIn.isEmpty())
		{
			String str = StdIn.readString();
			++i;
			
			int rand = StdRandom.uniform(i);
			
			if (rand < k)
			{
				rq.dequeue();
				rq.enqueue(str);
			}
		}
		
		for (String currentString : rq)
		{
			StdOut.println(currentString);
		}
		

	}

}
