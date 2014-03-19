package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.server.CommandAmountEntities;
import mcp.mobius.opis.data.holders.AmountHolder;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.SerializableInt;
import mcp.mobius.opis.data.holders.TargetEntity;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_DataList;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.overlay.OverlayStatus;
import mcp.mobius.opis.server.PlayerTracker;

public class DataReqHandler {

	private static DataReqHandler _instance;
	private DataReqHandler(){}
	
	public static DataReqHandler instance(){
		if(_instance == null)
			_instance = new DataReqHandler();			
		return _instance;
	}	

	public void handle(DataReq maintype, DataReq subtype, DataReq target, ISerializable param1, ISerializable param2, Player player){
		String   name  = ((EntityPlayer)player).getEntityName();
		
		if ((maintype == DataReq.OVERLAY) && (subtype == DataReq.CHUNK) && (target == DataReq.ENTITIES)){
			this.handleOverlayChunkEntities((CoordinatesChunk)param1, player);
		}
		
		else if ((maintype == DataReq.LIST) && (subtype == DataReq.CHUNK) && (target == DataReq.ENTITIES)){
				PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.CHUNK, DataReq.ENTITIES,  EntityManager.getEntitiesInChunk((CoordinatesChunk)param1)), (Player)player);
		}

		else if ((maintype == DataReq.LIST) && (subtype == DataReq.CHUNK) && (target == DataReq.LOADED)){
			PlayerTracker.instance().playerOverlayStatus.put(player, OverlayStatus.CHUNKSTATUS);
			PlayerTracker.instance().playerDimension.put(player, ((SerializableInt)param1).value);
			PacketDispatcher.sendPacketToPlayer(Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(((SerializableInt)param1).value)), player);
	}		
		
		else if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.TILETENTS)){
			ArrayList<TileEntityStats>  timingTileEnts = TileEntityManager.getTopEntities(100);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.TILETENTS, timingTileEnts), (Player)player);
		}
		
		else if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.ENTITIES)){
			ArrayList<EntityStats>      timingEntities = EntityManager.getTopEntities(100);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.ENTITIES,  timingEntities), (Player)player);
		}
		
		else if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.HANDLERS)){
			ArrayList<TickHandlerStats> timingHandlers = TickHandlerManager.getCumulatedStats();
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.HANDLERS,  timingHandlers), (Player)player);
		}
		
		else if ((maintype == DataReq.LIST) && (subtype == DataReq.TIMING) && (target == DataReq.CHUNK)){
			ArrayList<ChunkStats> timingChunks = ChunkManager.getTopChunks(100);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.CHUNK,  timingChunks), (Player)player);
		}
		
		else if ((maintype == DataReq.LIST) && (subtype == DataReq.AMOUNT) && (target == DataReq.ENTITIES)){
			boolean filtered = false;
			if (PlayerTracker.instance().filteredAmount.containsKey(name))
				filtered = PlayerTracker.instance().filteredAmount.get(name);
			
			ArrayList<AmountHolder> ents = EntityManager.getCumulativeEntities(filtered);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.AMOUNT, DataReq.ENTITIES,  ents), (Player)player);
		}
		
		else if ((maintype == DataReq.COMMAND) && (subtype == DataReq.FILTERING) && (target == DataReq.TRUE)){
			PlayerTracker.instance().filteredAmount.put(name, true);
		}

		else if ((maintype == DataReq.COMMAND) && (subtype == DataReq.FILTERING) && (target == DataReq.FALSE)){
			PlayerTracker.instance().filteredAmount.put(name, false);
		}		
		
		else if ((maintype == DataReq.COMMAND) && (subtype == DataReq.UNREGISTER)){
			PlayerTracker.instance().playerOverlayStatus.remove(player);
			PlayerTracker.instance().playerDimension.remove(player);
		}		

		else if ((maintype == DataReq.COMMAND) && (subtype == DataReq.START)){
			MetaManager.reset();	
			modOpis.profilerRun = true;
			ProfilerRegistrar.turnOn();
		}		
		
		else if ((maintype == DataReq.COMMAND) && (subtype == DataReq.TELEPORT) && (target == DataReq.BLOCK)){
			EntityManager.teleportPlayer((CoordinatesBlock)param1, (EntityPlayerMP)player);
		}	
		
		else if ((maintype == DataReq.COMMAND) && (subtype == DataReq.TELEPORT) && (target == DataReq.ENTITIES)){
			EntityManager.teleportPlayer(((TargetEntity)param1).entityID, ((TargetEntity)param1).dim, (EntityPlayerMP)player);
		}			
		
		else{
			modOpis.log.log(Level.WARNING, String.format("Unknown data request : %s / %s / %s", maintype, subtype, target));
		}
	}
	
	public void handleOverlayChunkEntities(CoordinatesChunk coord, Player player){
		
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = EntityManager.getAllEntitiesPerChunk();
		HashMap<CoordinatesChunk, Integer> perChunk = new HashMap<CoordinatesChunk, Integer>();
		
		for (CoordinatesChunk chunk : entities.keySet())
			perChunk.put(chunk, entities.get(chunk).size());

		PacketDispatcher.sendPacketToPlayer(Packet_DataOverlayChunkEntities.create(perChunk), player);
	}
	
}
