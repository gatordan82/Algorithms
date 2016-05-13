/**
 * 
 */
package baseball;

import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;

/**
 * @author Dan
 *
 */
public class BaseballElimination
{
	private int N;
	private LinearProbingHashST<String, Integer> teamNames;
	private int[] wins;
	private int[] losses;
	private int[] remaining;
	private LinearProbingHashST<String, Integer[]> against;
	
//	private class Team
//	{
//		private int wins;
//		private int losses;
//		private int remaining;
//		private LinearProbingHashST<String, Integer> against;
//		
//		private Team(String[] schedule, String[] teams)
//		{
//			wins      = Integer.parseInt(schedule[1]);
//			losses    = Integer.parseInt(schedule[2]);
//			remaining = Integer.parseInt(schedule[3]);
//			
//			against = new LinearProbingHashST<String, Integer>();
//			
//			for (int i = 0; i < N; i++)
//				against.put(teams[i], Integer.parseInt(schedule[i + 4]));
//		}
//	}
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		N = Integer.parseInt(in.readLine());
		
		teamNames = new LinearProbingHashST<String, Integer>();
		wins      = new int[N];
		losses    = new int[N];
		remaining = new int[N];
		against   = new LinearProbingHashST<String, Integer[]>();
		
		int i = 0;
		String[] statLine = new String[N + 4];
		
		while (!in.isEmpty())
		{
			statLine = in.readLine().trim().split("\\s+");
			teamNames.put(statLine[0], i);
			wins[i]      = Integer.parseInt(statLine[1]);
			losses[i]    = Integer.parseInt(statLine[2]);
			remaining[i] = Integer.parseInt(statLine[3]);
			
			Integer[] againstArray = new Integer[N];
			for (int j = 0; j < N; j++)
				againstArray[j] = Integer.parseInt(statLine[j + 4]);
			against.put(statLine[0], againstArray);
			
			i++;
		}
		
//		teams = new LinearProbingHashST<String, Team>();
//		for (int i = 0; i < N; i++)
//			teams.put(teamNames[i], new Team(statLines[i], teamNames));
	}
	
	public int numberOfTeams()
	{
		return N;
	}
	
	public Iterable<String> team()
	{
		return against.keys();
	}
	
	private int getTeamIndex(String team)
	{
		return teamNames.get(team);
	}
	
	public int wins(String team)
	{
		return wins[getTeamIndex(team)];
	}
	
	public int losses(String team)
	{
		return losses[getTeamIndex(team)];
	}
	
	public int remaining(String team)
	{
		return remaining[getTeamIndex(team)];
	}
	
	public int against(String team1, String team2)
	{
		int team2Index = getTeamIndex(team2);
		return against.get(team1)[team2Index];
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
			System.out.println(team + " has " + division.wins(team) + " wins.");
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
