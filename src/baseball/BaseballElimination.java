/**
 * 
 */
package baseball;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author Dan
 *
 */
public class BaseballElimination
{
	private int N;
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		N = Integer.parseInt(in.readLine());
		while (!in.isEmpty())
		{
			
		}
	}
	
	public int numberOfTeams()
	{
		return N;
	}
	
	public Iterable<String> team()
	{
		
	}
	
	public int wins(String team)
	{
		
	}
	
	public int losses(String team)
	{
		
	}
	
	public int remaining(String team)
	{
		
	}
	
	public int against(String team1, String team2)
	{
		
	}
	
	public boolean isEliminated(String team)
	{
		
	}
	
	public Iterable<String> certificateOfElimination(String team)
	{
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.team())
		{
			if (division.isEliminated(team))
			{
				StdOut.print(team + " is eliminated by the subset R = { ");
				for (String t : division.certificateOfElimination(team))
				{
					StdOut.print(t + " ");
				}
				StdOut.println("}");
			}
			else
			{
				StdOut.println(team + " is not eliminated.");
			}
		}
	}

}
