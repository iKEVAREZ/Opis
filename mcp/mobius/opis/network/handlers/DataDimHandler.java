package mcp.mobius.opis.network.handlers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.EntityManager;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;

public class DataDimHandler {

	private static DataDimHandler _instance;
	private DataDimHandler(){}
	
	public static DataDimHandler instance(){
		if(_instance == null)
			_instance = new DataDimHandler();			
		return _instance;
	}	
	
	public void handle(int dimension, String datatype, Player player){
		String[] data  = datatype.split(":");
		
		if (data[0].equals("overlay")){
			if (data[1].equals("chunk")){
				if (data[2].equals("entities")){
					this.handleOverlayChunkEntities(dimension, player);
					return;
				}
			}
		}

		modOpis.log.log(Level.WARNING, String.format("Unknown data request : %s", datatype));
	}
	
	public void handleOverlayChunkEntities(int dimension, Player player){
		
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = EntityManager.getEntitiesPerChunkInDim(dimension);
		HashMap<CoordinatesChunk, Integer> perChunk = new HashMap<CoordinatesChunk, Integer>();
		
		for (CoordinatesChunk chunk : entities.keySet())
			perChunk.put(chunk, entities.get(chunk).size());

		PacketDispatcher.sendPacketToPlayer(Packet_DataOverlayChunkEntities.create(perChunk), player);
	}
	
}
