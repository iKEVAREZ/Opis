package mcp.mobius.opis.data.managers;

import java.util.HashMap;

import mcp.mobius.opis.data.holders.stats.StatsWorld;

public class GlobalTimingManager {

	public static HashMap<Integer, StatsWorld> worldTickStats = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> entUpdateStats = new HashMap<Integer, StatsWorld>();
	
	/* Add an entity to the stat array, with timing data */
	public static void addWorldTickStat(int id, long timing){
		if (!(worldTickStats.containsKey(id)))
			worldTickStats.put(id, new StatsWorld(id));
		worldTickStats.get(id).addMeasure(timing);
	}	
	
	public static void addEntUpdateStat(int id, long timing){
		if (!(entUpdateStats.containsKey(id)))
			entUpdateStats.put(id, new StatsWorld(id));
		entUpdateStats.get(id).addMeasure(timing);
	}	

	public static double getTotalWorldTickStats(){
		double total = 0D;
		for (Integer dim : worldTickStats.keySet())
			total += worldTickStats.get(dim).getGeometricMean();
		return total;
	}	
	
	public static double getTotalEntUpdateStats(){
		double total = 0D;
		for (Integer dim : entUpdateStats.keySet())
			total += entUpdateStats.get(dim).getGeometricMean();
		return total;
	}
	
}
