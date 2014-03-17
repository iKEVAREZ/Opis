package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.server.CommandAmountEntities;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.network.server.Packet_DataListChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.network.server.Packet_DataScreenAmountEntities;

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
			} else if (data[1].equals("amount_ent")){
				if (data[2].equals("filtered")){
					HashMap<String, Integer> ents = EntityManager.getCumulativeEntities(false);
					PacketDispatcher.sendPacketToPlayer(Packet_DataScreenAmountEntities.create(ents), player);
					return;
				} else if (data[2].equals("unfiltered")){
					HashMap<String, Integer> ents = EntityManager.getCumulativeEntities(true);
					PacketDispatcher.sendPacketToPlayer(Packet_DataScreenAmountEntities.create(ents), player);
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
