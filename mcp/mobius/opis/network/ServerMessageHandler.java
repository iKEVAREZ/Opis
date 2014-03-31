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
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
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
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.packets.server.Packet_Tickets;

public class ServerMessageHandler {

	private static ServerMessageHandler _instance;
	private ServerMessageHandler(){}
	
	public static ServerMessageHandler instance(){
		if(_instance == null)
			_instance = new ServerMessageHandler();			
		return _instance;
	}	

	public void handle(Message maintype, ISerializable param1, ISerializable param2, Player player){
		String   name  = ((EntityPlayer)player).getEntityName();
		
		if (maintype == Message.OVERLAY_CHUNK_ENTITIES){
			this.handleOverlayChunkEntities((CoordinatesChunk)param1, player);
		}
		
		else if (maintype == Message.OVERLAY_CHUNK_TIMING){
			ArrayList<StatsChunk> timingChunks = ChunkManager.instance().getTopChunks(100);
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_CHUNK,  timingChunks), (Player)player);
		}		
		
		else if (maintype == Message.LIST_CHUNK_TILEENTS){
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_CHUNK_TILEENTS, TileEntityManager.getTileEntitiesInChunk((CoordinatesChunk)param1)), player);
		}		
		
		else if (maintype == Message.LIST_CHUNK_ENTITIES){
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_CHUNK_ENTITIES,  EntityManager.getEntitiesInChunk((CoordinatesChunk)param1)), (Player)player);
		}

		else if (maintype == Message.LIST_CHUNK_LOADED){
			PlayerTracker.instance().playerOverlayStatus.put(player, OverlayStatus.CHUNKSTATUS);
			PlayerTracker.instance().playerDimension.put(player, ((SerialInt)param1).value);
			OpisPacketHandler.validateAndSend(NetDataCommand.create(Message.LIST_CHUNK_LOADED_CLEAR), player);
			OpisPacketHandler.splitAndSend(Message.LIST_CHUNK_LOADED, ChunkManager.instance().getLoadedChunks(((SerialInt)param1).value), player);
		}		

		else if (maintype == Message.LIST_CHUNK_TICKETS){
			PacketDispatcher.sendPacketToPlayer(Packet_Tickets.create(ChunkManager.instance().getTickets()), player);
		}		
		
		else if (maintype == Message.LIST_TIMING_TILEENTS){
			ArrayList<StatsTileEntity>  timingTileEnts = TileEntityManager.getTopEntities(100);
			SerialDouble totalTime = new SerialDouble(TileEntityManager.getTotalUpdateTime());
			OpisPacketHandler.validateAndSend(NetDataList.create (Message.LIST_TIMING_TILEENTS, timingTileEnts), (Player)player);
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_TILEENTS, totalTime),      (Player)player);
		}
		
		else if (maintype == Message.LIST_TIMING_ENTITIES){
			ArrayList<StatsEntity>      timingEntities = EntityManager.getTopEntities(100);
			SerialDouble totalTime = new SerialDouble(EntityManager.getTotalUpdateTime());			
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_ENTITIES,  timingEntities), (Player)player);
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_ENTITIES, totalTime),      (Player)player);			
		}
		
		else if (maintype == Message.LIST_TIMING_HANDLERS){
			ArrayList<StatsTickHandler> timingHandlers = TickHandlerManager.getCumulatedStats();
			SerialDouble totalTime = new SerialDouble(TickHandlerManager.getTotalUpdateTime());
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_HANDLERS,  timingHandlers), (Player)player);
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_HANDLERS, totalTime),      (Player)player);			
		}
		
		else if (maintype == Message.LIST_TIMING_CHUNK){
			ArrayList<StatsChunk> timingChunks = ChunkManager.instance().getTopChunks(100);
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_CHUNK,  timingChunks), (Player)player);
		}

		else if (maintype == Message.VALUE_TIMING_WORLDTICK){
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_WORLDTICK, new SerialDouble(GlobalTimingManager.INSTANCE.getTotalStats(GlobalTimingManager.INSTANCE.worldTickStats))), (Player)player);
		}		

		else if (maintype == Message.VALUE_TIMING_ENTUPDATE){
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_ENTUPDATE, new SerialDouble(GlobalTimingManager.INSTANCE.getTotalStats(GlobalTimingManager.INSTANCE.entUpdateStats))), (Player)player);
		}				
		
		else if (maintype == Message.LIST_AMOUNT_ENTITIES){
			boolean filtered = false;
			if (PlayerTracker.instance().filteredAmount.containsKey(name))
				filtered = PlayerTracker.instance().filteredAmount.get(name);
			
			ArrayList<AmountHolder> ents = EntityManager.getCumulativeEntities(filtered);
			OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_AMOUNT_ENTITIES,  ents), (Player)player);
		}
		
		else if (maintype == Message.COMMAND_FILTERING_TRUE){
			PlayerTracker.instance().filteredAmount.put(name, true);
		}

		else if (maintype == Message.COMMAND_FILTERING_FALSE){
			PlayerTracker.instance().filteredAmount.put(name, false);
		}		
		
		else if (maintype == Message.COMMAND_UNREGISTER){
			PlayerTracker.instance().playerOverlayStatus.remove(player);
			PlayerTracker.instance().playerDimension.remove(player);
		}		

		else if (maintype == Message.COMMAND_START){
			MetaManager.reset();	
			modOpis.profilerRun = true;
			ProfilerRegistrar.turnOn();
			OpisPacketHandler.sendPacketToAllSwing(NetDataValue.create(Message.STATUS_START, new SerialInt(modOpis.profilerMaxTicks)));			
		}		
		
		else if (maintype == Message.COMMAND_TELEPORT_BLOCK){
			EntityManager.teleportPlayer((CoordinatesBlock)param1, (EntityPlayerMP)player);
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.CLIENT_HIGHLIGHT_BLOCK, param1), (Player)player);
		}	
		
		else if (maintype == Message.COMMAND_TELEPORT_TO_ENTITY){
			EntityManager.teleportEntity((EntityPlayerMP)player, EntityManager.getEntity(((TargetEntity)param1).entityID, ((TargetEntity)param1).dim), player);
		}			
		
		else if (maintype == Message.COMMAND_TELEPORT_PULL_ENTITY){
			EntityManager.teleportEntity(EntityManager.getEntity(((TargetEntity)param1).entityID, ((TargetEntity)param1).dim), (EntityPlayerMP)player, player);
		}		
		
		else if (maintype == Message.COMMAND_TELEPORT_CHUNK){
			CoordinatesChunk chunkCoord = (CoordinatesChunk)param1;
			World world = DimensionManager.getWorld(chunkCoord.dim);
			if (world == null) return;
			
			CoordinatesBlock blockCoord = new CoordinatesBlock(chunkCoord.dim, chunkCoord.x + 8, world.getTopSolidOrLiquidBlock(chunkCoord.x, chunkCoord.z), chunkCoord.z + 8);
			
			EntityManager.teleportPlayer(blockCoord, (EntityPlayerMP)player);
		}		
				
		
		else if (maintype == Message.COMMAND_KILLALL){
			EntityManager.killAll(((SerialString)param1).value);
			//this.handle(Message.LIST_AMOUNT_ENTITIES, null, null, player);
		}			
		
		else if(maintype == Message.COMMAND_UNREGISTER_SWING){
			PlayerTracker.instance().playersSwing.remove(player);
		}
			
		else if(maintype == Message.STATUS_TIME_LAST_RUN){	
			OpisPacketHandler.validateAndSend(NetDataValue.create(Message.STATUS_TIME_LAST_RUN,  new SerialLong(ProfilerRegistrar.timeStampLastRun)), (Player)player);
		}
		
		else if(maintype == Message.COMMAND_KILL_HOSTILE_ALL){
			
		}
		
		else if(maintype == Message.COMMAND_KILL_HOSTILE_DIM){
			
		}
		
		else if(maintype == Message.COMMAND_PURGE_CHUNKS_ALL){
			
		}
		
		else if(maintype == Message.COMMAND_PURGE_CHUNKS_DIM){
			
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
