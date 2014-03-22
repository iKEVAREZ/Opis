package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.server.CommandAmountEntities;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.holders.basetypes.TargetEntity;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.GlobalTimingManager;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.data.server.EntUpdateProfiler;
import mcp.mobius.opis.data.server.WorldTickProfiler;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_DataList;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataValue;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.network.server.Packet_Tickets;
import mcp.mobius.opis.overlay.OverlayStatus;

public class DataReqHandler {

	private static DataReqHandler _instance;
	private DataReqHandler(){}
	
	public static DataReqHandler instance(){
		if(_instance == null)
			_instance = new DataReqHandler();			
		return _instance;
	}	

	public void handle(DataReq maintype, ISerializable param1, ISerializable param2, Player player){
		String   name  = ((EntityPlayer)player).getEntityName();
		
		if (maintype == DataReq.OVERLAY_CHUNK_ENTITIES){
			this.handleOverlayChunkEntities((CoordinatesChunk)param1, player);
		}
		
		else if (maintype == DataReq.OVERLAY_CHUNK_TIMING){
			ArrayList<StatsChunk> timingChunks = ChunkManager.getTopChunks(100);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK,  timingChunks), (Player)player);
		}		
		
		else if (maintype == DataReq.LIST_CHUNK_TILEENTS){
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_CHUNK_TILEENTS, TileEntityManager.getTileEntitiesInChunk((CoordinatesChunk)param1)), player);
		}		
		
		else if (maintype == DataReq.LIST_CHUNK_ENTITIES){
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_CHUNK_ENTITIES,  EntityManager.getEntitiesInChunk((CoordinatesChunk)param1)), (Player)player);
		}

		else if (maintype == DataReq.LIST_CHUNK_LOADED){
			PlayerTracker.instance().playerOverlayStatus.put(player, OverlayStatus.CHUNKSTATUS);
			PlayerTracker.instance().playerDimension.put(player, ((SerialInt)param1).value);
			PacketDispatcher.sendPacketToPlayer(Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(((SerialInt)param1).value)), player);
		}		

		else if (maintype == DataReq.LIST_CHUNK_TICKETS){
			PacketDispatcher.sendPacketToPlayer(Packet_Tickets.create(ChunkManager.getTickets()), player);
		}		
		
		else if (maintype == DataReq.LIST_TIMING_TILEENTS){
			ArrayList<StatsTileEntity>  timingTileEnts = TileEntityManager.getTopEntities(100);
			SerialDouble totalTime = new SerialDouble(TileEntityManager.getTotalUpdateTime());
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create (DataReq.LIST_TIMING_TILEENTS, timingTileEnts), (Player)player);
			PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_TILEENTS, totalTime),      (Player)player);
		}
		
		else if (maintype == DataReq.LIST_TIMING_ENTITIES){
			ArrayList<StatsEntity>      timingEntities = EntityManager.getTopEntities(100);
			SerialDouble totalTime = new SerialDouble(EntityManager.getTotalUpdateTime());			
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_TIMING_ENTITIES,  timingEntities), (Player)player);
			PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_ENTITIES, totalTime),      (Player)player);			
		}
		
		else if (maintype == DataReq.LIST_TIMING_HANDLERS){
			ArrayList<StatsTickHandler> timingHandlers = TickHandlerManager.getCumulatedStats();
			SerialDouble totalTime = new SerialDouble(TickHandlerManager.getTotalUpdateTime());
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_TIMING_HANDLERS,  timingHandlers), (Player)player);
			PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_HANDLERS, totalTime),      (Player)player);			
		}
		
		else if (maintype == DataReq.LIST_TIMING_CHUNK){
			ArrayList<StatsChunk> timingChunks = ChunkManager.getTopChunks(100);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK,  timingChunks), (Player)player);
		}

		else if (maintype == DataReq.VALUE_TIMING_WORLDTICK){
			PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_WORLDTICK, new SerialDouble(GlobalTimingManager.getTotalWorldTickStats())), (Player)player);
		}		

		else if (maintype == DataReq.VALUE_TIMING_ENTUPDATE){
			PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_ENTUPDATE, new SerialDouble(GlobalTimingManager.getTotalEntUpdateStats())), (Player)player);
		}				
		
		else if (maintype == DataReq.LIST_AMOUNT_ENTITIES){
			boolean filtered = false;
			if (PlayerTracker.instance().filteredAmount.containsKey(name))
				filtered = PlayerTracker.instance().filteredAmount.get(name);
			
			ArrayList<AmountHolder> ents = EntityManager.getCumulativeEntities(filtered);
			PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_AMOUNT_ENTITIES,  ents), (Player)player);
		}
		
		else if (maintype == DataReq.COMMAND_FILTERING_TRUE){
			PlayerTracker.instance().filteredAmount.put(name, true);
		}

		else if (maintype == DataReq.COMMAND_FILTERING_FALSE){
			PlayerTracker.instance().filteredAmount.put(name, false);
		}		
		
		else if (maintype == DataReq.COMMAND_UNREGISTER){
			PlayerTracker.instance().playerOverlayStatus.remove(player);
			PlayerTracker.instance().playerDimension.remove(player);
		}		

		else if (maintype == DataReq.COMMAND_START){
			MetaManager.reset();	
			modOpis.profilerRun = true;
			ProfilerRegistrar.turnOn();
			OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.STATUS_START, new SerialInt(modOpis.profilerMaxTicks)));			
		}		
		
		else if (maintype == DataReq.COMMAND_TELEPORT_BLOCK){
			EntityManager.teleportPlayer((CoordinatesBlock)param1, (EntityPlayerMP)player);
		}	
		
		else if (maintype == DataReq.COMMAND_TELEPORT_TO_ENTITY){
			EntityManager.teleportEntity((EntityPlayerMP)player, EntityManager.getEntity(((TargetEntity)param1).entityID, ((TargetEntity)param1).dim), player);
		}			
		
		else if (maintype == DataReq.COMMAND_TELEPORT_PULL_ENTITY){
			EntityManager.teleportEntity(EntityManager.getEntity(((TargetEntity)param1).entityID, ((TargetEntity)param1).dim), (EntityPlayerMP)player, player);
		}		
		
		else if (maintype == DataReq.COMMAND_TELEPORT_CHUNK){
			CoordinatesChunk chunkCoord = (CoordinatesChunk)param1;
			World world = DimensionManager.getWorld(chunkCoord.dim);
			if (world == null) return;
			
			CoordinatesBlock blockCoord = new CoordinatesBlock(chunkCoord.dim, chunkCoord.x + 8, world.getTopSolidOrLiquidBlock(chunkCoord.x, chunkCoord.z), chunkCoord.z + 8);
			
			EntityManager.teleportPlayer(blockCoord, (EntityPlayerMP)player);
		}		
				
		
		else if (maintype == DataReq.COMMAND_KILLALL){
			EntityManager.killAll(((SerialString)param1).value);
		}			
		
		else if(maintype == DataReq.COMMAND_UNREGISTER_SWING){
			PlayerTracker.instance().playersSwing.remove(player);
		}
			
		else{
			modOpis.log.log(Level.WARNING, String.format("Unknown data request : %s ", maintype));
		}
	}
	
	public void handleOverlayChunkEntities(CoordinatesChunk coord, Player player){
		
		HashMap<CoordinatesChunk, ArrayList<StatsEntity>> entities = EntityManager.getAllEntitiesPerChunk();
		HashMap<CoordinatesChunk, Integer> perChunk = new HashMap<CoordinatesChunk, Integer>();
		
		for (CoordinatesChunk chunk : entities.keySet())
			perChunk.put(chunk, entities.get(chunk).size());

		PacketDispatcher.sendPacketToPlayer(Packet_DataOverlayChunkEntities.create(perChunk), player);
	}
	
}
