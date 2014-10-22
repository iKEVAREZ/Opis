package mcp.mobius.opis.network.rcon;

import io.nettyopis.buffer.ByteBuf;
import io.nettyopis.channel.ChannelHandlerContext;
import net.minecraftforge.common.util.FakePlayer;
import mcp.mobius.opis.modOpis;
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
	public static BiMap<FakePlayer, ChannelHandlerContext> fakePlayersRcon  = HashBiMap.create();
	public static BiMap<FakePlayer, ChannelHandlerContext> fakePlayersNexus = HashBiMap.create();
	
	static {
		packetTypes.put((byte)0, PacketReqChunks.class);
		packetTypes.put((byte)1, PacketReqData.class);
		packetTypes.put((byte)2, NetDataCommand.class);
		packetTypes.put((byte)3, NetDataList.class);
		packetTypes.put((byte)4, NetDataValue.class);
		packetTypes.put((byte)5, PacketChunks.class);
	}

	public static void sendToAllNexus(PacketBase packet){
		for (FakePlayer player : fakePlayersNexus.keySet())
			sendToPlayerNexus(packet, player);
	}
	
	public static void sendToAllRCon(PacketBase packet){
		for (FakePlayer player : fakePlayersRcon.keySet())
			sendToPlayerRCon(packet, player);
	}	
	
    public static void sendToPlayerNexus(PacketBase packet, FakePlayer player)
    {
    	ChannelHandlerContext ctx  = RConHandler.fakePlayersNexus.get(player);
    	sendToContext(packet, ctx);
    } 		
	
    public static void sendToPlayerRCon(PacketBase packet, FakePlayer player)
    {
    	ChannelHandlerContext ctx  = RConHandler.fakePlayersRcon.get(player);
    	sendToContext(packet, ctx);
    }
    
    public static void sendToContext(PacketBase packet, ChannelHandlerContext ctx){
    	//modOpis.log.info(String.format("%s", packet.msg));
    	
    	ByteArrayDataOutput output = ByteStreams.newDataOutput();    	
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
	
    public static boolean isPriviledge(FakePlayer player){
    	return fakePlayersRcon.containsKey(player) || fakePlayersNexus.containsKey(player);
    }
    
}
