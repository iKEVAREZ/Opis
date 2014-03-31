package mcp.mobius.opis.data.managers;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.gen.ChunkProviderServer;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;

public class ChunkManager implements IMessageHandler{
	
	private static ChunkManager _instance = new ChunkManager();
	public  static ChunkManager instance(){return _instance;};
	private ChunkManager(){}
	
	private ArrayList<CoordinatesChunk>  chunksLoad = new ArrayList<CoordinatesChunk>();
	private HashMap<CoordinatesChunk, StatsChunk>  chunkMeanTime = new HashMap<CoordinatesChunk, StatsChunk>();
	public  ArrayList<TicketData> tickets = new ArrayList<TicketData>();

	public void addLoadedChunks(ArrayList<ISerializable> data){
		//chunksLoad.clear();
		for (ISerializable chunk : data){
			chunksLoad.add((CoordinatesChunk)chunk);
		}
	}
	
	public ArrayList<CoordinatesChunk> getLoadedChunks(){
		return chunksLoad;
	}	
	
	public void setChunkMeanTime(ArrayList<ISerializable> data){
		chunkMeanTime.clear();
		for (ISerializable stat : data)
			chunkMeanTime.put(((StatsChunk)stat).getChunk(), (StatsChunk)stat);
	}
	
	public HashMap<CoordinatesChunk, StatsChunk> getChunkMeanTime(){
		return chunkMeanTime;
	}
	
	public ArrayList<CoordinatesChunk> getLoadedChunks(int dimension){
		HashSet<CoordinatesChunk> chunkStatus = new HashSet<CoordinatesChunk>();
		WorldServer world = DimensionManager.getWorld(dimension);
		if (world != null)
		{
			for (Object o : ((ChunkProviderServer)world.getChunkProvider()).loadedChunks){
				Chunk chunk = (Chunk)o;
				
				chunkStatus.add(new CoordinatesChunk(dimension, chunk.getChunkCoordIntPair(), (byte)0));
			}
			
			for (ChunkCoordIntPair coord : world.getPersistentChunks().keySet())
				chunkStatus.add(new CoordinatesChunk(dimension, coord, (byte)1));			
		}
		
		return new ArrayList<CoordinatesChunk>(chunkStatus);
	}

	public HashSet<TicketData> getTickets(){
		HashSet<TicketData> tickets = new HashSet<TicketData>();
		for (int dim : DimensionManager.getIDs())
			for (Ticket ticket : DimensionManager.getWorld(dim).getPersistentChunks().values())
				tickets.add(new TicketData(ticket));
		
		return tickets;
	}
	
	public ArrayList<StatsChunk> getChunksUpdateTime(){
		HashMap<CoordinatesChunk, StatsChunk> chunks = new HashMap<CoordinatesChunk, StatsChunk>();
		
		for (StatsTileEntity stat : TileEntityManager.stats.values()){
			if (!chunks.containsKey(stat.getChunk()))
				chunks.put(stat.getChunk(), new StatsChunk(stat.getChunk()));
			
			chunks.get(stat.getChunk()).addTileEntity();
			chunks.get(stat.getChunk()).addMeasure(stat.getGeometricMean());
		}
		
		for (StatsEntity stat : EntityManager.INSTANCE.stats.values()){
			if (!chunks.containsKey(stat.getChunk()))
				chunks.put(stat.getChunk(), new StatsChunk(stat.getChunk()));
			
			chunks.get(stat.getChunk()).addEntity();
			chunks.get(stat.getChunk()).addMeasure(stat.getGeometricMean());
		}		
		
		ArrayList<StatsChunk> chunksUpdate = new ArrayList<StatsChunk>(chunks.values());
		return chunksUpdate;
	}
	
	public ArrayList<StatsChunk> getTopChunks(int quantity){
		ArrayList<StatsChunk> chunks  = this.getChunksUpdateTime();
		ArrayList<StatsChunk> outList = new ArrayList<StatsChunk>();
		Collections.sort(chunks);
		
		for (int i = 0; i < Math.min(quantity, chunks.size()); i++)
			outList.add(chunks.get(i));
		
		return outList;
	}

	public int getLoadedChunkAmount(){	
		int loadedChunks = 0;
		for (WorldServer world : DimensionManager.getWorlds()){
			int loadedChunksForDim = world.getChunkProvider().getLoadedChunkCount();
			loadedChunks += loadedChunksForDim;
			//System.out.printf("[ %2d ]  %d chunks\n", world.provider.dimensionId, loadedChunksForDim);
		}
		//System.out.printf("Total : %d chunks\n", loadedChunks);
		return loadedChunks;
	}
	
	public int getForcedChunkAmount(){	
		int forcedChunks = 0;
		for (WorldServer world : DimensionManager.getWorlds()){
			forcedChunks += world.getPersistentChunks().size();
		}
		return forcedChunks;
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case LIST_TIMING_CHUNK:{
			this.setChunkMeanTime(rawdata.array);
			break;
		}
		case LIST_CHUNK_LOADED:{
			this.addLoadedChunks(rawdata.array);	 
			break;
		}		
		case LIST_CHUNK_LOADED_CLEAR:{
			chunksLoad.clear();
			break;
		}
		default:
			return false;
		}
		
		
		return true;
	}	
}
