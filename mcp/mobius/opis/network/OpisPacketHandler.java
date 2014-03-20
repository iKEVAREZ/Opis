package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.ArrayList;

import mapwriter.Mw;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.gui.swing.SwingUI;
import mcp.mobius.opis.network.client.Packet_ReqChunks;
import mcp.mobius.opis.network.client.Packet_ReqData;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_Chunks;
import mcp.mobius.opis.network.server.Packet_ClientCommand;
import mcp.mobius.opis.network.server.Packet_DataList;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataValue;
import mcp.mobius.opis.network.server.Packet_LoadedChunks;
import mcp.mobius.opis.network.server.Packet_MeanTime;
import mcp.mobius.opis.network.server.Packet_ModMeanTime;
import mcp.mobius.opis.network.server.Packet_TPS;
import mcp.mobius.opis.network.server.Packet_Tickets;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.overlay.OverlayMeanTime;
import mcp.mobius.opis.overlay.OverlayStatus;
import mcp.mobius.opis.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.server.PlayerTracker;
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

		else if (header == Packets.TICKETS){
			Packet_Tickets castedPacket = new Packet_Tickets(packet);
			OverlayLoadedChunks.instance().setupTable(castedPacket.tickets);
		}			

		else if (header == Packets.CHUNKS){
			Packet_Chunks castedPacket = Packet_Chunks.read(packet);
			if (castedPacket != null)
				Mw.instance.chunkManager.forceChunks(castedPacket.chunks);
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
		
		else if (header == Packets.CLIENT_CMD){
			Packet_ClientCommand castedPacket = new Packet_ClientCommand(packet);
			ClientCommandHandler.instance().handle(castedPacket.cmd);
		}		
		
		else if (header == Packets.DATA_LIST_GENERAL){
			Packet_DataList castedPacket = new Packet_DataList(packet);

			     if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.TILETENTS))
				DataCache.instance().setTimingTileEnts(castedPacket.data);
			
			else if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.ENTITIES))
				DataCache.instance().setTimingEntities(castedPacket.data);
			
			else if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.HANDLERS))
				DataCache.instance().setTimingHandlers(castedPacket.data);		
			
			else if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.CHUNK))
				DataCache.instance().setTimingChunks(castedPacket.data);
			
			else if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.AMOUNT) && (castedPacket.target == DataReq.ENTITIES))
				DataCache.instance().setAmountEntities(castedPacket.data);			

			else if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.CHUNK)  && (castedPacket.target == DataReq.ENTITIES)){
				OverlayEntityPerChunk.instance().setEntStats(castedPacket.data);
				OverlayEntityPerChunk.instance().setupEntTable();		
			}
			else if ((castedPacket.maintype == DataReq.LIST) && (castedPacket.subtype == DataReq.CHUNK)  && (castedPacket.target == DataReq.TILETENTS)){
				OverlayMeanTime.instance().setupTable(castedPacket.data);	 
			}			     
			
		}
		
		else if (header == Packets.DATA_VALUE_GENERAL){
			Packet_DataValue castedPacket = new Packet_DataValue(packet);
			
			if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.TILETENTS))
				DataCache.instance().setTimingTileEntsTotal(((SerialDouble)castedPacket.data).value);
			
			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.ENTITIES))
				DataCache.instance().setTimingEntitiesTotal(((SerialDouble)castedPacket.data).value);	
			
			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.HANDLERS))
				DataCache.instance().setTimingHandlersTotal(((SerialDouble)castedPacket.data).value);	

			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.WORLDTICK))
				DataCache.instance().setTimingWorldTickTotal(((SerialDouble)castedPacket.data).value);			

			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.ENTUPDATE))
				DataCache.instance().setTimingEntUpdateTotal(((SerialDouble)castedPacket.data).value);						
			
			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.AMOUNT) && (castedPacket.target == DataReq.TILETENTS))
				DataCache.instance().setAmountTileEntsTotal(((SerialInt)castedPacket.data).value);
			
			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.AMOUNT) && (castedPacket.target == DataReq.ENTITIES))
				DataCache.instance().setAmountEntitiesTotal(((SerialInt)castedPacket.data).value);	
			
			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.AMOUNT) && (castedPacket.target == DataReq.HANDLERS))
				DataCache.instance().setAmountHandlersTotal(((SerialInt)castedPacket.data).value);
			
			else if ((castedPacket.maintype == DataReq.VALUE) && (castedPacket.subtype == DataReq.TIMING) && (castedPacket.target == DataReq.TICK))
				DataCache.instance().setTimingTick(castedPacket.data);				
		}
	}

	void onPacketToServer(INetworkManager manager, Packet250CustomPayload packet, Player player, Byte header) {
		if (header == Packets.REQ_CHUNKS){
			Packet_ReqChunks castedPacket = new Packet_ReqChunks(packet);
			if (this.isOp(player)){				
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
			if (this.isOp(player)){	
				DataReqHandler.instance().handle(castedPacket.maintype, castedPacket.subtype, castedPacket.target, castedPacket.param1, castedPacket.param2,  player);
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
