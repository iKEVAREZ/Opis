package mcp.mobius.opis.network.rconserver;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

class RConInboundHandler extends ChannelInboundHandlerAdapter{
	// One object of this type is created by connection
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	// This method is called when the connection is created.
        ctx.fireChannelActive();
    }
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	// This method is called when new data is readable in the stream.
    	
        ctx.write(msg);
        ctx.flush();    	
    	
    	/*
        ByteBuf in = (ByteBuf) msg;
        try {
            while (in.isReadable()) { // (1)
                System.out.print((char) in.readByte());
                System.out.flush();
            }
        } finally {
            ReferenceCountUtil.release(msg);
        }
        */   	
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }		
}

