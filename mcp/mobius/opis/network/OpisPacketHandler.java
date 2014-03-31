package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import mapwriter.Mw;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.GlobalTimingManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.gui.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;
import mcp.mobius.opis.network.packets.client.Packet_ReqChunks;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.network.packets.custom.Packet250Metadata;
import mcp.mobius.opis.network.packets.server.Packet_Chunks;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.packets.server.Packet_Tickets;
import mcp.mobius.opis.swing.SwingUI;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet56MapChunks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.relauncher.Side;

public class OpisPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals("Opis") || packet.channel.equals("Opis_Chunk")) {
			byte header = this.getHeader(packet);
			
			if (header == -1) return;
			
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
				this.onPacketToClient(manager, packet, player, header);

			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
				this.onPacketToServer(manager, packet, player, header);
        }
	}

	void onPacketToClient(INetworkManager manager, Packet250CustomPayload packet, Player player, Byte header) {
		
		if (header == Packets.TICKETS){
			Packet_Tickets castedPacket = new Packet_Tickets(packet);
			OverlayLoadedChunks.instance().setupTable(castedPacket.tickets);
		}			

		else if (header == Packets.CHUNKS){
			Packet_Chunks castedPacket = Packet_Chunks.read(packet);
			if (castedPacket != null)
				Mw.instance.chunkManager.forceChunks(castedPacket.chunks);
		}
		
		else if (header == Packets.DATA_OVERLAY_CHUNK_ENTITIES){
			Packet_DataOverlayChunkEntities castedPacket = new Packet_DataOverlayChunkEntities(packet);
			OverlayEntityPerChunk.instance().overlayData = castedPacket.chunkStatus;
			OverlayEntityPerChunk.instance().reduceData();
			OverlayEntityPerChunk.instance().setupChunkTable();
		}
		
		else if (header == Packets.NETDATACOMMAND){
			NetDataRaw rawdata = new NetDataCommand(packet);
			MessageHandlerRegistrar.INSTANCE.routeMessage(rawdata.msg, rawdata);			
		}		
		
		else if (header == Packets.NETDATALIST){
			NetDataRaw rawdata = new NetDataList(packet);
			MessageHandlerRegistrar.INSTANCE.routeMessage(rawdata.msg, rawdata);
		}		
		
		else if (header == Packets.NETDATAVALUE){
			NetDataRaw rawdata = new NetDataValue(packet);
			MessageHandlerRegistrar.INSTANCE.routeMessage(rawdata.msg, rawdata);
		}
	}

	void onPacketToServer(INetworkManager manager, Packet250CustomPayload packet, Player player, Byte header) {
		if (header == Packets.REQ_CHUNKS){
			Packet_ReqChunks castedPacket = new Packet_ReqChunks(packet);
			if (PlayerTracker.instance().getPlayerAccessLevel(player).ordinal() > AccessLevel.PRIVILEGED.ordinal()){				
				ArrayList<Chunk> list = new ArrayList();
				World world = DimensionManager.getWorld(castedPacket.dim);
				
				if (world != null){
					for (CoordinatesChunk chunk : castedPacket.chunks)
						list.add(world.getChunkFromChunkCoords(chunk.chunkX, chunk.chunkZ));
					
					//if (!list.isEmpty()){
					//	Packet_Chunks.send(castedPacket.dim, !world.provider.hasNoSky, list, player);
					
						
						/*
						Packet250CustomPayload chunkPacket = Packet_Chunks.create(castedPacket.dim, !world.provider.hasNoSky, list);
						if (chunkPacket != null)
							PacketDispatcher.sendPacketToPlayer( chunkPacket, player);
						*/
					//}
				}
			}
		}
		
		else if (header == Packets.REQ_DATA){
			Packet_ReqData castedPacket = new Packet_ReqData(packet);
			
			String logmsg = String.format("Received request %s from player %s ... ", castedPacket.dataReq, ((EntityPlayer)player).username);
			
			if (castedPacket.dataReq.canPlayerUseCommand(player)){
				logmsg += "Accepted";
				ServerMessageHandler.instance().handle(castedPacket.dataReq, castedPacket.param1, castedPacket.param2,  player);
			} else {
				logmsg += "Rejected";
			}
			
			//modOpis.log.log(Level.INFO, logmsg);
		}
	}	
	
	
	
	public byte getHeader(Packet250CustomPayload packet){
		try{
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));			
			return inputStream.readByte();
		} catch (Exception e){
			return -1;
		}
	}        
	
	public static void validateAndSend(NetDataRaw capsule, Player player){
		if (capsule.msg.canPlayerUseCommand(player) && capsule.packet.getPacketSize() < 32000)
			PacketDispatcher.sendPacketToPlayer(capsule.packet, player);
		else if (capsule.packet.getPacketSize() > 32000)
			modOpis.log.warning(String.format("Data packet size of type %s too big : %d ! Dropping !", capsule.msg, capsule.packet.getPacketSize()));
	}

	public static void sendPacketToAllSwing(NetDataRaw capsule){
		for (Player player : PlayerTracker.instance().playersSwing)
			OpisPacketHandler.validateAndSend(capsule, player);
	}

	public static void splitAndSend(Message msg, ArrayList<? extends ISerializable> data, Player player){
		int i = 0;
		while (i < data.size()){
			validateAndSend(NetDataList.create(msg, data.subList(i, Math.min(i + 500, data.size()))), player);
			i += 500;
		}
		
	}
	
	public static void sendChatMsg(String msg, Player player){
		PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(msg)), player);		
	}
	
	public static void sendFullUpdate(Player player){
		ArrayList<StatsTickHandler> timingHandlers = TickHandlerManager.getCumulatedStats();
		ArrayList<StatsEntity>      timingEntities = EntityManager.INSTANCE.getTopEntities(100);
		ArrayList<StatsTileEntity>  timingTileEnts = TileEntityManager.getTopEntities(100);
		ArrayList<StatsChunk>         timingChunks = ChunkManager.instance().getTopChunks(100);
		SerialDouble totalTimeTE      = new SerialDouble(TileEntityManager.getTotalUpdateTime());
		SerialDouble totalTimeEnt     = new SerialDouble(EntityManager.INSTANCE.getTotalUpdateTime());
		SerialDouble totalTimeHandler = new SerialDouble(TickHandlerManager.getTotalUpdateTime());
		SerialDouble totalWorldTick   = new SerialDouble(GlobalTimingManager.INSTANCE.getTotalStats(GlobalTimingManager.INSTANCE.worldTickStats));
		SerialDouble totalEntUpdate   = new SerialDouble(GlobalTimingManager.INSTANCE.getTotalStats(GlobalTimingManager.INSTANCE.entUpdateStats));

		OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_HANDLERS,    timingHandlers),   player);
		OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_ENTITIES,    timingEntities),   player);
		OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_TILEENTS,    timingTileEnts),   player);
		OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_TIMING_CHUNK,       timingChunks),     player);
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_TILEENTS,  totalTimeTE),      player);
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_ENTITIES,  totalTimeEnt),     player);
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_HANDLERS,  totalTimeHandler), player);
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_WORLDTICK, totalWorldTick),   player);		
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_TIMING_ENTUPDATE, totalEntUpdate),   player);					
		
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.VALUE_AMOUNT_HANDLERS, new SerialInt(TickHandlerManager.startStats.size())), player);
		
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.STATUS_TIME_LAST_RUN, new SerialLong(ProfilerRegistrar.timeStampLastRun)), player);
		
		OpisPacketHandler.validateAndSend(NetDataValue.create(Message.STATUS_ACCESS_LEVEL, new SerialInt(PlayerTracker.instance().getPlayerAccessLevel(player).ordinal())), player);
		
		// This portion is to get the proper filtered amounts depending on the player preferences.
		String name = ((EntityPlayer)player).getEntityName();
		boolean filtered = false;
		if (PlayerTracker.instance().filteredAmount.containsKey(name))
			filtered = PlayerTracker.instance().filteredAmount.get(name);
		ArrayList<AmountHolder> amountEntities = EntityManager.INSTANCE.getCumulativeEntities(filtered);

		// Here we send a full update to the player
		//OpisPacketHandler.validateAndSend(Packet_DataList.create(DataReq.LIST_AMOUNT_ENTITIES, amountEntities), player);
		OpisPacketHandler.validateAndSend(NetDataList.create(Message.LIST_AMOUNT_ENTITIES, amountEntities), player);		
		
	}
	
}
