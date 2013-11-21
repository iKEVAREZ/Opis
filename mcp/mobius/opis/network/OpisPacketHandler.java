package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import mapwriter.Mw;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.ChunkManager;
import mcp.mobius.opis.data.EntityManager;
import mcp.mobius.opis.data.TileEntityManager;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.TicketData;
import mcp.mobius.opis.network.client.Packet_ReqChunks;
import mcp.mobius.opis.network.client.Packet_ReqChunksInDim;
import mcp.mobius.opis.network.client.Packet_ReqMeanTimeInDim;
import mcp.mobius.opis.network.client.Packet_ReqTEsInChunk;
import mcp.mobius.opis.network.client.Packet_ReqTeleport;
import mcp.mobius.opis.network.client.Packet_ReqTickets;
import mcp.mobius.opis.network.client.Packet_UnregisterPlayer;
import mcp.mobius.opis.network.server.Packet_ChunkTopList;
import mcp.mobius.opis.network.server.Packet_Chunks;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.network.server.Packet_MeanTime;
import mcp.mobius.opis.network.server.Packet_TPS;
import mcp.mobius.opis.network.server.Packet_Tickets;
import mcp.mobius.opis.network.server.Packet_TileEntitiesChunkList;
import mcp.mobius.opis.network.server.Packet_TileEntitiesTopList;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.overlay.OverlayStatus;
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
        if (packet.channel.equals("Opis")) {
			byte header = this.getHeader(packet);
			
			if (header == -1) return;
			
			if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT)
				this.onPacketToClient(manager, packet, player, header);

			if (FMLCommonHandler.instance().getEffectiveSide() == Side.SERVER)
				this.onPacketToServer(manager, packet, player, header);
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
			Packet_Chunks castedPacket = new Packet_Chunks(packet);
			Mw.instance.chunkManager.forceChunks(castedPacket.chunks);
		}
		
		else if (header == Packets.CHUNKS_TOPLIST){
			Packet_ChunkTopList castedPacket = new Packet_ChunkTopList(packet);
			modOpis.proxy.displayChunkList(castedPacket.chunks);
		}	
		
		else if (header == Packets.TPS){
			Packet_TPS castedPacket = new Packet_TPS(packet);
			
			for(Integer dim : castedPacket.ticktimes.keySet())
				System.out.printf("%s : %.5f ms | %.5f ms\n", dim,
						castedPacket.ticktimes.get(dim).getMean() / 1000.0,
						castedPacket.ticktimes.get(dim).getGeometricMean() / 1000.0);
		}
	}

	void onPacketToServer(INetworkManager manager, Packet250CustomPayload packet, Player player, Byte header) {
		if (header == Packets.UNREGISTER_USER){
			Packet_UnregisterPlayer castedPacket = new Packet_UnregisterPlayer(packet);
			modOpis.proxy.playerOverlayStatus.remove(player);
			modOpis.proxy.playerDimension.remove(player);
		}			
		
		else if (header == Packets.REQ_CHUNKS_IN_DIM){
			Packet_ReqChunksInDim castedPacket = new Packet_ReqChunksInDim(packet);
			modOpis.proxy.playerOverlayStatus.put(player, OverlayStatus.CHUNKSTATUS);
			modOpis.proxy.playerDimension.put(player, castedPacket.dimension);
			PacketDispatcher.sendPacketToPlayer(Packet_LoadedChunks.create(ChunkManager.getLoadedChunks(castedPacket.dimension)), player);
		}
	
		else if (header == Packets.REQ_MEANTIME_IN_DIM){
			Packet_ReqMeanTimeInDim castedPacket = new Packet_ReqMeanTimeInDim(packet);
			if (this.isOp(player)){
				modOpis.proxy.playerOverlayStatus.put(player, OverlayStatus.MEANTIME);
				modOpis.proxy.playerDimension.put(player, castedPacket.dimension);
				PacketDispatcher.sendPacketToPlayer(Packet_MeanTime.create(TileEntityManager.getTimes(castedPacket.dimension), castedPacket.dimension), player);
			}
		}		
		
		else if (header == Packets.REQ_TES_IN_CHUNK){
			Packet_ReqTEsInChunk castedPacket = new Packet_ReqTEsInChunk(packet);
			if (this.isOp(player)){				
				PacketDispatcher.sendPacketToPlayer(Packet_TileEntitiesChunkList.create(TileEntityManager.getInChunk(castedPacket.chunk)), player);
			}
		}
		
		else if (header == Packets.REQ_TICKETS){
			Packet_ReqTickets castedPacket = new Packet_ReqTickets(packet);
			if (this.isOp(player)){
				PacketDispatcher.sendPacketToPlayer(Packet_Tickets.create(ChunkManager.getTickets()), player);
			}
		}
		
		else if (header == Packets.REQ_CHUNKS){
			Packet_ReqChunks castedPacket = new Packet_ReqChunks(packet);
			if (this.isOp(player)){				
				ArrayList<Chunk> list = new ArrayList();
				World world = DimensionManager.getWorld(castedPacket.dim);
				
				if (world != null){
					for (CoordinatesChunk chunk : castedPacket.chunks)
						list.add(world.getChunkFromChunkCoords(chunk.chunkX, chunk.chunkZ));
					
					if (!list.isEmpty()){
						Packet250CustomPayload chunkPacket = Packet_Chunks.create(castedPacket.dim, !world.provider.hasNoSky, list);
						if (chunkPacket != null)
							PacketDispatcher.sendPacketToPlayer( chunkPacket, player);
						//PacketDispatcher.sendPacketToPlayer( new Packet56MapChunks(list), player);
					}
				}
			}
		}
		
		else if (header == Packets.REQ_TELEPORT){
			Packet_ReqTeleport castedPacket = new Packet_ReqTeleport(packet);
			if (this.isOp(player)){		
				EntityManager.teleportPlayer(castedPacket.coord, (EntityPlayerMP)player);
			}
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
	
	public boolean isOp(Player player){
		return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)player).username) || MinecraftServer.getServer().isSinglePlayer();
	}
        
}
