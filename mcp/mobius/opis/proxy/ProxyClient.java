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
import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.api.MessageHandlerRegistrar;
import mcp.mobius.opis.api.TabPanelRegistrar;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.holders.newtypes.DataHandler;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
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
import mcp.mobius.opis.swing.panels.PanelAmountEntities;
import mcp.mobius.opis.swing.panels.PanelDimensions;
import mcp.mobius.opis.swing.panels.PanelPlayers;
import mcp.mobius.opis.swing.panels.PanelSummary;
import mcp.mobius.opis.swing.panels.PanelTimingChunks;
import mcp.mobius.opis.swing.panels.PanelTimingEntities;
import mcp.mobius.opis.swing.panels.PanelTimingHandlers;
import mcp.mobius.opis.swing.panels.PanelTimingTileEnts;

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

		IMessageHandler panelSummary        = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelSummary());		
		IMessageHandler panelPlayers        = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelPlayers());
		IMessageHandler panelAmountEntities = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelAmountEntities());
		IMessageHandler panelTimingTileEnts = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelTimingTileEnts());
		IMessageHandler panelTimingEntities = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelTimingEntities());
		IMessageHandler panelTimingHandlers = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelTimingHandlers());
		IMessageHandler panelTimingChunks   = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelTimingChunks());
		IMessageHandler panelDimensions     = (IMessageHandler)TabPanelRegistrar.INSTANCE.registerTab(new PanelDimensions());
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_PLAYERS,          panelPlayers);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_AMOUNT_ENTITIES,  panelAmountEntities);
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_TIMING_TILEENTS,  panelTimingTileEnts);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_TILEENTS, panelTimingTileEnts);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_START,          panelTimingTileEnts);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_STOP,           panelTimingTileEnts);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_RUNNING,        panelTimingTileEnts);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.CLIENT_HIGHLIGHT_BLOCK,panelTimingTileEnts);		
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_TIMING_ENTITIES,  panelTimingEntities);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_ENTITIES, panelTimingEntities);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_START,          panelTimingEntities);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_STOP,           panelTimingEntities);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_RUNNING,        panelTimingEntities);
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_TIMING_HANDLERS,  panelTimingHandlers);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_HANDLERS, panelTimingHandlers);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_START,          panelTimingHandlers);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_STOP,           panelTimingHandlers);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_RUNNING,        panelTimingHandlers);		
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_TIMING_CHUNK,     panelTimingChunks);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_START,          panelTimingChunks);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_STOP,           panelTimingChunks);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_RUNNING,        panelTimingChunks);
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_ACCESS_LEVEL,   DataCache.instance());
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_CURRENT_TIME,   DataCache.instance());
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_ACCESS_LEVEL,   SwingUI.instance());
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.CLIENT_SHOW_SWING,     SwingUI.instance());	
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_AMOUNT_TILEENTS,  panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_AMOUNT_ENTITIES,  panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_AMOUNT_HANDLERS,  panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_AMOUNT_UPLOAD,    panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_AMOUNT_DOWNLOAD,  panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_TICK,      panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_CHUNK_FORCED,     panelSummary);	
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_CHUNK_LOADED,     panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_TILEENTS,  panelSummary);		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_ENTITIES,  panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_HANDLERS,  panelSummary);		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_WORLDTICK, panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_START,           panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_STOP,            panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_RUN_UPDATE,      panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_RUNNING,         panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.STATUS_TIME_LAST_RUN,   panelSummary);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.VALUE_TIMING_ENTUPDATE, panelSummary);	
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_DIMENSION_DATA,    panelDimensions);
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.CLIENT_CLEAR_SELECTION,  modOpis.proxy);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.CLIENT_START_PROFILING,  modOpis.proxy);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.CLIENT_SHOW_RENDER_TICK, modOpis.proxy);
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_CHUNK_ENTITIES, OverlayEntityPerChunk.instance());
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_CHUNK_TILEENTS, OverlayMeanTime.instance());
		
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_TIMING_CHUNK, ChunkManager.INSTANCE);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_CHUNK_LOADED, ChunkManager.INSTANCE);
		MessageHandlerRegistrar.INSTANCE.registerHandler(Message.LIST_CHUNK_LOADED_CLEAR, ChunkManager.INSTANCE);
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
			ProfilerSection.activateAll();
			break;
		}
		case CLIENT_SHOW_RENDER_TICK:{
			modOpis.log.log(Level.INFO, "=== RENDER TICK ===");
			ArrayList<DataHandler> stats = TickHandlerManager.getCumulatedStats();
			for (DataHandler stat : stats){
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
