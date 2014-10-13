package mcp.mobius.opis.network.rcon.nexus;

import io.netty.bootstrap.Bootstrap;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.ssl.SslContext;
import io.netty.handler.ssl.util.InsecureTrustManagerFactory;
import io.netty.handler.ssl.util.SelfSignedCertificate;
import io.netty.handler.timeout.ReadTimeoutHandler;
import io.netty.handler.timeout.WriteTimeoutHandler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.cert.CertificateException;
import java.util.Properties;

import javax.net.ssl.SSLException;

import net.minecraftforge.common.util.FakePlayer;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.packets.client.PacketReqChunks;
import mcp.mobius.opis.network.packets.client.PacketReqData;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import mcp.mobius.opis.network.packets.server.NetDataList;
import mcp.mobius.opis.network.packets.server.NetDataValue;
import mcp.mobius.opis.network.packets.server.PacketChunks;
import mcp.mobius.opis.network.rcon.server.RConHandshakeDecoder;
import mcp.mobius.opis.network.rcon.server.RConHandshakeHandler;
import mcp.mobius.opis.network.rcon.server.RConServer;

public class NexusClient implements Runnable {

    class ChannelInit extends ChannelInitializer<SocketChannel>{
        private final SslContext sslCtx;
        public ChannelInit(SslContext sslCtx) { this.sslCtx = sslCtx; }

        @Override
        protected void initChannel(SocketChannel ch) throws Exception {
            ch.pipeline().addLast(sslCtx.newHandler(ch.alloc(), NexusClient.instance.host, NexusClient.instance.port));
            ch.pipeline().addLast(new NexusHandshakeDecoder());
            ch.pipeline().addLast(new NexusHandshakeHandler());
            //ch.pipeline().addLast(new JdkZlibDecoder());
            //ch.pipeline().addLast(new JdkZlibEncoder());
        	//ch.pipeline().addLast(new WriteTimeoutHandler(5));
            
        	//ch.pipeline().addLast(new NexusMsgDecoder());
            //ch.pipeline().addLast(new NexusInboundHandler());
        }
    }		
	
	String  reverseprop = "opis.properties";
	
	String  host   = "localhost";
	String  uuid   = "";
	Integer port   = 8013;
	Boolean active = false;
	
	public final static NexusClient instance = new NexusClient();
	
	private NexusClient(){}
	
    @Override
    public void run(){
    	this.readConfig(this.reverseprop);
    	if (!this.active) return;
    	if (this.uuid.equals("")){
    		modOpis.log.error("UUID not set properly.");
    		return;
    	}
    	
    	modOpis.log.info(String.format("Connecting to OpixNexus %s:%s", this.host, this.port));
    	
        SslContext sslCtx = null;
        try {
             sslCtx = SslContext.newClientContext(InsecureTrustManagerFactory.INSTANCE);
        } catch (SSLException e) {
            e.printStackTrace();
        }    	
    	
        EventLoopGroup workerGroup = new NioEventLoopGroup();

        try {
            Bootstrap b = new Bootstrap(); // (1)
            b.group(workerGroup); // (2)
            b.channel(NioSocketChannel.class); // (3)
            b.option(ChannelOption.SO_KEEPALIVE, true); // (4)
            b.handler(new ChannelInit(sslCtx));
            ChannelFuture f = b.connect(this.host, this.port).sync(); // (5)

            // Wait until the connection is closed.
            f.channel().closeFuture().sync();
        } catch (Exception e){
        	modOpis.log.error("Error while connecting to Nexus Server");
			e.printStackTrace();            
        } finally {
            workerGroup.shutdownGracefully();
        }    	
    	
    	
    }   	

    private void readConfig(String filename){
        if (!new File(filename).exists())
            writeConfig(filename);

        Properties prop = new Properties();
        InputStream input = null;

        try {
            input = new FileInputStream(filename);
            prop.load(input);
            input.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        this.host   = prop.getProperty("host");
        this.port   = Integer.valueOf(prop.getProperty("port"));
        this.active = Boolean.valueOf(prop.getProperty("active"));
        this.uuid   = prop.getProperty("uuid");
    }
    
    private void writeConfig(String filename){
        Properties   prop   = new Properties();
        OutputStream output = null;

        try {
            output = new FileOutputStream(filename);

            prop.setProperty("host",   host);
            prop.setProperty("port",   this.port.toString());
            prop.setProperty("active", this.active.toString());
            prop.setProperty("uuid",   this.uuid);
            prop.store(output, null);

            output.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }    
    
}
