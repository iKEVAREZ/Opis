package mcp.mobius.opis.network.rconserver;

import java.util.UUID;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.mojang.authlib.GameProfile;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.compression.JdkZlibDecoder;
import io.netty.handler.codec.compression.JdkZlibEncoder;
import io.netty.util.ReferenceCountUtil;

public class RConHandshakeHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        // Here we should check the password against what the client is sending and do all the user registration part
    	// if the password is valid. We should close the socket immediately otherwise.
    	
    	try{
			modOpis.log.info(String.format("Connection to rcon from %s detected", ctx.channel().remoteAddress().toString()));
			
    		if (((String)msg).equals(modOpis.rconpass)){
    			ByteBuf buf = ctx.alloc().buffer();
    			buf.writeBoolean(true);
    			ctx.writeAndFlush(buf);
    			
    			ctx.pipeline().remove(RConHandshakeDecoder.class);
    			ctx.pipeline().remove(RConHandshakeHandler.class);
    			ctx.pipeline().addLast(new JdkZlibDecoder());
    			ctx.pipeline().addLast(new JdkZlibEncoder());
    			ctx.pipeline().addLast(new RConMsgDecoder());
    			ctx.pipeline().addLast(new RConInboundHandler());    			
    			
    		} else {
    			modOpis.log.info(String.format("Password error. Closing socket"));
    			ByteBuf buf = ctx.alloc().buffer();
    			buf.writeBoolean(false);
    			ctx.writeAndFlush(buf);
    			ctx.close();
    		}

        } finally {
            ReferenceCountUtil.release(msg);
        }
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
}
