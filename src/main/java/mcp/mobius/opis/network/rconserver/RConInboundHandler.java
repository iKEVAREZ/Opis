package mcp.mobius.opis.network.rconserver;

import java.util.UUID;

import com.mojang.authlib.GameProfile;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.PacketReqData;
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
        try {
        	FakePlayer fakePlayer = RConServer.instance.fakePlayers.inverse().get(ctx);
        	PacketBase packet     = (PacketBase) msg;
        	
        	if (packet instanceof PacketReqData)
        		modOpis.log.info(String.format("Received request %s from player %s", ((PacketReqData)packet).dataReq, fakePlayer.getDisplayName()));
        	
        	packet.actionServer(null, fakePlayer);
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

