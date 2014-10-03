package mcp.mobius.opis.network.rconserver;

import java.util.List;

import com.google.common.io.ByteStreams;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.PacketBase;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class RconMsgDecoder extends ByteToMessageDecoder {

    @Override
    protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
        if (in.readableBytes() < 4) { return; }					// Packet starts with the size (int)
        int packetSize = in.getInt(in.readerIndex());           // We read the packet size (not including the packet type byte)

        if (in.readableBytes() < 4 + 1 + packetSize) { return ;}	// We check if there is at least packet type + packetSize available bytes
        in.readInt();
        byte packetType = in.readByte();						    // We read the header

        // Here we should get a list of potential packets and decode for the corresponding byte

        PacketBase packet = (PacketBase) RConServer.instance.packetTypes.get(packetType).newInstance();
        packet.decode(ByteStreams.newDataInput(in.readBytes(packetSize).array()));
        out.add(packet);
    }
}
