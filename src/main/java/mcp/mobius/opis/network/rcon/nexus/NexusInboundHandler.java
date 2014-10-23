package mcp.mobius.opis.network.rcon.nexus;

import java.lang.ref.WeakReference;
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
import mcp.mobius.opis.data.holders.newtypes.NexusAuth;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.rcon.RConHandler;
import mcp.mobius.opis.network.rcon.server.RConServer;
import mcp.mobius.opis.swing.SelectedTab;
import io.nettyopis.buffer.ByteBuf;
import io.nettyopis.channel.ChannelHandlerContext;
import io.nettyopis.channel.ChannelInboundHandlerAdapter;
import io.nettyopis.handler.codec.ByteToMessageDecoder;
import io.nettyopis.util.ReferenceCountUtil;

public class NexusInboundHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void handlerAdded(final ChannelHandlerContext ctx) {
    	NexusClient.instance.ctx = new WeakReference<ChannelHandlerContext>(ctx);
    	
    	UUID       fakeUUID   = UUID.randomUUID();
    	
		FakePlayer fakePlayer = FakePlayerFactory.get(DimensionManager.getWorld(0), new GameProfile(fakeUUID, ctx.name()));
		RConHandler.fakePlayersNexus.put(fakePlayer, ctx);
		
		PlayerTracker.INSTANCE.playersSwing.add(fakePlayer);
		PlayerTracker.INSTANCE.playerTab.put(fakePlayer, SelectedTab.ALL);
		RConHandler.sendToPlayerNexus(new NetDataValue(Message.NEXUS_UUID, new NexusAuth(NexusClient.instance.uuid, NexusClient.instance.pass, NexusClient.instance.reconnect)), fakePlayer);
		RConHandler.sendToPlayerNexus(new NetDataValue(Message.STATUS_CURRENT_TIME, new SerialLong(System.currentTimeMillis())), fakePlayer);
		StringCache.INSTANCE.syncCache(fakePlayer);    	

		modOpis.log.info(String.format("FakePlayer %s with uuid %s created.", ctx.name(), fakeUUID));     	
    }
	
    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) {
        try {
        	FakePlayer fakePlayer = RConHandler.fakePlayersNexus.inverse().get(ctx);
        	PacketBase packet     = (PacketBase) msg;
        	packet.actionServer(null, fakePlayer);
        } finally {
            ReferenceCountUtil.release(msg);
        }    	
    }    
    
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
    	RConHandler.exceptionCaught(ctx, cause);
    }
}
