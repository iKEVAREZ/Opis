package mcp.mobius.opis.network.rconserver;

import java.util.List;

import com.google.common.io.ByteStreams;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.PacketBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RconMsgColater extends ByteToMessageDecoder {

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) { return; }					// Packet starts with the size (int)
        int packetSize = in.readInt();							// We read the packet size (not including the packet type byte)
        if (in.readableBytes() < 1 + packetSize) { return ;}	// We check if there is at least packet type + packetSize available bytes
        byte packetType = in.readByte();						// We read the header
        
        // Here we should get a list of potential packets and decode for the corresponding byte
        
        Class packetClass = RConServer.instance.packetTypes.get(packetType);
        modOpis.log.info(String.format("%d %d %s\n", packetSize, packetType, packetClass));
        
        PacketBase packet = (PacketBase) packetClass.newInstance();
        if (packetSize > 0)
        	packet.decode(ByteStreams.newDataInput(in.readBytes(packetSize).array()));
        out.add(packet);
	}

}
