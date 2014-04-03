package mcp.mobius.opis.data.server;

import net.minecraft.world.ChunkCoordIntPair;

import com.google.common.collect.HashBasedTable;

import mcp.mobius.mobiuscore.profiler.IProfilerWorldTick;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.stats.StatsWorld;
import mcp.mobius.opis.data.managers.GlobalTimingManager;

public final class WorldTickProfiler extends AbstractProfiler implements IProfilerWorldTick {

	private static WorldTickProfiler _instance = new WorldTickProfiler();
	public  static WorldTickProfiler instance() {return _instance;}	
	
	
	
	//public  StatsBase  stats = new StatsBase();
	
	//HashMap<Integer, DescriptiveStatistics> worldTick = 
	
	HashBasedTable<Integer, String, Clock>           clocksDim   = HashBasedTable.create();
	HashBasedTable<CoordinatesChunk, String, Clock> clocksChunk = HashBasedTable.create();	
	
	@Override
	public void WorldTickStart(int id) {
		this.clock.start();
	}

	@Override
	public void WorldTickEnd(int id) {
		this.clock.stop();
		GlobalTimingManager.INSTANCE.addStat(id, this.clock.timeDelta, GlobalTimingManager.INSTANCE.worldTickStats);
	}

	@Override
	public void startDim(int dim, String subsection) {
		if (!clocksDim.contains(dim, subsection)){
			clocksDim.put(dim, subsection, new Clock());
		}
		clocksDim.get(dim, subsection).start();
	}

	@Override
	public void stopDim(int dim, String subsection) {
		clocksDim.get(dim, subsection).stop();
		GlobalTimingManager.INSTANCE.addStat(dim, subsection, clocksDim.get(dim, subsection).timeDelta);
	}

	@Override
	public void startChunk(int dim, ChunkCoordIntPair chunk, String subsection) {
		CoordinatesChunk coord = new CoordinatesChunk(dim, chunk);
		if (!clocksChunk.contains(coord, subsection)){
			clocksChunk.put(coord, subsection, new Clock());
		}
		clocksChunk.get(coord, subsection).start();
		
	}

	@Override
	public void stopChunk(int dim, ChunkCoordIntPair chunk, String subsection) {
		CoordinatesChunk coord = new CoordinatesChunk(dim, chunk);
		clocksChunk.get(coord, subsection).stop();
		GlobalTimingManager.INSTANCE.addStat(coord, subsection, clocksChunk.get(coord, subsection).timeDelta);
		
	}
}
