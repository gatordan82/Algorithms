/**
 * 
 */
package baseball;

import edu.princeton.cs.algs4.FlowEdge;
import edu.princeton.cs.algs4.FlowNetwork;
import edu.princeton.cs.algs4.FordFulkerson;
import edu.princeton.cs.algs4.In;
import edu.princeton.cs.algs4.LinearProbingHashST;
import edu.princeton.cs.algs4.Queue;
import edu.princeton.cs.algs4.StdOut;

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
	private int[][] against;
	
	private FlowNetwork schedule;
	private final int totalVertices;
	private final int source;
	private final int sink;
	private int lastTeamChecked;
	private boolean isTeamChecked;
	private int totalRemaining;
	
	public BaseballElimination(String filename)
	{
		In in = new In(filename);
		N = Integer.parseInt(in.readLine());
		
		teamNames = new LinearProbingHashST<String, Integer>();
		wins      = new int[N];
		losses    = new int[N];
		remaining = new int[N];
		against   = new int[N][N];
		
		int i = 0;		
		while (!in.isEmpty())
		{
			String[] statLine = in.readLine().trim().split("\\s+");
			teamNames.put(statLine[0], i);
			wins[i]      = Integer.parseInt(statLine[1]);
			losses[i]    = Integer.parseInt(statLine[2]);
			remaining[i] = Integer.parseInt(statLine[3]);
			
			int[] againstArray = new int[N];
			for (int j = 0; j < N; j++)
				againstArray[j] = Integer.parseInt(statLine[j + 4]);
			against[i] = againstArray;
			
			i++;
		}
		
		totalVertices = N + N * (N - 1) / 2 + 2;
		source = totalVertices - 2;
		sink   = totalVertices - 1;
		
		lastTeamChecked = -1;
		isTeamChecked   = false;
		
		for (int j = 0; j < N; j++)
			for (int k = N - 1; k > j; k--)
				totalRemaining += against[j][k];
	}
	
	/**
	 * 
	 * @return number of teams in the division
	 */
	public int numberOfTeams()
	{
		return N;
	}
	
	/**
	 * 
	 * @return iterable list of team names
	 */
	public Iterable<String> teams()
	{
		return teamNames.keys();
	}
	
	private void checkValidTeam(String team)
	{
		if (!teamNames.contains(team)) throw new IllegalArgumentException(team + " is not a team in this division.");
	}
	
	/**
	 * 
	 * @param team
	 * @return integer index of team
	 */
	private int getTeamIndex(String team)
	{
		checkValidTeam(team);
		return teamNames.get(team);
	}
	
	/**
	 * 
	 * @param team
	 * @return number of wins so far for team
	 */
	public int wins(String team)
	{
		checkValidTeam(team);
		return wins[getTeamIndex(team)];
	}
	
	/**
	 * 
	 * @param team
	 * @return number of losses so far for team
	 */
	public int losses(String team)
	{
		checkValidTeam(team);
		return losses[getTeamIndex(team)];
	}
	
	/**
	 * 
	 * @param team
	 * @return number of games remaining to be played for team
	 */
	public int remaining(String team)
	{
		checkValidTeam(team);
		return remaining[getTeamIndex(team)];
	}
	
	/**
	 * 
	 * @param team1
	 * @param team2
	 * @return number of games remaining for team1 to play against team2
	 */
	public int against(String team1, String team2)
	{
		checkValidTeam(team1);
		checkValidTeam(team2);
		
		int i = getTeamIndex(team1);
		int j = getTeamIndex(team2);
		return against[i][j];
	}
	
	/**
	 * 
	 * @param team
	 * @return determine if the team is mathematically eliminated
	 */
	public boolean isEliminated(String team)
	{
		checkValidTeam(team);
		
		int x = getTeamIndex(team);
		
		// Check if team is trivially eliminated.  If team's wins plus games remaining 
		// is less than another team's current wins, they have no way to catch up.
		boolean eliminated = isTriviallyEliminated(x);
		if (eliminated) return eliminated;
		
		// Check if team is mathematically eliminated by comparing the match ups
		// of all other teams remaining in team X's schedule.
		buildScheduleNetwork(x);
		FordFulkerson ff = new FordFulkerson(schedule, source, sink);
		if (ff.value() != totalRemaining) eliminated = true;
		lastTeamChecked = x;
		isTeamChecked = true;
		
		return eliminated;
	}
	
	private boolean isTriviallyEliminated(int x)
	{
		boolean eliminated = false;
		for (int i = 0; i < N; i++)
			if (i != x && wins[x] + remaining[x] < wins[i])
				eliminated = true;
		
		return eliminated;
	}
	
	private void buildScheduleNetwork(int teamX)
	{
		schedule = new FlowNetwork(totalVertices);
		
		// Build the edges from the source to the team pair (match up) vertices
		int matches = 0;
		int teamVertices = N * (N - 1) / 2;
		for (int i = 0; i < N - 1; i++)
		{
			for (int j = i + 1; j < N; j++)
			{
				schedule.addEdge(new FlowEdge(source, matches, against[i][j]));
				schedule.addEdge(new FlowEdge(matches, teamVertices + i, Integer.MAX_VALUE));
				schedule.addEdge(new FlowEdge(matches, teamVertices + j, Integer.MAX_VALUE));
				matches++;
			}
			schedule.addEdge(new FlowEdge(teamVertices + i, sink, wins[teamX] + remaining[teamX] - wins[i]));
		}
		schedule.addEdge(new FlowEdge(teamVertices + N - 1, sink, wins[teamX] + remaining[teamX] - wins[N - 1]));
	}
	
	public Iterable<String> certificateOfElimination(String team)
	{
		checkValidTeam(team);
		
		int x = getTeamIndex(team);
		Queue<String> cert = new Queue<String>();
		
		if (isTriviallyEliminated(x))
		{
			for (String t : teams())
			{
				int i = getTeamIndex(t);
				if (i != x && wins[x] + remaining[x] < wins[i])
					cert.enqueue(t);
			}
			return cert;
		}
		
		// Check if network is already cached, if not, build a new network
		if (isTeamChecked || lastTeamChecked != x)
			buildScheduleNetwork(x);

		FordFulkerson ff = new FordFulkerson(schedule, source, sink);
		if (ff.value() == totalRemaining) return null;
		else
			for (String t : teams())
				if (!t.equals(team))
				{
					int i = getTeamIndex(t);
					int vertex = N * (N - 1) / 2 + i;
					if (ff.inCut(vertex)) cert.enqueue(t);
				}
		
		return cert;
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args)
	{
		BaseballElimination division = new BaseballElimination(args[0]);
		for (String team : division.teams())
		{
//			System.out.println(team + " has " + division.wins(team) + " wins.");
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
//				if (division.certificateOfElimination(team) == null);
//					StdOut.println("The certificate is null!");
			}
		}
	}

}
