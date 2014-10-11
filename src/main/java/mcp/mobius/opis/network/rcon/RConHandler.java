package mcp.mobius.opis.network.rcon;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraftforge.common.util.FakePlayer;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.packets.client.PacketReqChunks;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.packets.server.PacketChunks;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

public class RConHandler {
	public static BiMap<Byte, Class> packetTypes = HashBiMap.create();
	public static BiMap<FakePlayer, ChannelHandlerContext> fakePlayers = HashBiMap.create();
	
	static {
		packetTypes.put((byte)0, PacketReqChunks.class);
		packetTypes.put((byte)1, PacketReqData.class);
		packetTypes.put((byte)2, NetDataCommand.class);
		packetTypes.put((byte)3, NetDataList.class);
		packetTypes.put((byte)4, NetDataValue.class);
		packetTypes.put((byte)5, PacketChunks.class);
	}

    public static void sendToPlayer(PacketBase packet, FakePlayer player)
    {
    	ByteArrayDataOutput output = ByteStreams.newDataOutput();
    	ChannelHandlerContext ctx  = RConHandler.fakePlayers.get(player);
    	
    	packet.encode(output);
    	byte[] data = output.toByteArray();
    	
    	ByteBuf buf = ctx.alloc().buffer(data.length + 4 + 1);
    	buf.writeInt(data.length);
    	buf.writeByte(RConHandler.packetTypes.inverse().get(packet.getClass()));
    	buf.writeBytes(data);
    	ctx.write(buf);
    	ctx.flush();
    	
    	//modOpis.log.info(String.format("%s", packet.msg));
    } 	
	
}
