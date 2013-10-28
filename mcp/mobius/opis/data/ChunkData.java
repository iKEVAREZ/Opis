package mcp.mobius.opis.data;

import java.util.HashMap;
import java.util.Set;

import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class ChunkData {
	
	public static HashMap<ChunkCoordIntPair, Boolean> chunks = new HashMap<ChunkCoordIntPair, Boolean>();
	
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
