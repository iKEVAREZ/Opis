package mcp.mobius.opis.events;

import java.util.ArrayList;
import java.util.HashMap;

import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.newtypes.DataDimension;
import mcp.mobius.opis.data.holders.newtypes.DataPacket;
import mcp.mobius.opis.data.holders.newtypes.DataPacket250;
import mcp.mobius.opis.data.holders.newtypes.DataThread;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.newtypes.NexusData;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.data.profilers.ProfilerPacket;
import mcp.mobius.opis.data.profilers.ProfilerTick;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.rcon.RConHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.relauncher.Side;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;

public enum OpisServerTickHandler{
	INSTANCE;
	
	public long profilerUpdateTickCounter = 0;	
	public int  profilerRunningTicks;
	public EventTimer timer500   = new EventTimer(500);	
	public EventTimer timer1000  = new EventTimer(1000);
	public EventTimer timer2000  = new EventTimer(2000);
	public EventTimer timer5000  = new EventTimer(5000);
	public EventTimer timer10000 = new EventTimer(10000);
	
	public HashMap<EntityPlayerMP, AccessLevel> cachedAccess = new HashMap<EntityPlayerMP, AccessLevel>(); 
	
	@SubscribeEvent
	public void tickEnd(TickEvent.ServerTickEvent event) {

			/*
			if (System.nanoTime() - timer500 >  500000000){
				timer500 = System.nanoTime();
				
				for (Player player : PlayerTracker.instance().playersSwing){
					PacketDispatcher.sendPacketToPlayer(Packet_DataValue.create(DataReq.VALUE_TIMING_TICK,     TickProfiler.instance().stats), player);					
				}
			}
			*/
				
			StringCache.INSTANCE.syncNewCache();		
		
			// One second timer
			if (timer1000.isDone()){

				SerialLong amountUpload   = new SerialLong(((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).dataAmount);
				SerialLong amountDownload = new SerialLong(((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).dataAmount);
				SerialInt  chunkForced    = new SerialInt(ChunkManager.INSTANCE.getForcedChunkAmount());
				SerialInt  chunkLoaded    = new SerialInt(ChunkManager.INSTANCE.getLoadedChunkAmount());
				DataTiming timingTick     = new DataTiming(((ProfilerTick)ProfilerSection.TICK.getProfiler()).data.getGeometricMean());
				SerialInt  amountTileEnts = new SerialInt(TileEntityManager.INSTANCE.getAmountTileEntities());
				SerialInt  amountEntities = new SerialInt(EntityManager.INSTANCE.getAmountEntities());
				SerialInt  amountPlayers  = new SerialInt(FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount());
				
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_UPLOAD,   amountUpload));
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_DOWNLOAD, amountDownload));
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_CHUNK_FORCED,    chunkForced));
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_CHUNK_LOADED,    chunkLoaded));
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_TIMING_TICK,     timingTick));
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_TILEENTS, amountTileEnts));
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.VALUE_AMOUNT_ENTITIES, amountEntities));
				
				RConHandler.sendToAllNexus(new NetDataValue(Message.NEXUS_DATA,
						new NexusData(amountUpload, amountDownload, chunkForced, chunkLoaded, 
								timingTick, amountTileEnts, amountEntities, amountPlayers)
						));
				
				for (EntityPlayerMP player : PlayerTracker.INSTANCE.playersSwing){
					if (!cachedAccess.containsKey(player) || cachedAccess.get(player) != PlayerTracker.INSTANCE.getPlayerAccessLevel(player)){
						PacketManager.validateAndSend(new NetDataValue(Message.STATUS_ACCESS_LEVEL, new SerialInt(PlayerTracker.INSTANCE.getPlayerAccessLevel(player).ordinal())), player);
						cachedAccess.put(player, PlayerTracker.INSTANCE.getPlayerAccessLevel(player));
					}
				}
				
				ArrayList<DataThread> threads = new ArrayList<DataThread>();
				for (Thread t : Thread.getAllStackTraces().keySet()){
					threads.add(new DataThread().fill(t));
				}
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_THREADS, threads));
				
				// Dimension data update.
				ArrayList<DataDimension> dimData = new ArrayList<DataDimension>();
				for (int dim : DimensionManager.getIDs()){
					dimData.add(new DataDimension().fill(dim));
				}
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_DIMENSION_DATA, dimData));

				// Profiler update (if running)
				if (modOpis.profilerRun){
					PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_RUNNING,    new SerialInt(modOpis.profilerMaxTicks)));
					PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_RUN_UPDATE, new SerialInt(profilerRunningTicks)));
				}
				
				((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).dataAmount = 0L;
				((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).dataAmount = 0L;
			}
			
			// Two second timer
			if (timer2000.isDone()){
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PLAYERS, EntityManager.INSTANCE.getAllPlayers()));				
			}
			
			// Five second timer
			if (timer5000.isDone()){
				updatePlayers();
				
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_OUTBOUND, new ArrayList<DataPacket>( ((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).data.values())));
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_INBOUND,  new ArrayList<DataPacket>( ((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).data.values())));
				
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_OUTBOUND_250, new ArrayList<DataPacket250>( ((ProfilerPacket)ProfilerSection.PACKET_OUTBOUND.getProfiler()).data250.values())));
				PacketManager.sendPacketToAllSwing(new NetDataList(Message.LIST_PACKETS_INBOUND_250,  new ArrayList<DataPacket250>( ((ProfilerPacket)ProfilerSection.PACKET_INBOUND.getProfiler()).data250.values())));

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
				
				PacketManager.sendPacketToAllSwing(new NetDataValue(Message.STATUS_STOP, new SerialInt(modOpis.profilerMaxTicks)));
				
				for (EntityPlayerMP player : PlayerTracker.INSTANCE.playersSwing){
					PacketManager.sendFullUpdate(player);
				}
				

				
			}			
	}

	private void updatePlayers(){
		for (EntityPlayerMP player : PlayerTracker.INSTANCE.playerOverlayStatus.keySet()){
			
			if (PlayerTracker.INSTANCE.playerOverlayStatus.get(player) == OverlayStatus.CHUNKSTATUS){
				PacketManager.validateAndSend(new NetDataCommand(Message.LIST_CHUNK_LOADED_CLEAR), player);
				PacketManager.splitAndSend(Message.LIST_CHUNK_LOADED, ChunkManager.INSTANCE.getLoadedChunks(PlayerTracker.INSTANCE.playerDimension.get(player)), player);				
			}
			
			if (PlayerTracker.INSTANCE.playerOverlayStatus.get(player) == OverlayStatus.MEANTIME){
				ArrayList<StatsChunk> timingChunks = ChunkManager.INSTANCE.getTopChunks(100);
				//OpisPacketHandler.validateAndSend(Packet_DataList.create(DataReq.LIST_TIMING_CHUNK, timingChunks), player);
				PacketManager.validateAndSend(new NetDataList(Message.LIST_TIMING_CHUNK, timingChunks), player);
			}
		}
	}
	
}
