import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class BaseballElimination {
	
	private final int teamNumber;
	private String[] teamNames;
	private int[] wins;
	private int[] loses;
	private int[] remains;
	private int[][] schedules;
	
	private HashMap<String, Integer>IDs;
	private FordFulkerson maxflow;
	
	private boolean marked;
	private int subset;
	
	public BaseballElimination(String filename)                    // create a baseball division from given filename in format specified below
	{
		In in = new In(filename);
		teamNumber = in.readInt();
		teamNames = new String[teamNumber];
		wins = new int[teamNumber];
		loses = new int[teamNumber];
		remains = new int[teamNumber];
		schedules = new int[teamNumber][teamNumber];
		IDs = new HashMap<String, Integer>();
		int index = 0;
		
		while(index < teamNumber){
			teamNames[index] = in.readString();
			IDs.put(teamNames[index], index);
			wins[index] = in.readInt();
			loses[index] = in.readInt();
			remains[index] = in.readInt();
			
			for(int i = 0;i < teamNumber;i++)
			{
				schedules[index][i] = in.readInt();
			}
			
			index++;
		}
	}
	
	public int numberOfTeams()                        // number of teams
	{
		return teamNumber;
	}
	
	public Iterable<String> teams()                  // all teams
	{
		List<String> wordList = Arrays.asList(teamNames);
		return wordList;
	}
	
	public int wins(String team)                      // number of wins for given team
	{
		if(!IDs.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		
		int index = IDs.get(team);
		return wins[index];
	}
	
	public int losses(String team)                    // number of losses for given team
	{
		if(!IDs.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		
		int index = IDs.get(team);
		return loses[index];
	}
	
	public int remaining(String team)                 // number of remaining games for given team
	{
		if(!IDs.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		
		int index = IDs.get(team);
		return remains[index];
	}
	
	public int against(String team1, String team2)    // number of remaining games between team1 and team2
	{
		if(!IDs.containsKey(team1))
			throw new java.lang.IllegalArgumentException();
		if(!IDs.containsKey(team2))
			throw new java.lang.IllegalArgumentException();
		
		int index1 = IDs.get(team1);
		int index2 = IDs.get(team2);
		return schedules[index1][index2];
	}
	
	public boolean isEliminated(String team)              // is given team eliminated?
	{
		if(!IDs.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		
		marked = false;
		int row1 = (teamNumber-2)*(teamNumber-1)/2;
		int nodesNumber = row1 + (teamNumber-1) + 2;
		FlowNetwork flowNetwork = new FlowNetwork(nodesNumber);
		int index = IDs.get(team);
		int totalRemains = 0;
		
		int k = 1;
		for(int i = 0;i < teamNumber;i++)
		{
			for(int j = (i+1);j < teamNumber;j++)
			{
				if(i == index || j == index)
					continue;
				else
				{
					int id1 = teamNumberToIndex(i,index);
					int id2 = teamNumberToIndex(j,index);
					totalRemains += schedules[i][j];
					FlowEdge fe = new FlowEdge(0, k, schedules[i][j]);
					FlowEdge fe1 = new FlowEdge(k, id1, Double.POSITIVE_INFINITY);
					FlowEdge fe2 = new FlowEdge(k, id2, Double.POSITIVE_INFINITY);
					k++;
					flowNetwork.addEdge(fe);
					flowNetwork.addEdge(fe1);
					flowNetwork.addEdge(fe2);
				}
			}
		}
		
		for(int i = 0;i < teamNumber;i++)
		{
			
			if(i == index)
				continue;
			else
			{
				int s = teamNumberToIndex(i,index);
				int capacity = wins[index]+remains[index]-wins[i];
				if(capacity < 0) {
					marked = true;
					subset = i;
					return true;
				}
				FlowEdge fe = new FlowEdge(s, nodesNumber-1, capacity);
				flowNetwork.addEdge(fe);
			}
		}

		maxflow = new FordFulkerson(flowNetwork, 0, nodesNumber-1);
		int flow = (int) maxflow.value();
		if(flow == totalRemains)
			return false;
		return true;
	}
	
	public Iterable<String> certificateOfElimination(String team)  // subset R of teams that eliminates given team; null if not eliminated
	{
		if(!IDs.containsKey(team))
			throw new java.lang.IllegalArgumentException();
		
		List<String> list = new ArrayList<String>();
		int index = IDs.get(team);
		
		if (isEliminated(team) == true)
		{
			if(marked == true){
				list.add(teamNames[subset]);
			}
			
			else
			{
				for(int i = 0;i < teamNumber;i++)
				{
					
					if(i == index)
						continue;
					else
					{
						int s = teamNumberToIndex(i,index);
						if(maxflow.inCut(s))
							list.add(teamNames[i]);
					}
				}

			}
				
			
			return list;
		}
		
		else
			return null;
	}
	
	private int teamNumberToIndex(int n, int Pivot)
	{
		int row1 = (teamNumber-2)*(teamNumber-1)/2;
		if(n < Pivot)
			return n+row1+1;
		else
			return n+row1;
	}
	
	public static void main(String[] args) {
	    BaseballElimination division = new BaseballElimination("teams5.txt");
	    for (String team : division.teams()) {
	        if (division.isEliminated(team)) {
	            StdOut.print(team + " is eliminated by the subset R = { ");
	            for (String t : division.certificateOfElimination(team)) {
	               StdOut.print(t + " ");
	            }
	            StdOut.println("}");
	        }
	        else {
	            StdOut.println(team + " is not eliminated");
	        }
	    }
	}
	
	
}
