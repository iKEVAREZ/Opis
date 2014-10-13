package mcp.mobius.opis.network.rcon.nexus;

import java.util.List;
import java.util.UUID;

import net.minecraftforge.common.DimensionManager;
import net.minecraftforge.common.util.FakePlayer;
import net.minecraftforge.common.util.FakePlayerFactory;

import com.google.common.io.ByteStreams;
import com.mojang.authlib.GameProfile;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.basetypes.SerialString;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.rcon.RConHandler;
import mcp.mobius.opis.network.rcon.server.RConServer;
import mcp.mobius.opis.swing.SelectedTab;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.codec.ByteToMessageDecoder;

public class NexusInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(final ChannelHandlerContext ctx) {
    	UUID       fakeUUID   = UUID.randomUUID();
    	
		FakePlayer fakePlayer = FakePlayerFactory.get(DimensionManager.getWorld(0), new GameProfile(fakeUUID, ctx.name()));
		RConHandler.fakePlayers.put(fakePlayer, ctx);
		
		PlayerTracker.INSTANCE.playersSwing.add(fakePlayer);
		PlayerTracker.INSTANCE.playerTab.put(fakePlayer, SelectedTab.SUMMARY);
		PacketManager.validateAndSend(new NetDataValue(Message.NEXUS_UUID, new SerialString(NexusClient.instance.uuid)), fakePlayer);
		PacketManager.validateAndSend(new NetDataValue(Message.STATUS_CURRENT_TIME, new SerialLong(System.currentTimeMillis())), fakePlayer);
		StringCache.INSTANCE.syncCache(fakePlayer);    	

		modOpis.log.info(String.format("FakePlayer %s with uuid %s created.", ctx.name(), fakeUUID));     	
    }
	
    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        cause.printStackTrace();
        ctx.close();
    }
	
}
