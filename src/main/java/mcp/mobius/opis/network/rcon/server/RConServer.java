package mcp.mobius.opis.network.rcon.server;

import static cpw.mods.fml.relauncher.Side.SERVER;

import java.security.cert.CertificateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Timer;

import javax.net.ssl.SSLException;

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
import mcp.mobius.opis.network.rcon.RConHandler;
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
import io.netty.handler.codec.compression.JdkZlibDecoder;
import io.netty.handler.codec.compression.JdkZlibEncoder;
import io.netty.handler.codec.compression.ZlibCodecFactory;
import io.netty.handler.codec.compression.ZlibDecoder;
import io.netty.handler.codec.compression.ZlibWrapper;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.util.ReferenceCountUtil;

public class RConServer implements Runnable {

	class ChannelInit extends ChannelInitializer<SocketChannel>{
		 private final SslContext sslCtx;
		 
		 public ChannelInit(SslContext sslCtx) {
			 this.sslCtx = sslCtx;
		 }		
		
		@Override
		protected void initChannel(SocketChannel ch) throws Exception {
			
			ch.pipeline().addLast(sslCtx.newHandler(ch.alloc()));
            ch.pipeline().addLast(new ReadTimeoutHandler(10));
            ch.pipeline().addLast(new RConHandshakeDecoder());
            ch.pipeline().addLast(new RConHandshakeHandler());
            //ch.pipeline().addLast(new JdkZlibDecoder());
            //ch.pipeline().addLast(new JdkZlibEncoder());
            //ch.pipeline().addLast(new RconMsgDecoder());
            //ch.pipeline().addLast(new RConInboundHandler());

		}
	}	
	
	public final static RConServer instance = new RConServer();
	
	private int port = -1;
	
	private RConServer(){
		this.port = modOpis.rconport;
	}
	
    @Override
    public void run(){
    	if (!modOpis.rconactive) return;
    	
    	modOpis.log.info("Starting Opis remote control server.");
    	

    	SelfSignedCertificate ssc = null;
    	SslContext sslCtx         = null;
		try {
			ssc    = new SelfSignedCertificate();
			sslCtx = SslContext.newServerContext(ssc.certificate(), ssc.privateKey());
		} catch (CertificateException e1) {	e1.printStackTrace();
		} catch (SSLException e1) {	e1.printStackTrace(); }
    	
        EventLoopGroup bossGroup   = new NioEventLoopGroup();
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        
        try {
            ServerBootstrap b = new ServerBootstrap();
            b.group(bossGroup, workerGroup)
             .channel(NioServerSocketChannel.class)
             .childHandler(new ChannelInit(sslCtx))
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
}
