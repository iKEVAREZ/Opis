package mcp.mobius.opis.proxy;

import java.awt.Font;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;
import mapwriter.api.MwAPI;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsMod;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.gui.font.Fonts;
import mcp.mobius.opis.gui.font.TrueTypeFont;
import mcp.mobius.opis.gui.overlay.OverlayLoadedChunks;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.gui.screens.ScreenBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.swing.panels.PanelPlayers;

public class ProxyClient extends ProxyServer implements IMessageHandler{
	
	public static TrueTypeFont fontMC8, fontMC12, fontMC16, fontMC18, fontMC24;	
	
	@Override
	public void init(){
		//MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance);
		MwAPI.registerDataProvider("Loaded chunks", OverlayLoadedChunks.instance());
		MwAPI.registerDataProvider("Mean time",     OverlayMeanTime.instance());
		MwAPI.registerDataProvider("Ent per chunk", OverlayEntityPerChunk.instance());
		
		//fontMC12 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 12, false);
		//fontMC24 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 24, false);
		//fontMC8  = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"),  8, true);
		//fontMC12 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 12, true);
		//fontMC16 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 16, true);		
		//fontMC18 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 18, true);		
		//fontMC24 = Fonts.createFont(new ResourceLocation("opis", "fonts/Minecraftia.ttf"), 24, true);
		//fontMC8 = Fonts.loadSystemFont("Monospace", 12, true, Font.TRUETYPE_FONT | Font.BOLD);
		fontMC8 = Fonts.createFont(new ResourceLocation("opis", "fonts/LiberationMono-Bold.ttf"), 14, true);
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_PLAYERS,          SwingUI.instance().getPanelPlayers());
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_AMOUNT_ENTITIES,  SwingUI.instance().getPanelAmountEntities());
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_TIMING_TILEENTS,  SwingUI.instance().getPanelTimingTileEnts());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_TILEENTS, SwingUI.instance().getPanelTimingTileEnts());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_START,          SwingUI.instance().getPanelTimingTileEnts());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_STOP,           SwingUI.instance().getPanelTimingTileEnts());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_RUNNING,        SwingUI.instance().getPanelTimingTileEnts());
		MessageHandlerRegistrar.instance().registerHandler(Message.CLIENT_HIGHLIGHT_BLOCK,SwingUI.instance().getPanelTimingTileEnts());		
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_TIMING_ENTITIES,  SwingUI.instance().getPanelTimingEntities());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_ENTITIES, SwingUI.instance().getPanelTimingEntities());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_START,          SwingUI.instance().getPanelTimingEntities());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_STOP,           SwingUI.instance().getPanelTimingEntities());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_RUNNING,        SwingUI.instance().getPanelTimingEntities());
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_TIMING_HANDLERS,  SwingUI.instance().getPanelTimingHandlers());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_HANDLERS, SwingUI.instance().getPanelTimingHandlers());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_START,          SwingUI.instance().getPanelTimingHandlers());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_STOP,           SwingUI.instance().getPanelTimingHandlers());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_RUNNING,        SwingUI.instance().getPanelTimingHandlers());		
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_TIMING_CHUNK,     SwingUI.instance().getPanelTimingChunks());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_START,          SwingUI.instance().getPanelTimingChunks());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_STOP,           SwingUI.instance().getPanelTimingChunks());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_RUNNING,        SwingUI.instance().getPanelTimingChunks());
		
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_ACCESS_LEVEL,   DataCache.instance());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_CURRENT_TIME,   DataCache.instance());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_ACCESS_LEVEL,   SwingUI.instance());
		MessageHandlerRegistrar.instance().registerHandler(Message.CLIENT_SHOW_SWING,     SwingUI.instance());	
		
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_AMOUNT_TILEENTS,  SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_AMOUNT_ENTITIES,  SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_AMOUNT_HANDLERS,  SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_AMOUNT_UPLOAD,    SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_AMOUNT_DOWNLOAD,  SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_TICK,      SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_CHUNK_FORCED,     SwingUI.instance().getPanelSummary());	
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_CHUNK_LOADED,     SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_TILEENTS,  SwingUI.instance().getPanelSummary());		
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_ENTITIES,  SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_HANDLERS,  SwingUI.instance().getPanelSummary());		
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_WORLDTICK, SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_START,           SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_STOP,            SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_RUN_UPDATE,      SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_RUNNING,         SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.STATUS_TIME_LAST_RUN,   SwingUI.instance().getPanelSummary());
		MessageHandlerRegistrar.instance().registerHandler(Message.VALUE_TIMING_ENTUPDATE, SwingUI.instance().getPanelSummary());	
		
		MessageHandlerRegistrar.instance().registerHandler(Message.CLIENT_CLEAR_SELECTION,  modOpis.proxy);
		MessageHandlerRegistrar.instance().registerHandler(Message.CLIENT_START_PROFILING,  modOpis.proxy);
		MessageHandlerRegistrar.instance().registerHandler(Message.CLIENT_SHOW_RENDER_TICK, modOpis.proxy);
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_CHUNK_ENTITIES, OverlayEntityPerChunk.instance());
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_CHUNK_TILEENTS, OverlayMeanTime.instance());
		
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_TIMING_CHUNK, ChunkManager.instance());
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_CHUNK_LOADED, ChunkManager.instance());
		MessageHandlerRegistrar.instance().registerHandler(Message.LIST_CHUNK_LOADED_CLEAR, ChunkManager.instance());
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		switch(msg){
		case CLIENT_CLEAR_SELECTION:{
			modOpis.selectedBlock = null;
			break;
		}
		case CLIENT_START_PROFILING:{
			modOpis.log.log(Level.INFO, "Started profiling");
			MetaManager.reset();		
			modOpis.profilerRun = true;
			ProfilerRegistrar.turnOn();				
			break;
		}
		case CLIENT_SHOW_RENDER_TICK:{
			modOpis.log.log(Level.INFO, "=== RENDER TICK ===");
			ArrayList<StatsTickHandler> stats = TickHandlerManager.getCumulatedStats();
			for (StatsTickHandler stat : stats){
				System.out.printf("%s \n", stat);
			}			
			break;
		}
		default:
			return false;
		}
		
		
		return true;
	}
}
