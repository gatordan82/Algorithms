/**
 * 
 */
package baseball;

import edu.princeton.cs.algs4.Bag;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.ST;
import edu.princeton.cs.algs4.StdOut;

/**
 * @author Dan
 *
 */
public class BaseballElimination
{
	private int N;
	private String[] teamNames;
	private LinearProbingHashST<String, Team> teams;
	
	private class Team
	{
		private int wins;
		private int losses;
		private int remaining;
		private LinearProbingHashST<String, Integer> against;
		
		private Team(String[] schedule, String[] teams)
		{
			wins      = Integer.parseInt(schedule[1]);
			losses    = Integer.parseInt(schedule[2]);
			remaining = Integer.parseInt(schedule[3]);
			
			against = new LinearProbingHashST<String, Integer>();
			
			for (int i = 0; i < N; i++)
				against.put(teams[i], Integer.parseInt(schedule[i + 4]));
		}
	}
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		N = Integer.parseInt(in.readLine());
		String[][] statLines = new String[N][N-2];
		
		teamNames = new String[N];
		int j = 0;
		while (!in.isEmpty())
		{
			statLines[j] = in.readLine().trim().split("\\s+");
			teamNames[j] = statLines[j][0];
			j++;
		}
		
		teams = new LinearProbingHashST<String, Team>();
		for (int i = 0; i < N; i++)
			teams.put(teamNames[i], new Team(statLines[i], teamNames));
	}
	
	public int numberOfTeams()
	{
		return N;
	}
	
	public Iterable<String> team()
	{
		return teams.keys();
	}
	
	public int wins(String team)
	{
		return teams.get(team).wins;
	}
	
	public int losses(String team)
	{
		return teams.get(team).losses;
	}
	
	public int remaining(String team)
	{
		return teams.get(team).remaining;
	}
	
	public int against(String team1, String team2)
	{
		return teams.get(team1).against.get(team2);
	}
	
//	public boolean isEliminated(String team)
//	{
//		
//	}
//	
//	public Iterable<String> certificateOfElimination(String team)
//	{
//		
//	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.team())
		{
			System.out.println(division.wins(team));
//			if (division.isEliminated(team))
//			{
//				StdOut.print(team + " is eliminated by the subset R = { ");
//				for (String t : division.certificateOfElimination(team))
//				{
//					StdOut.print(t + " ");
//				}
//				StdOut.println("}");
//			}
//			else
//			{
//				StdOut.println(team + " is not eliminated.");
//			}
		}
	}

}
