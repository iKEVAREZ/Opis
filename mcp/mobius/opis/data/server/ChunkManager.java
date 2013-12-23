package mcp.mobius.opis.data.server;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.TicketData;
import mcp.mobius.opis.data.holders.TileEntityStats;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkManager {
	

	
	public static HashMap<ChunkCoordIntPair, Boolean>       chunksLoad    = new HashMap<ChunkCoordIntPair, Boolean>();
	public static HashMap<CoordinatesChunk, ChunkStats> chunkMeanTime = new HashMap<CoordinatesChunk, ChunkStats>();
	public static ArrayList<TicketData> tickets = new ArrayList<TicketData>();

	public static HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>> getAllLoadedChunks(){
		HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>> chunkStatus = new HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>>();
		for (int dim : DimensionManager.getIDs())
			chunkStatus.put(dim, ChunkManager.getLoadedChunks(dim));
		
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

	public static HashSet<TicketData> getTickets(){
		HashSet<TicketData> tickets = new HashSet<TicketData>();
		for (int dim : DimensionManager.getIDs())
			for (Ticket ticket : DimensionManager.getWorld(dim).getPersistentChunks().values())
				tickets.add(new TicketData(ticket));
		
		return tickets;
	}
	
	public static ArrayList<ChunkStats> getChunksUpdateTime(){
		HashMap<CoordinatesChunk, ChunkStats> chunks = new HashMap<CoordinatesChunk, ChunkStats>();
		
		for (TileEntityStats stat : TileEntityManager.stats.values()){
			if (!chunks.containsKey(stat.getChunk()))
				chunks.put(stat.getChunk(), new ChunkStats(stat.getChunk(), 0, 0));
			
			chunks.get(stat.getChunk()).nentities += 1;
			chunks.get(stat.getChunk()).updateTime += stat.getGeometricMean();
		}
		
		ArrayList<ChunkStats> chunksUpdate = new ArrayList<ChunkStats>(chunks.values());
		return chunksUpdate;
	}
	
	public static ArrayList<ChunkStats> getTopChunks(int quantity){
		ArrayList<ChunkStats> chunks  = ChunkManager.getChunksUpdateTime();
		ArrayList<ChunkStats> outList = new ArrayList<ChunkStats>();
		Collections.sort(chunks);
		
		for (int i = 0; i < Math.min(quantity, chunks.size()); i++)
			outList.add(chunks.get(i));
		
		return outList;
	}
	
}
