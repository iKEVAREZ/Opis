package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.server.CommandAmountEntities;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.server.Packet_DataListChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataListTimingEntities;
import mcp.mobius.opis.network.server.Packet_DataListTimingHandlers;
import mcp.mobius.opis.network.server.Packet_DataListTimingTileEnts;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataListAmountEntities;
import mcp.mobius.opis.server.PlayerTracker;

public class DataReqHandler {

	private static DataReqHandler _instance;
	private DataReqHandler(){}
	
	public static DataReqHandler instance(){
		if(_instance == null)
			_instance = new DataReqHandler();			
		return _instance;
	}	

	public void handle(CoordinatesChunk coord, String datatype, Player player){
		String[] data  = datatype.split(":");
		String   name  = ((EntityPlayer)player).getEntityName();
		
		if (data[0].equals("overlay")){
			if (data[1].equals("chunk")){
				if (data[2].equals("entities")){
					this.handleOverlayChunkEntities(coord, player);
					return;
				}
			}
		}
		else if (data[0].equals("list")){
			
			if (data[1].equals("chunk")){
				if (data[2].equals("entities")){
					PacketDispatcher.sendPacketToPlayer(Packet_DataListChunkEntities.create(EntityManager.getEntitiesInChunk(coord)), player);
					return;
				}
			}
			
			else if (data[1].equals("timing")){
				
				if (data[2].equals("tileents")){
					ArrayList<TileEntityStats>  timingTileEnts = TileEntityManager.getTopEntities(100);
					PacketDispatcher.sendPacketToPlayer(Packet_DataListTimingTileEnts.create(timingTileEnts), (Player)player);							
				}
				else if (data[2].equals("ents")){
					ArrayList<EntityStats>      timingEntities = EntityManager.getTopEntities(100);
					PacketDispatcher.sendPacketToPlayer(Packet_DataListTimingEntities.create(timingEntities), (Player)player);					
				}
				else if (data[2].equals("handlers")){
					ArrayList<TickHandlerStats> timingHandlers = TickHandlerManager.getCumulatedStats();
					PacketDispatcher.sendPacketToPlayer(Packet_DataListTimingHandlers.create(timingHandlers), (Player)player);					
				}
			}
			
			else if (data[1].equals("amount_ent")){
				boolean filtered = false;
				if (PlayerTracker.instance().filteredAmount.containsKey(name))
					filtered = PlayerTracker.instance().filteredAmount.get(name);
				
				HashMap<String, Integer> ents = EntityManager.getCumulativeEntities(filtered);
				PacketDispatcher.sendPacketToPlayer(Packet_DataListAmountEntities.create(ents), player);
				return;
			}
			
		}
		else if (data[0].equals("cmd")){
			if (data[1].equals("filtering")){
				if (data[2].equals("true")){
					PlayerTracker.instance().filteredAmount.put(name, true);
					return;
				} else if (data[2].equals("false")) {
					PlayerTracker.instance().filteredAmount.put(name, false);
					return;
				}
			}
		}
		
		
		modOpis.log.log(Level.WARNING, String.format("Unknown data request : %s", datatype));
	}
	
	public void handleOverlayChunkEntities(CoordinatesChunk coord, Player player){
		
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = EntityManager.getAllEntitiesPerChunk();
		HashMap<CoordinatesChunk, Integer> perChunk = new HashMap<CoordinatesChunk, Integer>();
		
		for (CoordinatesChunk chunk : entities.keySet())
			perChunk.put(chunk, entities.get(chunk).size());

		PacketDispatcher.sendPacketToPlayer(Packet_DataOverlayChunkEntities.create(perChunk), player);
	}
	
}
