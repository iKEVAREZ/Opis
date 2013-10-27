package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Set;
import java.util.logging.Level;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.constants.OverlayStatus;
import mcp.mobius.opis.data.ChunkData;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;

public class OpisPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals("Opis")) {
			byte header = this.getHeader(packet);
			if (header == 0x01){
				Packet0x01ChunkStatus castedPacket = new Packet0x01ChunkStatus(packet);
				OverlayLoadedChunks.instance.chunks = castedPacket.chunkStatus;
			}
			
			else if (header == 0x02){
				Packet0x02RequestChunkStatus castedPacket = new Packet0x02RequestChunkStatus(packet);
				modOpis.proxy.playerOverlayStatus.put(player, OverlayStatus.CHUNKSTATUS);
				modOpis.proxy.playerDimension.put(player, castedPacket.dimension);
				PacketDispatcher.sendPacketToPlayer(Packet0x01ChunkStatus.create(ChunkData.getLoadedChunks(castedPacket.dimension)), player);
			}
		
			else if (header == 0x03){
				modOpis.proxy.playerOverlayStatus.remove(player);
				modOpis.proxy.playerDimension.remove(player);
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
