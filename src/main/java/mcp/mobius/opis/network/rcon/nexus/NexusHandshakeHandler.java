package mcp.mobius.opis.network.rcon.nexus;

import mcp.mobius.opis.modOpis;
import io.nettyopis.buffer.ByteBuf;
import io.nettyopis.channel.ChannelHandlerContext;
import io.nettyopis.channel.ChannelInboundHandlerAdapter;
import io.nettyopis.handler.ssl.SslHandler;
import io.nettyopis.util.concurrent.Future;
import io.nettyopis.util.concurrent.GenericFutureListener;
import io.nettyopis.channel.Channel;

public class NexusHandshakeHandler extends ChannelInboundHandlerAdapter {
    
	@Override
    public void channelActive(final ChannelHandlerContext ctx) throws Exception {
		modOpis.log.info("SSL encryption activated.");
        ctx.pipeline().remove(NexusHandshakeDecoder.class);
        ctx.pipeline().remove(NexusHandshakeHandler.class);
        ctx.pipeline().addLast(new NexusMsgDecoder());
        ctx.pipeline().addLast(new NexusInboundHandler());
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }

}
