package mcp.mobius.opis.events;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.HashSet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.HashBasedTable;
import com.google.common.collect.Table;
import com.google.common.collect.Table.Cell;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.ChatMessageComponent;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.newtypes.CachedString;
import mcp.mobius.opis.data.holders.newtypes.DataDimension;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import mcp.mobius.opis.data.holders.newtypes.DataThread;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.data.profilers.ProfilerEvent;
import mcp.mobius.opis.data.profilers.ProfilerPacket;
import mcp.mobius.opis.data.profilers.ProfilerTick;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.OpisPacketHandler_OLD;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import cpw.mods.fml.common.ITickHandler;
import cpw.mods.fml.common.TickType;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;

public enum OpisServerTickHandler implements ITickHandler {
	INSTANCE;
	
	public long profilerUpdateTickCounter = 0;	
	public int  profilerRunningTicks;
	public EventTimer timer500   = new EventTimer(500);	
	public EventTimer timer1000  = new EventTimer(1000);
	public EventTimer timer2000  = new EventTimer(2000);
	public EventTimer timer5000  = new EventTimer(5000);
	public EventTimer timer10000 = new EventTimer(10000);
	
	public HashMap<Player, AccessLevel> cachedAccess = new HashMap<Player, AccessLevel>(); 
	
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
				
			// One second timer
			if (timer1000.isDone()){

				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_AMOUNT_UPLOAD,   new SerialLong(((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).dataAmount)));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_AMOUNT_DOWNLOAD, new SerialLong(((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).dataAmount)));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_CHUNK_FORCED,    new SerialInt(ChunkManager.INSTANCE.getForcedChunkAmount())));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_CHUNK_LOADED,    new SerialInt(ChunkManager.INSTANCE.getLoadedChunkAmount())));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_TIMING_TICK,     new DataTiming(((ProfilerTick)ProfilerSection.TICK.getProfiler()).data.getGeometricMean())));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_AMOUNT_TILEENTS, new SerialInt(TileEntityManager.INSTANCE.getAmountTileEntities())));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.VALUE_AMOUNT_ENTITIES, new SerialInt(EntityManager.INSTANCE.getAmountEntities())));
				
				for (Player player : PlayerTracker.instance().playersSwing){
					if (!cachedAccess.containsKey(player) || cachedAccess.get(player) != PlayerTracker.instance().getPlayerAccessLevel(player)){
						OpisPacketHandler_OLD.validateAndSend(NetDataValue.create(Message.STATUS_ACCESS_LEVEL, new SerialInt(PlayerTracker.instance().getPlayerAccessLevel(player).ordinal())), player);
						cachedAccess.put(player, PlayerTracker.instance().getPlayerAccessLevel(player));
					}
				}
				
				ArrayList<DataThread> threads = new ArrayList<DataThread>();
				for (Thread t : Thread.getAllStackTraces().keySet()){
					threads.add(new DataThread().fill(t));
				}
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_THREADS, threads));
				
				// Dimension data update.
				ArrayList<DataDimension> dimData = new ArrayList<DataDimension>();
				for (int dim : DimensionManager.getIDs()){
					dimData.add(new DataDimension().fill(dim));
				}
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_DIMENSION_DATA, dimData));

				// Profiler update (if running)
				if (modOpis.profilerRun){
					OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.STATUS_RUNNING,    new SerialInt(modOpis.profilerMaxTicks)));
					OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.STATUS_RUN_UPDATE, new SerialInt(profilerRunningTicks)));
				}
				
				((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).dataAmount = 0L;
				((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).dataAmount = 0L;
			}
			
			// Two second timer
			if (timer2000.isDone()){
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_PLAYERS, EntityManager.INSTANCE.getAllPlayers()));				
			}
			
			// Five second timer
			if (timer5000.isDone()){
				updatePlayers();
				
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_PACKETS_OUTBOUND, ((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).data));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_PACKETS_INBOUND,  ((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).data));
				
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_PACKETS_OUTBOUND_250, new ArrayList<DataPacket250>( ((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).data250.values())));
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataList.create(Message.LIST_PACKETS_INBOUND_250,  new ArrayList<DataPacket250>( ((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).data250.values())));

				((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).startInterval();
				((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).startInterval();				
				
				/*
				for (DataPacket data : ((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).jabbaSpec){
					System.out.printf("[ %d ] %d %d\n", data.id, data.amount, data.size);
				}
				*/
			}
			
			profilerUpdateTickCounter++;
			
			if (profilerRunningTicks < modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks++;
			}else if (profilerRunningTicks >= modOpis.profilerMaxTicks && modOpis.profilerRun){
				profilerRunningTicks = 0;
				modOpis.profilerRun = false;
				ProfilerSection.desactivateAll(Side.SERVER);
				
				OpisPacketHandler_OLD.sendPacketToAllSwing(NetDataValue.create(Message.STATUS_STOP, new SerialInt(modOpis.profilerMaxTicks)));
				
				for (Player player : PlayerTracker.instance().playersSwing){
					OpisPacketHandler_OLD.sendFullUpdate(player);
				}
				

				
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
			
			if (PlayerTracker.instance().playerOverlayStatus.get(player) == OverlayStatus.CHUNKSTATUS){
				OpisPacketHandler_OLD.validateAndSend(NetDataCommand.create(Message.LIST_CHUNK_LOADED_CLEAR), player);
				OpisPacketHandler_OLD.splitAndSend(Message.LIST_CHUNK_LOADED, ChunkManager.INSTANCE.getLoadedChunks(PlayerTracker.instance().playerDimension.get(player)), player);				
			}
			
			if (PlayerTracker.instance().playerOverlayStatus.get(player) == OverlayStatus.MEANTIME){
				ArrayList<StatsChunk> timingChunks = ChunkManager.INSTANCE.getTopChunks(100);
				//OpisPacketHandler.validateAndSend(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK, timingChunks), player);
				OpisPacketHandler_OLD.validateAndSend(NetDataList.create(Message.LIST_TIMING_CHUNK, timingChunks), player);
			}
		}
	}
	
}
