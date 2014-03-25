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
	

	
	private static ArrayList<CoordinatesChunk>  chunksLoad = new ArrayList<CoordinatesChunk>();
	private static HashMap<CoordinatesChunk, StatsChunk>  chunkMeanTime = new HashMap<CoordinatesChunk, StatsChunk>();
	public  static ArrayList<TicketData> tickets = new ArrayList<TicketData>();

	public static void setLoadedChunks(ArrayList<ISerializable> data){
		chunksLoad.clear();
		for (ISerializable chunk : data){
			chunksLoad.add((CoordinatesChunk)chunk);
		}
	}
	
	public static ArrayList<CoordinatesChunk> getLoadedChunks(){
		return chunksLoad;
	}	
	
	public static void setChunkMeanTime(ArrayList<ISerializable> data){
		chunkMeanTime.clear();
		for (ISerializable stat : data)
			chunkMeanTime.put(((StatsChunk)stat).getChunk(), (StatsChunk)stat);
	}
	
	public static HashMap<CoordinatesChunk, StatsChunk> getChunkMeanTime(){
		return chunkMeanTime;
	}
	
	public static ArrayList<CoordinatesChunk> getLoadedChunks(int dimension){
		HashSet<CoordinatesChunk> chunkStatus = new HashSet<CoordinatesChunk>();
		WorldServer world = DimensionManager.getWorld(dimension);
		if (world != null)
		{
			for (ChunkCoordIntPair coord : world.getPersistentChunks().keySet())
				chunkStatus.add(new CoordinatesChunk(dimension, coord, (byte)1));
			
			for (Object o : ((ChunkProviderServer)world.getChunkProvider()).loadedChunks){
				Chunk chunk = (Chunk)o;
				
				chunkStatus.add(new CoordinatesChunk(dimension, chunk.getChunkCoordIntPair(), (byte)0));
			}			
		}
		
		return new ArrayList<CoordinatesChunk>(chunkStatus);
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

	public static int getLoadedChunkAmount(){	
		int loadedChunks = 0;
		for (WorldServer world : DimensionManager.getWorlds()){
			int loadedChunksForDim = world.getChunkProvider().getLoadedChunkCount();
			loadedChunks += loadedChunksForDim;
			//System.out.printf("[ %2d ]  %d chunks\n", world.provider.dimensionId, loadedChunksForDim);
		}
		//System.out.printf("Total : %d chunks\n", loadedChunks);
		return loadedChunks;
	}
	
	public static int getForcedChunkAmount(){	
		int forcedChunks = 0;
		for (WorldServer world : DimensionManager.getWorlds()){
			forcedChunks += world.getPersistentChunks().size();
		}
		return forcedChunks;
	}	
}
