package mcp.mobius.opis;

import java.util.logging.Logger;

import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.commands.CommandChunkDump;
import mcp.mobius.opis.commands.CommandFrequency;
import mcp.mobius.opis.commands.CommandStart;
import mcp.mobius.opis.commands.CommandStop;
import mcp.mobius.opis.data.TileEntityProfiler;
import mcp.mobius.opis.network.OpisConnectionHandler;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.proxy.ProxyServer;
import mcp.mobius.opis.server.OpisPlayerTracker;
import mcp.mobius.opis.server.OpisServerTickHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.Mod.ServerStarting;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.network.NetworkMod;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.TickRegistry;
import cpw.mods.fml.relauncher.Side;

@Mod(modid="Opis", name="Opis", version="0.0.1")
@NetworkMod(channels={"Opis", "Opis_Chunk"},clientSideRequired=true, serverSideRequired=true, connectionHandler=OpisConnectionHandler.class, packetHandler=OpisPacketHandler.class)

public class modOpis {

	@Instance("Opis")
	public static modOpis instance;	

	public static Logger log = Logger.getLogger("Opis");	

	@SidedProxy(clientSide="mcp.mobius.opis.proxy.ProxyClient", serverSide="mcp.mobius.opis.proxy.ProxyServer")
	public static ProxyServer proxy;		

	public static int profilerDelay   = 1;
	public static boolean profilerRun = false; 
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent event) {}	
	
	@EventHandler
	public void load(FMLInitializationEvent event) {
		TickRegistry.registerTickHandler(new OpisServerTickHandler(), Side.SERVER);		
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent event) {
		proxy.init();
	}	
	
	@EventHandler
	public void serverStarting(FMLServerStartingEvent event){
		event.registerServerCommand(new CommandChunkDump());
		event.registerServerCommand(new CommandFrequency());
		event.registerServerCommand(new CommandStart());
		event.registerServerCommand(new CommandStop());		
		
		GameRegistry.registerPlayerTracker(new OpisPlayerTracker());
		ProfilerRegistrar.registerTileEntityProfiler(new TileEntityProfiler());		
	}	
}
