package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import mapwriter.Mw;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.TicketData;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.json.CommandPacket;
import mcp.mobius.opis.network.json.CommandPayload;
import mcp.mobius.opis.network.json.OpisCommand;
//import mcp.mobius.opis.network.client.Packet_ReqChunks;
//import mcp.mobius.opis.network.client.Packet_ReqChunksInDim;
//import mcp.mobius.opis.network.client.Packet_ReqData;
//import mcp.mobius.opis.network.client.Packet_ReqMeanTimeInDim;
//import mcp.mobius.opis.network.client.Packet_ReqTEsInChunk;
//import mcp.mobius.opis.network.client.Packet_ReqTeleport;
//import mcp.mobius.opis.network.client.Packet_ReqTeleportEID;
//import mcp.mobius.opis.network.client.Packet_ReqTickets;
//import mcp.mobius.opis.network.client.Packet_UnregisterPlayer;
import mcp.mobius.opis.network.server.Packet_ChunkTopList;
import mcp.mobius.opis.network.server.Packet_Chunks;
import mcp.mobius.opis.network.server.Packet_ClearSelection;
import mcp.mobius.opis.network.server.Packet_ClientCommand;
import mcp.mobius.opis.network.server.Packet_DataListChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataScreenAmountEntities;
import mcp.mobius.opis.network.server.Packet_DataScreenTimingEntities;
import mcp.mobius.opis.network.server.Packet_DataScreenTimingHandlers;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.network.server.Packet_MeanTime;
import mcp.mobius.opis.network.server.Packet_ModMeanTime;
import mcp.mobius.opis.network.server.Packet_TPS;
import mcp.mobius.opis.network.server.Packet_Tickets;
import mcp.mobius.opis.network.server.Packet_TileEntitiesChunkList;
import mcp.mobius.opis.network.server.Packet_TileEntitiesTopList;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.overlay.OverlayStatus;
import mcp.mobius.opis.overlay.entperchunk.OverlayEntityPerChunk;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet56MapChunks;
import net.minecraft.server.MinecraftServer;
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
			
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT){
				byte header = this.getHeader(packet);
				if (header == -1) return;
				this.onPacketToClient(manager, packet, player, header);
			}

			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
				this.onPacketToServer(manager, packet, player);
        }
	}

	void onPacketToClient(INetworkManager manager, Packet250CustomPayload packet, Player player, Byte header) {
		if (header == Packets.LOADED_CHUNKS){
			Packet_LoadedChunks castedPacket = new Packet_LoadedChunks(packet);
			ChunkManager.chunksLoad = castedPacket.chunkStatus;
		}

		else if (header == Packets.MEANTIME){
			Packet_MeanTime castedPacket = new Packet_MeanTime(packet);
			ChunkManager.chunkMeanTime = castedPacket.chunkStatus;
		}			

		else if (header == Packets.TILEENTITIES_CHUNKLIST){
			Packet_TileEntitiesChunkList castedPacket = new Packet_TileEntitiesChunkList(packet);
			OverlayMeanTime.instance().setupTable(castedPacket.entities);
		}

		else if (header == Packets.TILEENTITIES_TOPLIST){
			Packet_TileEntitiesTopList castedPacket = new Packet_TileEntitiesTopList(packet);
			modOpis.proxy.displayTileEntityList(castedPacket.entities);
		}			
		
		else if (header == Packets.TICKETS){
			Packet_Tickets castedPacket = new Packet_Tickets(packet);
			OverlayLoadedChunks.instance().setupTable(castedPacket.tickets);
		}			

		else if (header == Packets.CHUNKS){
			Packet_Chunks castedPacket = Packet_Chunks.read(packet);
			if (castedPacket != null)
				Mw.instance.chunkManager.forceChunks(castedPacket.chunks);
		}
		
		else if (header == Packets.CHUNKS_TOPLIST){
			Packet_ChunkTopList castedPacket = new Packet_ChunkTopList(packet);
			modOpis.proxy.displayChunkList(castedPacket.chunks);
		}	
		
		else if (header == Packets.MODMEANTIME){
			Packet_ModMeanTime castedPacket = new Packet_ModMeanTime(packet);
			modOpis.proxy.displayModList(castedPacket.modStats);
		}		
		
		else if (header == Packets.TPS){
			Packet_TPS castedPacket = new Packet_TPS(packet);
			
			for(Integer dim : castedPacket.ticktimes.keySet())
				System.out.printf("%s : %.5f ms | %.5f ms\n", dim,
						castedPacket.ticktimes.get(dim).getMean() / 1000.0,
						castedPacket.ticktimes.get(dim).getGeometricMean() / 1000.0);
		}
		
		else if (header == Packets.DATA_OVERLAY_CHUNK_ENTITIES){
			Packet_DataOverlayChunkEntities castedPacket = new Packet_DataOverlayChunkEntities(packet);
			OverlayEntityPerChunk.instance().overlayData = castedPacket.chunkStatus;
			OverlayEntityPerChunk.instance().reduceData();
			OverlayEntityPerChunk.instance().setupChunkTable();
		}
		
		else if (header == Packets.DATA_LIST_CHUNK_ENTITIES){
			Packet_DataListChunkEntities castedPacket = new Packet_DataListChunkEntities(packet);
			OverlayEntityPerChunk.instance().entStats = castedPacket.stats;
			OverlayEntityPerChunk.instance().setupEntTable();
		}		
		
		else if (header == Packets.DATA_SCREEN_TIMING_ENTITIES){
			Packet_DataScreenTimingEntities castedPacket = new Packet_DataScreenTimingEntities(packet);
			modOpis.proxy.displayEntityList(castedPacket.entities);
		}		

		else if (header == Packets.DATA_SCREEN_AMOUNT_ENTITIES){
			Packet_DataScreenAmountEntities castedPacket = new Packet_DataScreenAmountEntities(packet);
			modOpis.proxy.displayEntityAmount(castedPacket.entities);
		}			

		else if (header == Packets.DATA_SCREEN_TIMING_HANDLERS){
			Packet_DataScreenTimingHandlers castedPacket = new Packet_DataScreenTimingHandlers(packet);
			modOpis.proxy.displayHandlerList(castedPacket.stats);
		}					
		
		else if (header == Packets.CLR_SELECTION){
			Packet_ClearSelection castedPacket = new Packet_ClearSelection(packet);
			modOpis.selectedBlock = null;
		}		

		else if (header == Packets.CLIENT_CMD){
			Packet_ClientCommand castedPacket = new Packet_ClientCommand(packet);
			ClientCommandHandler.instance().handle(castedPacket.cmd);
		}		
		
	}

	void onPacketToServer(INetworkManager manager, Packet250CustomPayload packet, Player player) {
		
		CommandPayload commandPayload = CommandPacket.getCommand(packet);
		OpisCommand    command        = commandPayload.getCommand();
		
		switch(command){
		case GET_CHUNKS:
			break;
			
		case GET_CHUNKS_MEAN_TIME:
		{
			Integer dim = (Integer)commandPayload.getParam("dim");
			if (this.isOp(player)){
				modOpis.proxy.playerOverlayStatus.put(player, OverlayStatus.MEANTIME);
				modOpis.proxy.playerDimension.put(player, dim);
				PacketDispatcher.sendPacketToPlayer(Packet_MeanTime.create(TileEntityManager.getTimes(dim), dim), player);
			}			
		}	
			break;
			
		case GET_DATA:
		{
			CoordinatesChunk chunk = (CoordinatesChunk)commandPayload.getParam("chunk");
			String           data  = (String)commandPayload.getParam("dataname");
			
			if (this.isOp(player)){	
				DataReqHandler.instance().handle(chunk, data, player);
			}			
		}
			break;
			
		case GET_LOADED_CHUNKS_IN_DIM:
		{
			Integer dim = (Integer)commandPayload.getParam("dim");
			modOpis.proxy.playerOverlayStatus.put(player, OverlayStatus.CHUNKSTATUS);
			modOpis.proxy.playerDimension.put(player, dim);
			PacketDispatcher.sendPacketToPlayer(Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(dim)), player);
		}
			break;
			
		case GET_LOADED_CHUNKS_TICKETS:
		{
			if (this.isOp(player)){
				PacketDispatcher.sendPacketToPlayer(Packet_Tickets.create(ChunkManager.getTickets()), player);
			}
		}
			break;
			
		case GET_TES_IN_CHUNK:
		{
			CoordinatesChunk chunk = (CoordinatesChunk)commandPayload.getParam("chunk");
			if (this.isOp(player)){				
				PacketDispatcher.sendPacketToPlayer(Packet_TileEntitiesChunkList.create(TileEntityManager.getInChunk(chunk)), player);
			}
		}
			break;
			
		case TELEPORT:
		{
			CoordinatesBlock coord = (CoordinatesBlock)commandPayload.getParam("blockcoord");
			if (this.isOp(player)){		
				EntityManager.teleportPlayer(coord, (EntityPlayerMP)player);
			}			
		}
			break;
			
		case TELEPORT_TO_ENTITY:
		{
			Integer eid = (Integer)commandPayload.getParam("eid");
			Integer dim = (Integer)commandPayload.getParam("dim");
			if (this.isOp(player)){		
				EntityManager.teleportPlayer(eid, dim, (EntityPlayerMP)player);
			}
		}
			break;
			
		case UNREGISTER_USER:
			modOpis.proxy.playerOverlayStatus.remove(player);
			modOpis.proxy.playerDimension.remove(player);			
			break;
			
		default:
			modOpis.log.warning(String.format("Error while processing command packet ! Command : %s", command));
			break;
		
		}
		
		/*
		else if (header == Packets.REQ_CHUNKS){
			Packet_ReqChunks castedPacket = new Packet_ReqChunks(packet);
			if (this.isOp(player)){				
				ArrayList<Chunk> list = new ArrayList();
				World world = DimensionManager.getWorld(castedPacket.dim);
				
				if (world != null){
					for (CoordinatesChunk chunk : castedPacket.chunks)
						list.add(world.getChunkFromChunkCoords(chunk.chunkX, chunk.chunkZ));
					
					if (!list.isEmpty()){
						Packet_Chunks.send(castedPacket.dim, !world.provider.hasNoSky, list, player);
					}
				}
			}
		}
		*/
	}	
	
	
	
	public byte getHeader(Packet250CustomPayload packet){
		try{
			DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));			
			return inputStream.readByte();
		} catch (Exception e){
			return -1;
		}
	}        
	
	public boolean isOp(Player player){
		return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)player).username) || MinecraftServer.getServer().isSinglePlayer();
	}
        
}
