package mcp.mobius.opis.data.managers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkManager {
	

	
	public  static HashMap<ChunkCoordIntPair, Boolean>       chunksLoad = new HashMap<ChunkCoordIntPair, Boolean>();
	private static HashMap<CoordinatesChunk, StatsChunk>  chunkMeanTime = new HashMap<CoordinatesChunk, StatsChunk>();
	public  static ArrayList<TicketData> tickets = new ArrayList<TicketData>();

	public static void setChunkMeanTime(ArrayList<ISerializable> stats){
		chunkMeanTime.clear();
		for (ISerializable stat : stats)
			chunkMeanTime.put(((StatsChunk)stat).getChunk(), (StatsChunk)stat);
	}
	
	public static HashMap<CoordinatesChunk, StatsChunk> getChunkMeanTime(){
		return chunkMeanTime;
	}
	
	public static HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>> getAllLoadedChunks(){
		HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>> chunkStatus = new HashMap<Integer, HashMap<ChunkCoordIntPair, Boolean>>();
		for (int dim : DimensionManager.getIDs()){
			chunkStatus.put(dim, ChunkManager.getLoadedChunks(dim));
		}
		
		return chunkStatus;
	}
	
	public static HashMap<ChunkCoordIntPair, Boolean> getLoadedChunks(int dimension){
		HashMap<ChunkCoordIntPair, Boolean> chunkStatus = new HashMap<ChunkCoordIntPair, Boolean>();
		WorldServer world = DimensionManager.getWorld(dimension);
		if (world != null)
		{
			Set<ChunkCoordIntPair> persistantChunks = world.getPersistentChunks().keySet();
			Set<ChunkCoordIntPair> chunks = (Set<ChunkCoordIntPair>)world.activeChunkSet;
			HashSet<ChunkCoordIntPair> provider = new HashSet<ChunkCoordIntPair>();
			
			List loadedChunks = ((ChunkProviderServer)world.getChunkProvider()).loadedChunks;
			
			for (ChunkCoordIntPair chunk : chunks){
				chunkStatus.put(chunk, persistantChunks.contains(chunk));
			}
			
			for (Object o : loadedChunks){
				Chunk chunk = (Chunk)o;
				if(!chunkStatus.containsKey(chunk.getChunkCoordIntPair()))
					chunkStatus.put(chunk.getChunkCoordIntPair(), false);
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
	
	public static ArrayList<StatsChunk> getChunksUpdateTime(){
		HashMap<CoordinatesChunk, StatsChunk> chunks = new HashMap<CoordinatesChunk, StatsChunk>();
		
		for (StatsTileEntity stat : TileEntityManager.stats.values()){
			if (!chunks.containsKey(stat.getChunk()))
				chunks.put(stat.getChunk(), new StatsChunk(stat.getChunk()));
			
			chunks.get(stat.getChunk()).addTileEntity();
			chunks.get(stat.getChunk()).addMeasure(stat.getGeometricMean());
		}
		
		for (StatsEntity stat : EntityManager.stats.values()){
			if (!chunks.containsKey(stat.getChunk()))
				chunks.put(stat.getChunk(), new StatsChunk(stat.getChunk()));
			
			chunks.get(stat.getChunk()).addEntity();
			chunks.get(stat.getChunk()).addMeasure(stat.getGeometricMean());
		}		
		
		ArrayList<StatsChunk> chunksUpdate = new ArrayList<StatsChunk>(chunks.values());
		return chunksUpdate;
	}
	
	public static ArrayList<StatsChunk> getTopChunks(int quantity){
		ArrayList<StatsChunk> chunks  = ChunkManager.getChunksUpdateTime();
		ArrayList<StatsChunk> outList = new ArrayList<StatsChunk>();
		Collections.sort(chunks);
		
		for (int i = 0; i < Math.min(quantity, chunks.size()); i++)
			outList.add(chunks.get(i));
		
		return outList;
	}
	
}
