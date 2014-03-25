package mcp.mobius.opis.network;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.logging.Level;

import javax.swing.event.TableModelEvent;
import javax.swing.table.DefaultTableModel;

import cpw.mods.fml.common.network.PacketDispatcher;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.client.DataCache;
import mcp.mobius.opis.data.client.TickHandlerClientProfiler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.events.OpisClientTickHandler;
import mcp.mobius.opis.gui.overlay.OverlayMeanTime;
import mcp.mobius.opis.gui.overlay.entperchunk.OverlayEntityPerChunk;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.client.Packet_ReqData;
import mcp.mobius.opis.swing.SwingUI;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.basetypes.SerialDouble;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.holders.basetypes.SerialLong;
import mcp.mobius.opis.data.holders.basetypes.TicketData;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.enums.Packets;

public class ClientMessageHandler {
	
	private static ClientMessageHandler _instance;
	private ClientMessageHandler(){}
	
	public static ClientMessageHandler instance(){
		if(_instance == null)
			_instance = new ClientMessageHandler();			
		return _instance;
	}
	
	public void handle(Message cmd){
		if (cmd == Message.CLIENT_START_PROFILING){
			modOpis.log.log(Level.INFO, "Started profiling");
			
			MetaManager.reset();		
			modOpis.profilerRun = true;
			ProfilerRegistrar.turnOn();		
		}
		else if (cmd == Message.CLIENT_SHOW_RENDER_TICK){
			modOpis.log.log(Level.INFO, "=== RENDER TICK ===");
			ArrayList<StatsTickHandler> stats = TickHandlerManager.getCumulatedStats();
			for (StatsTickHandler stat : stats){
				System.out.printf("%s \n", stat);
			}
		}
		
		else if (cmd == Message.CLIENT_SHOW_SWING){
			SwingUI.instance().showUI();		
		}
		
		else if (cmd == Message.CLIENT_CLEAR_SELECTION){
			modOpis.selectedBlock = null;			
		}
	}
	
	public void handle(Message msg, ArrayList<ISerializable> data){
		
		if (msg == Message.LIST_TIMING_TILEENTS)
			DataCache.instance().setTimingTileEnts(data);
		
		else if (msg == Message.LIST_TIMING_ENTITIES)
			DataCache.instance().setTimingEntities(data);
		
		else if (msg == Message.LIST_TIMING_HANDLERS)
			DataCache.instance().setTimingHandlers(data);		
		
		else if (msg == Message.LIST_TIMING_CHUNK){
			DataCache.instance().setTimingChunks(data);
			ChunkManager.setChunkMeanTime(data);
		}
		
		else if (msg == Message.LIST_AMOUNT_ENTITIES)
			DataCache.instance().setAmountEntities(data);			
	
		else if (msg == Message.LIST_CHUNK_ENTITIES){
			OverlayEntityPerChunk.instance().setEntStats(data);
			OverlayEntityPerChunk.instance().setupEntTable();		
		}
		else if (msg == Message.LIST_CHUNK_TILEENTS){
			OverlayMeanTime.instance().setupTable(data);	 
		}			     
		
		else if (msg == Message.LIST_PLAYERS){
			DataCache.instance().setListPlayers(data);	 
		}	
		
		else if (msg == Message.LIST_CHUNK_LOADED){
			ChunkManager.setLoadedChunks(data);	 
		}			
	}
	
	public void handle(Message msg, ISerializable data){
		if (msg == Message.VALUE_TIMING_TILEENTS)
			DataCache.instance().setTimingTileEntsTotal(((SerialDouble)data).value);
		
		else if (msg == Message.VALUE_TIMING_ENTITIES)
			DataCache.instance().setTimingEntitiesTotal(((SerialDouble)data).value);	
		
		else if (msg == Message.VALUE_TIMING_HANDLERS)
			DataCache.instance().setTimingHandlersTotal(((SerialDouble)data).value);	
	
		else if (msg == Message.VALUE_TIMING_WORLDTICK)
			DataCache.instance().setTimingWorldTickTotal(((SerialDouble)data).value);			
	
		else if (msg == Message.VALUE_TIMING_ENTUPDATE)
			DataCache.instance().setTimingEntUpdateTotal(((SerialDouble)data).value);						
		
		else if (msg == Message.VALUE_AMOUNT_TILEENTS)
			DataCache.instance().setAmountTileEntsTotal(((SerialInt)data).value);
		
		else if (msg == Message.VALUE_AMOUNT_ENTITIES)
			DataCache.instance().setAmountEntitiesTotal(((SerialInt)data).value);	
		
		else if (msg == Message.VALUE_AMOUNT_HANDLERS)
			DataCache.instance().setAmountHandlersTotal(((SerialInt)data).value);
		
		else if (msg == Message.VALUE_TIMING_TICK)
			DataCache.instance().setTimingTick(data);	
		
		else if (msg == Message.VALUE_AMOUNT_UPLOAD)
			DataCache.instance().setAmountUpload(((SerialLong)data).value);		
		
		else if (msg == Message.VALUE_AMOUNT_DOWNLOAD)
			DataCache.instance().setAmountDownload(((SerialLong)data).value);	
		     
		else if (msg == Message.STATUS_START){
			SwingUI.instance().getBtnTimingChunkRefresh().setText("Running...");
			SwingUI.instance().getBtnSummaryRefresh().setText("Running...");
			SwingUI.instance().getBtnTimingEntRefresh().setText("Running...");
			SwingUI.instance().getBtnTimingHandlerRefresh().setText("Running...");
			SwingUI.instance().getBtnTimingTERefresh().setText("Running...");
			
			SwingUI.instance().getProgBarSummaryOpis().setValue(0);
			SwingUI.instance().getProgBarSummaryOpis().setMinimum(0);
			SwingUI.instance().getProgBarSummaryOpis().setMaximum(((SerialInt)data).value);
		}
		     
		else if (msg == Message.STATUS_STOP){
			SwingUI.instance().getBtnTimingChunkRefresh().setText("Run Opis");
			SwingUI.instance().getBtnSummaryRefresh().setText("Run Opis");
			SwingUI.instance().getBtnTimingEntRefresh().setText("Run Opis");
			SwingUI.instance().getBtnTimingHandlerRefresh().setText("Run Opis");
			SwingUI.instance().getBtnTimingTERefresh().setText("Run Opis");
			
			SwingUI.instance().getProgBarSummaryOpis().setValue(((SerialInt)data).value);
			SwingUI.instance().getProgBarSummaryOpis().setMinimum(0);
			SwingUI.instance().getProgBarSummaryOpis().setMaximum(((SerialInt)data).value);				
		}
		     
		else if (msg == Message.STATUS_RUN_UPDATE){
			SwingUI.instance().getProgBarSummaryOpis().setValue(((SerialInt)data).value);
		}
		     
		else if (msg == Message.STATUS_RUNNING){
			SwingUI.instance().getBtnTimingChunkRefresh().setText("Running...");
			SwingUI.instance().getBtnSummaryRefresh().setText("Running...");
			SwingUI.instance().getBtnTimingEntRefresh().setText("Running...");
			SwingUI.instance().getBtnTimingHandlerRefresh().setText("Running...");
			SwingUI.instance().getBtnTimingTERefresh().setText("Running...");				
			
			SwingUI.instance().getProgBarSummaryOpis().setMaximum(((SerialInt)data).value);
		}			     
	
		else if (msg == Message.STATUS_CURRENT_TIME){
			DataCache.instance().computeClockScrew(((SerialLong)data).value);
		}
	
		else if (msg == Message.STATUS_TIME_LAST_RUN){
			long serverLastRun = ((SerialLong)data).value;
			if (serverLastRun == 0){
				SwingUI.instance().getLblSummaryTimeStampLastRun().setText("Last run : <Never>");
			} else {
				long clientLastRun = serverLastRun + DataCache.instance().getClockScrew();
		        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		        Date resultdate = new Date(clientLastRun);
				
				SwingUI.instance().getLblSummaryTimeStampLastRun().setText(String.format("Last run : %s", sdf.format(resultdate)));
			}
	
		}	
		     
		else if(msg == Message.STATUS_ACCESS_LEVEL){
			DataCache.instance().setAccessLevel( AccessLevel.values()[((SerialInt)data).value] );
		}			     
	
		else if(msg == Message.CLIENT_HIGHLIGHT_BLOCK){
			modOpis.selectedBlock = (CoordinatesBlock)data;
			SwingUI.instance().getBtnTimingTERemoveHighlight().setEnabled(true);
		}
		
		else if(msg == Message.VALUE_CHUNK_FORCED){
			SwingUI.instance().getLblSummaryForcedChunks().setText(String.valueOf(((SerialInt)data).value));
		}
		     
		else if(msg == Message.VALUE_CHUNK_LOADED){
			SwingUI.instance().getLblSummaryLoadedChunks().setText(String.valueOf(((SerialInt)data).value));
		}			
	}
}
