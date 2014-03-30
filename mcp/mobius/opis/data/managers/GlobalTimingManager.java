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
	
	HashBasedTable<Integer, String, StatsWorld> statsDim = HashBasedTable.create();
	HashBasedTable<CoordinatesChunk, String, StatsChunkMedian> statsChunk = HashBasedTable.create();
	
	
	/*
	public static HashMap<Integer, StatsWorld> worldUpdatesStats           = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldBlocksAndAmbianceStats = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldPlayerInstancesStats   = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldVillageCollectionStats = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldVillageSiegeStats      = new HashMap<Integer, StatsWorld>();
	public static HashMap<Integer, StatsWorld> worldApplyBlockEventsStats  = new HashMap<Integer, StatsWorld>();
	*/
	
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
	
	public void addStat(int id, String subsection, long timing){
		if (!statsDim.contains(id, subsection)){
			statsDim.put(id, subsection, new StatsWorld(id));
		}
		statsDim.get(id, subsection).addMeasure(timing);
	}
	
	public void addStat(CoordinatesChunk coord, String subsection, long timing){
		
		if (!statsChunk.contains(coord, subsection)){
			statsChunk.put(coord, subsection, new StatsChunkMedian(coord));
		}
		statsChunk.get(coord, subsection).addMeasure(timing);
	}	
	
	public HashBasedTable<CoordinatesChunk, String, Double> getStatsChunk(){
		HashBasedTable<CoordinatesChunk, String, Double> timings = HashBasedTable.create();
		
		for(Table.Cell<CoordinatesChunk, String, StatsChunkMedian> cell : this.statsChunk.cellSet()){
			timings.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getGeometricMean());
		}
		
		return timings;
	}
	
	public HashBasedTable<Integer, String, Double> getStatsDim(){
		HashBasedTable<Integer, String, Double> timings = HashBasedTable.create();
		
		for(Table.Cell<Integer, String, StatsWorld> cell : this.statsDim.cellSet()){
			timings.put(cell.getRowKey(), cell.getColumnKey(), cell.getValue().getGeometricMean());
		}
		
		return timings;
	}
	
	public HashBasedTable<Integer, String, Double> getStatsDimPerSection(){
		HashBasedTable<Integer, String, Double> timings = HashBasedTable.create();
		
		for(Table.Cell<CoordinatesChunk, String, StatsChunkMedian> cell : this.statsChunk.cellSet()){
			if (!timings.contains(cell.getRowKey().dim, cell.getColumnKey()))
				timings.put(cell.getRowKey().dim, cell.getColumnKey(), 0.0);
			
			timings.put(cell.getRowKey().dim, cell.getColumnKey(), timings.get(cell.getRowKey().dim, cell.getColumnKey()) + cell.getValue().getGeometricMean());
		}
		
		return timings;
	}	
}
