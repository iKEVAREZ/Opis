package mcp.mobius.opis.network.rcon.nexus;

import mcp.mobius.opis.modOpis;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.ssl.SslHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import io.netty.channel.Channel;

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
