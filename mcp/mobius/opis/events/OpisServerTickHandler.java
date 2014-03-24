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
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.Packet_DataList;
import mcp.mobius.opis.network.packets.server.Packet_DataValue;
import mcp.mobius.opis.network.packets.server.Packet_LoadedChunks;
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

				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_AMOUNT_UPLOAD,   new SerialLong(PacketProfiler.instance().dataSizeOut)));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_AMOUNT_DOWNLOAD, new SerialLong(PacketProfiler.instance().dataSizeIn)));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_CHUNK_FORCED,    new SerialInt(ChunkManager.getForcedChunkAmount())));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_CHUNK_LOADED,    new SerialInt(ChunkManager.getLoadedChunkAmount())));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_TIMING_TICK,     TickProfiler.instance().stats));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_AMOUNT_TILEENTS, new SerialInt(TileEntityManager.getAmountTileEntities())));
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.VALUE_AMOUNT_ENTITIES, new SerialInt(EntityManager.getAmountEntities())));
				
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataList.create(Message.LIST_PLAYERS,           EntityManager.getAllPlayers()));
				
				for (Player player : PlayerTracker.instance().playersSwing){
					OpisPacketHandler.validateAndSend(Packet_DataValue.create(Message.STATUS_ACCESS_LEVEL, new SerialInt(PlayerTracker.instance().getPlayerAccessLevel(player).ordinal())), player);
				}
				
				
				if (modOpis.profilerRun){
					OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.STATUS_RUNNING,   new SerialInt(modOpis.profilerMaxTicks)));
					OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.STATUS_RUN_UPDATE,     new SerialInt(profilerRunningTicks)));
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
				
				OpisPacketHandler.sendPacketToAllSwing(Packet_DataValue.create(Message.STATUS_STOP, new SerialInt(modOpis.profilerMaxTicks)));
				
				for (Player player : PlayerTracker.instance().playersSwing){
					OpisPacketHandler.sendFullUpdate(player);
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
				PacketDispatcher.sendPacketToPlayer( Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(PlayerTracker.instance().playerDimension.get(player))), player);
			}
			
			if (PlayerTracker.instance().playerOverlayStatus.get(player) == OverlayStatus.MEANTIME){
				ArrayList<StatsChunk> timingChunks = ChunkManager.getTopChunks(100);
				//OpisPacketHandler.validateAndSend(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK, timingChunks), player);
				OpisPacketHandler.validateAndSend(Packet_DataList.create(Message.LIST_TIMING_CHUNK, timingChunks), player);
			}
		}
	}
	
}
