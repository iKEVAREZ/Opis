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
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.overlay.OverlayStatus;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.network.packet.Packet56MapChunks;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class OpisPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals("Opis")) {
			byte header = this.getHeader(packet);
			
			// CLIENT RECEIVED PACKETS
			if (header == Packets.LOADED_CHUNKS){
				Packet_LoadedChunks castedPacket = new Packet_LoadedChunks(packet);
				ChunkManager.chunksLoad = castedPacket.chunkStatus;
			}

			else if (header == Packets.MEANTIME){
				Packet_MeanTime castedPacket = new Packet_MeanTime(packet);
				ChunkManager.chunkMeanTime = castedPacket.chunkStatus;
			}			

			else if (header == Packets.TILEENTITIES_LIST){
				Packet_TileEntitiesList castedPacket = new Packet_TileEntitiesList(packet);
				OverlayMeanTime.instance().setupTable(castedPacket.entities);
			}

			else if (header == Packets.TICKETS){
				Packet_Tickets castedPacket = new Packet_Tickets(packet);
				OverlayLoadedChunks.instance().setupTable(castedPacket.tickets);
			}			

			else if (header == Packets.CHUNKS){
				Packet_Chunks castedPacket = new Packet_Chunks(packet);
				Mw.instance.chunkManager.forceChunks(castedPacket.chunks);
			}
			
			// SERVER RECEIVED PACKETS
			else if (header == Packets.UNREGISTER_USER){
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
				modOpis.proxy.playerOverlayStatus.put(player, OverlayStatus.MEANTIME);
				modOpis.proxy.playerDimension.put(player, castedPacket.dimension);
				PacketDispatcher.sendPacketToPlayer(Packet_MeanTime.create(TileEntityManager.getTimes(castedPacket.dimension), castedPacket.dimension), player);
			}		
			
			else if (header == Packets.REQ_TES_IN_CHUNK){
				Packet_ReqTEsInChunk castedPacket = new Packet_ReqTEsInChunk(packet);
				PacketDispatcher.sendPacketToPlayer(Packet_TileEntitiesList.create(TileEntityManager.getInChunk(castedPacket.chunk)), player);
			}
			
			else if (header == Packets.REQ_TICKETS){
				Packet_ReqTickets castedPacket = new Packet_ReqTickets(packet);
				PacketDispatcher.sendPacketToPlayer(Packet_Tickets.create(ChunkManager.getTickets()), player);
			}
			
			else if (header == Packets.REQ_CHUNKS){
				Packet_ReqChunks castedPacket = new Packet_ReqChunks(packet);
				ArrayList<Chunk> list = new ArrayList();
				World world = DimensionManager.getWorld(castedPacket.dim);
				
				if (world != null){
					for (CoordinatesChunk chunk : castedPacket.chunks)
						list.add(world.getChunkFromChunkCoords(chunk.chunkX, chunk.chunkZ));
					
					if (!list.isEmpty())
						PacketDispatcher.sendPacketToPlayer( Packet_Chunks.create(castedPacket.dim, !world.provider.hasNoSky, list), player);
						//PacketDispatcher.sendPacketToPlayer( new Packet56MapChunks(list), player);
				}
			}
			
			else if (header == Packets.REQ_TELEPORT){
				Packet_ReqTeleport castedPacket = new Packet_ReqTeleport(packet);
				EntityManager.teleportPlayer(castedPacket.coord, (EntityPlayerMP)player);
			}
			
        }
	}

	public byte getHeader(Packet250CustomPayload packet){
		DataInputStream inputStream = new DataInputStream(new ByteArrayInputStream(packet.data));
		try{
			return inputStream.readByte();
		} catch (IOException e){
			return -1;
		}
	}        
        
}
