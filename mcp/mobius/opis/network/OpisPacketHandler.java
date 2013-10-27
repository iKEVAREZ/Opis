package mcp.mobius.opis.network;

import java.io.ByteArrayInputStream;
import java.io.DataInputStream;
import java.io.IOException;
import java.util.logging.Level;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.overlay.OverlayLoadedChunks;
import net.minecraft.network.INetworkManager;
import net.minecraft.network.packet.Packet250CustomPayload;
import cpw.mods.fml.common.network.IPacketHandler;
import cpw.mods.fml.common.network.Player;

public class OpisPacketHandler implements IPacketHandler {

	@Override
	public void onPacketData(INetworkManager manager, Packet250CustomPayload packet, Player player) {
        if (packet.channel.equals("Opis")) {
			byte header = this.getHeader(packet);
			if (header == 0x01){
				Packet0x01ChunkStatus castedPacket = new Packet0x01ChunkStatus(packet);
				modOpis.log.log(Level.INFO, String.format("Received a Packet0x01ChunkStatus with %s chunks", castedPacket.chunkStatus.size()));
				OverlayLoadedChunks.chunks = castedPacket.chunkStatus;
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
