package mcp.mobius.opis.data;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.TicketData;
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
}
