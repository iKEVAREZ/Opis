package mcp.mobius.opis.events;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.ChatMessageComponent;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.GlobalTimingManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.data.server.EntUpdateProfiler;
import mcp.mobius.opis.data.server.PacketProfiler;
import mcp.mobius.opis.data.server.WorldTickProfiler;
import mcp.mobius.opis.data.server.TickProfiler;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_DataList;
import mcp.mobius.opis.network.server.Packet_DataValue;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.overlay.OverlayStatus;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;

public class OpisServerTickHandler implements ITickHandler {

	public long profilerUpdateTickCounter = 0;	
	public long clientUpdateTickCounter = 0;
	public int  profilerRunningTicks;
	public long timer500  = System.nanoTime();	
	public long timer1000 = System.nanoTime();
	
	public static OpisServerTickHandler instance;
	
	public OpisServerTickHandler(){
		instance = this;
	}
	
	@Override
	public void tickStart(EnumSet<TickType> type, Object... tickData) {
	}

	@Override
	public void tickEnd(EnumSet<TickType> type, Object... tickData) {
		if(type.contains(TickType.SERVER)){

			/*
			if (System.nanoTime() - timer500 >  500000000){
				timer500 = System.nanoTime();
				
				for (Player player : PlayerTracker.instance().playersSwing){
					PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_TICK,     TickProfiler.instance().stats), player);					
				}
			}
			*/
				
			if (System.nanoTime() - timer1000 > 1000000000){
				timer1000 = System.nanoTime();

				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_AMOUNT_UPLOAD,   new SerialLong(PacketProfiler.instance().dataSizeOut)));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_AMOUNT_DOWNLOAD, new SerialLong(PacketProfiler.instance().dataSizeIn)));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_TIMING_TICK,     TickProfiler.instance().stats));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataList.create(DataReq.LIST_PLAYERS,           EntityManager.getAllPlayers()));

				if (modOpis.profilerRun){
					OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.STATUS_RUNNING,   new SerialInt(modOpis.profilerMaxTicks)));
					OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.STATUS_RUN_UPDATE,     new SerialInt(profilerRunningTicks)));
				}
				
				PacketProfiler.instance().dataSizeOut = 0L;
				PacketProfiler.instance().dataSizeIn  = 0L;
			}
			
			clientUpdateTickCounter++;
			if (clientUpdateTickCounter % 100 == 0){
				updatePlayers();
				clientUpdateTickCounter = 0;
			}
			
			profilerUpdateTickCounter++;
			
			if (profilerRunningTicks < modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks++;
			}else if (profilerRunningTicks >= modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks = 0;
				modOpis.profilerRun = false;
				ProfilerRegistrar.turnOff();				
				
				// Here we should send a full update to all the clients registered
				
				ArrayList<StatsTickHandler> timingHandlers = TickHandlerManager.getCumulatedStats();
				ArrayList<StatsEntity>      timingEntities = EntityManager.getTopEntities(100);
				ArrayList<StatsTileEntity>  timingTileEnts = TileEntityManager.getTopEntities(100);
				ArrayList<StatsChunk>         timingChunks = ChunkManager.getTopChunks(100);
				SerialDouble totalTimeTE      = new SerialDouble(TileEntityManager.getTotalUpdateTime());
				SerialDouble totalTimeEnt     = new SerialDouble(EntityManager.getTotalUpdateTime());
				SerialDouble totalTimeHandler = new SerialDouble(TickHandlerManager.getTotalUpdateTime());
				SerialDouble totalWorldTick   = new SerialDouble(GlobalTimingManager.getTotalWorldTickStats());
				SerialDouble totalEntUpdate   = new SerialDouble(GlobalTimingManager.getTotalEntUpdateStats());

				OpisPacketHandler.sendPacketToAllSwing(Packet_DataList.create(DataReq.LIST_TIMING_HANDLERS,    timingHandlers));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataList.create(DataReq.LIST_TIMING_ENTITIES,    timingEntities));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataList.create(DataReq.LIST_TIMING_TILEENTS,    timingTileEnts));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK,       timingChunks));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_TIMING_TILEENTS,  totalTimeTE));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_TIMING_ENTITIES,  totalTimeEnt));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_TIMING_HANDLERS,  totalTimeHandler));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_TIMING_WORLDTICK, totalWorldTick));		
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_TIMING_ENTUPDATE, totalEntUpdate));					
				
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_AMOUNT_TILEENTS, new SerialInt(TileEntityManager.stats.size())));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_AMOUNT_ENTITIES, new SerialInt(EntityManager.stats.size())));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.VALUE_AMOUNT_HANDLERS, new SerialInt(TickHandlerManager.startStats.size())));
				
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(DataReq.STATUS_STOP, new SerialInt(modOpis.profilerMaxTicks)));				
				
				
				for (Player player : PlayerTracker.instance().playersSwing){

					// This portion is to get the proper filtered amounts depending on the player preferences.
					String name = ((EntityPlayer)player).getEntityName();
					boolean filtered = false;
					if (PlayerTracker.instance().filteredAmount.containsKey(name))
						filtered = PlayerTracker.instance().filteredAmount.get(name);
					ArrayList<AmountHolder> amountEntities = EntityManager.getCumulativeEntities(filtered);

					// Here we send a full update to the player
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_AMOUNT_ENTITIES,    amountEntities),  player); 					
				}
				
				for (Player player : PlayerTracker.instance().playersOpis)
					PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oOpis automaticly stopped after %d ticks.", modOpis.profilerMaxTicks))), player);

			}			
		}
	}

	@Override
	public EnumSet<TickType> ticks() {
		return EnumSet.of(TickType.SERVER);
	}

	@Override
	public String getLabel() {
		return "opis.server.tickhandler";
	}

	private void updatePlayers(){
		for (Player player : PlayerTracker.instance().playerOverlayStatus.keySet()){
			if (PlayerTracker.instance().playerOverlayStatus.get(player) == OverlayStatus.CHUNKSTATUS)
				PacketDispatcher.sendPacketToPlayer( Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(PlayerTracker.instance().playerDimension.get(player))), player);
			if (PlayerTracker.instance().playerOverlayStatus.get(player) == OverlayStatus.MEANTIME){
				ArrayList<StatsChunk> timingChunks = ChunkManager.getTopChunks(100);
				PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK, timingChunks), player);
			}
		}
	}
	
}
