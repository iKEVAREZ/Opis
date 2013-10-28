package mcp.mobius.opis.data;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class ChunksData {
	
	public static HashMap<ChunkCoordIntPair, Boolean> chunksLoad    = new HashMap<ChunkCoordIntPair, Boolean>();
	public static HashMap<CoordinatesChunk, ChunkStatsData> chunkMeanTime = new HashMap<CoordinatesChunk, ChunkStatsData>();

	public static HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>> getAllLoadedChunks(){
		HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>> chunkStatus = new HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>>();
		for (int dim : DimensionManager.getIDs())
			chunkStatus.put(dim, ChunksData.getLoadedChunks(dim));
		
		return chunkStatus;
	}
	
	public static HashMap<ChunkCoordIntPair, Boolean> getLoadedChunks(int dimension){
		HashMap<ChunkCoordIntPair, Boolean> chunkStatus = new HashMap<ChunkCoordIntPair, Boolean>();
		WorldServer world = DimensionManager.getWorld(dimension);
		if (world != null)
		{
			Set<ChunkCoordIntPair> persistantChunks = world.getPersistentChunks().keySet();
			Set<ChunkCoordIntPair> chunks = (Set<ChunkCoordIntPair>)world.activeChunkSet;
			
			for (ChunkCoordIntPair chunk : chunks){
				chunkStatus.put(chunk, persistantChunks.contains(chunk));
			}
		}
		
		return chunkStatus;
	}
}
