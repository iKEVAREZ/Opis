package mcp.mobius.opis.data.managers;

import java.util.HashMap;

import mcp.mobius.opis.data.holders.stats.StatsWorld;

public class GlobalTimingManager {

	public static HashMap<Integer, StatsWorld> worldTickStats = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> entUpdateStats = new HashMap<Integer, StatsWorld>();
	
	public static HashMap<Integer, StatsWorld> worldUpdatesStats           = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldBlocksAndAmbianceStats = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldPlayerInstancesStats   = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldVillageCollectionStats = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldVillageSiegeStats      = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldApplyBlockEventsStats  = new HashMap<Integer, StatsWorld>();
	
	/* Add an entity to the stat array, with timing data */
	public static void addStat(int id, long timing, HashMap<Integer, StatsWorld> target){
		if (!(target.containsKey(id)))
			target.put(id, new StatsWorld(id));
		target.get(id).addMeasure(timing);		
	}

	public static double getTotalStats(HashMap<Integer, StatsWorld> target){
		double total = 0D;
		for (Integer dim : target.keySet())
			total += target.get(dim).getGeometricMean();
		return total;
	}	
}
