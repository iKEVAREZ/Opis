package mcp.mobius.opis.network;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.logging.Level;

import net.minecraft.entity.player.EntityPlayer;
import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.server.CommandAmountEntities;
import mcp.mobius.opis.data.holders.AmountHolder;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.data.managers.ChunkManager;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.enums.DataReq;
import mcp.mobius.opis.network.server.Packet_DataList;
import mcp.mobius.opis.network.server.Packet_DataOverlayChunkEntities;
import mcp.mobius.opis.server.PlayerTracker;

public class DataReqHandler {

	private static DataReqHandler _instance;
	private DataReqHandler(){}
	
	public static DataReqHandler instance(){
		if(_instance == null)
			_instance = new DataReqHandler();			
		return _instance;
	}	

	public void handle(CoordinatesChunk coord, DataReq maintype, DataReq subtype, DataReq target, Player player){
		String   name  = ((EntityPlayer)player).getEntityName();
		
		if (maintype == DataReq.OVERLAY){
			if (subtype == DataReq.CHUNK){
				if (target == DataReq.ENTITIES){
					this.handleOverlayChunkEntities(coord, player);
					return;
				}
			}
		}
		else if (maintype == DataReq.LIST){
			if (subtype == DataReq.CHUNK){
				if (target == DataReq.ENTITIES){
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.CHUNK, DataReq.ENTITIES,  EntityManager.getEntitiesInChunk(coord)), (Player)player);					
					return;
				}
			}
			
			else if (subtype == DataReq.TIMING){
				
				if (target == DataReq.TILETENTS){
					ArrayList<TileEntityStats>  timingTileEnts = TileEntityManager.getTopEntities(100);
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.TILETENTS, timingTileEnts), (Player)player);	
					return;
				}
				else if (target == DataReq.ENTITIES){
					ArrayList<EntityStats>      timingEntities = EntityManager.getTopEntities(100);
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.ENTITIES,  timingEntities), (Player)player);
					return;
				}
				else if (target == DataReq.HANDLERS){
					ArrayList<TickHandlerStats> timingHandlers = TickHandlerManager.getCumulatedStats();
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.HANDLERS,  timingHandlers), (Player)player);
					return;
				}
				else if (target == DataReq.CHUNK){
					ArrayList<ChunkStats> timingChunks = ChunkManager.getTopChunks(100);
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.TIMING, DataReq.CHUNK,  timingChunks), (Player)player);
					return;
				}				
			}
			
			else if (subtype == DataReq.AMOUNT){
				
				if (target == DataReq.ENTITIES){
					boolean filtered = false;
					if (PlayerTracker.instance().filteredAmount.containsKey(name))
						filtered = PlayerTracker.instance().filteredAmount.get(name);
					
					ArrayList<AmountHolder> ents = EntityManager.getCumulativeEntities(filtered);
					PacketDispatcher.sendPacketToPlayer(Packet_DataList.create(DataReq.LIST, DataReq.AMOUNT, DataReq.ENTITIES,  ents), (Player)player);
					return;
				}
			}
			
		}
		else if (maintype == DataReq.COMMAND){
			if (subtype == DataReq.FILTERING){
				if (target == DataReq.TRUE){
					PlayerTracker.instance().filteredAmount.put(name, true);
					return;
				} else if (target == DataReq.FALSE) {
					PlayerTracker.instance().filteredAmount.put(name, false);
					return;
				}
			}
		}
		
		modOpis.log.log(Level.WARNING, String.format("Unknown data request : %s / %s / %s", maintype, subtype, target));		
	}
	
	public void handleOverlayChunkEntities(CoordinatesChunk coord, Player player){
		
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = EntityManager.getAllEntitiesPerChunk();
		HashMap<CoordinatesChunk, Integer> perChunk = new HashMap<CoordinatesChunk, Integer>();
		
		for (CoordinatesChunk chunk : entities.keySet())
			perChunk.put(chunk, entities.get(chunk).size());

		PacketDispatcher.sendPacketToPlayer(Packet_DataOverlayChunkEntities.create(perChunk), player);
	}
	
}
