package mcp.mobius.opis.data.managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import net.minecraft.world.ChunkCoordIntPair;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;

import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsChunkMedian;
import mcp.mobius.opis.data.holders.stats.StatsWorld;

public enum GlobalTimingManager {
	INSTANCE;
	
	public HashMap<Integer, StatsWorld> worldTickStats = new HashMap<Integer, StatsWorld>();
	public HashMap<Integer, StatsWorld> entUpdateStats = new HashMap<Integer, StatsWorld>();
	
	/* Add an entity to the stat array, with timing data */
	public void addStat(int id, long timing, HashMap<Integer, StatsWorld> target){
		if (!(target.containsKey(id)))
			target.put(id, new StatsWorld(id));
		target.get(id).addMeasure(timing);		
	}

	public double getTotalStats(HashMap<Integer, StatsWorld> target){
		double total = 0D;
		for (Integer dim : target.keySet())
			total += target.get(dim).getGeometricMean();
		return total;
	}	
}
