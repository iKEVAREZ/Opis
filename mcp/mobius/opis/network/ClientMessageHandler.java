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
import mcp.mobius.opis.api.IMessageHandler;
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
import mcp.mobius.opis.network.packets.server.NetDataRaw;
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

public class ClientMessageHandler implements IMessageHandler{
	
	private static ClientMessageHandler _instance;
	private ClientMessageHandler(){}
	
	public static ClientMessageHandler instance(){
		if(_instance == null)
			_instance = new ClientMessageHandler();			
		return _instance;
	}
	
	public void handle(Message msg, ArrayList<ISerializable> data){
		
		if (msg == Message.LIST_TIMING_CHUNK){
			ChunkManager.setChunkMeanTime(data);
		}
		
		else if (msg == Message.LIST_CHUNK_ENTITIES){
			OverlayEntityPerChunk.instance().setEntStats(data);
			OverlayEntityPerChunk.instance().setupEntTable();		
		}
		else if (msg == Message.LIST_CHUNK_TILEENTS){
			OverlayMeanTime.instance().setupTable(data);	 
		}			     
		
		else if (msg == Message.LIST_CHUNK_LOADED){
			ChunkManager.setLoadedChunks(data);	 
		}			
	}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		return false;
	}
}
