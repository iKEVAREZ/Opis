package mcp.mobius.opis.network.rconserver;

import static cpw.mods.fml.relauncher.Side.SERVER;

import java.util.ArrayList;
import java.util.HashMap;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import com.google.common.io.ByteArrayDataOutput;
import com.google.common.io.ByteStreams;

import cpw.mods.fml.common.network.FMLOutboundHandler;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.common.util.FakePlayer;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.packets.client.PacketReqChunks;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.packets.server.PacketChunks;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.util.ReferenceCountUtil;

public class RConServer implements Runnable {

	class ChannelInit extends ChannelInitializer<SocketChannel>{
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(new RconMsgColater(), new RConInboundHandler());
		}
	}	
	
	public final static RConServer instance = new RConServer();
	public BiMap<FakePlayer, ChannelHandlerContext> fakePlayers = HashBiMap.create();
	public ArrayList<Class> packetTypes = new ArrayList<Class>();
	
	private int port = -1;
	
	private RConServer(){
		this.port = modOpis.rconport;
		
		packetTypes.add(PacketReqChunks.class);
		packetTypes.add(PacketReqData.class);
		packetTypes.add(NetDataCommand.class);
		packetTypes.add(NetDataList.class);
		packetTypes.add(NetDataValue.class);
		packetTypes.add(PacketChunks.class);		
	}
	
    @Override
    public void run(){
    	modOpis.log.info("Starting Opis remote control server.");
    	
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInit())
             .option(ChannelOption.SO_BACKLOG, 128)
             .childOption(ChannelOption.SO_KEEPALIVE, true);

            ChannelFuture f = b.bind(port).sync(); // (7)

            f.channel().closeFuture().sync();
            
        } catch (Exception e){
        	modOpis.log.error("Error in Opis remote control server.");
			e.printStackTrace();
        } finally {
            workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }    
    
    public void sendToPlayer(PacketBase packet, FakePlayer player)
    {
    	ByteArrayDataOutput output = ByteStreams.newDataOutput();
    	ChannelHandlerContext ctx  = RConServer.instance.fakePlayers.get(player);
    	
    	packet.encode(output);
    	byte[] data = output.toByteArray();
    	
    	ByteBuf buf = ctx.alloc().buffer(data.length);
    	buf.writeInt(data.length);
    	buf.writeByte(packetTypes.indexOf(packet.getClass()));
    	buf.writeBytes(data);
    	ctx.write(buf);
    	ctx.flush();
    }    
    
}