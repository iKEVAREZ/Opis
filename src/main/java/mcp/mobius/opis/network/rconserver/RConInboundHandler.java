package mcp.mobius.opis.network.rconserver;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mcp.mobius.opis.modOpis;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

class RConInboundHandler extends ChannelInboundHandlerAdapter{
	// One object of this type is created by connection
	// There is one permanent context per connection !
	
    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
    	// This method is called when the connection is created.
    	UUID       fakeUUID   = UUID.randomUUID();
    	FakePlayer fakePlayer = FakePlayerFactory.get(DimensionManager.getWorld(0), new GameProfile(fakeUUID, ctx.name()));
    	RConServer.instance.fakePlayers.put(fakePlayer, ctx);
    	
    	modOpis.log.info(String.format("Connection to rcon detected. FakePlayer %s with uuid %s created.", ctx.name(), fakeUUID));
    	
    }
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
    	// This method is called when new data is readable in the stream.

    	System.out.printf("Context : %s\n", ctx);    	
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

