package mcp.mobius.opis.data.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;

public class EntityManager {

	public static HashMap<Integer, EntityStats> stats = new HashMap<Integer, EntityStats>();
	
	/* Add an entity to the stat array, with timing data */
	public static void addEntity(Entity ent, long timing){
		int entID = ent.entityId;
		String entName;
		entName = getEntityName(ent); //ent.getClass().getName();

		if (!(stats.containsKey(entID)))
			stats.put(entID, new EntityStats(entID, entName, ent.dimension, ent.posX, ent.posY, ent.posZ));
		stats.get(entID).addMeasure(timing);
	}	

	/* Returns the x slowest entities in all dimensions (with timing data) */
	public static ArrayList<EntityStats> getTopEntities(int quantity){
		ArrayList<EntityStats> sortedEntities = new ArrayList(EntityManager.stats.values());
		ArrayList<EntityStats> topEntities    = new ArrayList<EntityStats>();
		Collections.sort(sortedEntities);
		
		int i = 0;
		while (topEntities.size() < Math.min(quantity, sortedEntities.size()) && (i < sortedEntities.size()) ){
		//for (int i = 0; i < Math.min(quantity, sortedEntities.size()); i++){
			EntityStats testats = sortedEntities.get(i);
			
			if (testats.getDataPoints() < 40){
				i++;
				continue;
			}
			
			World world = DimensionManager.getWorld(testats.getCoordinates().dim);
			if (world == null) {
				i++;
				continue;				
			};
			
			Entity entity = world.getEntityByID(testats.getID());
			if (entity == null) {
				i++;
				continue;
			}			
			
			topEntities.add(testats);
			i++;
		}
		
		return topEntities;
	}	
	
	/* Returns all the entities in all dimensions (without timing data) */
	public static ArrayList<EntityStats> getAllEntities(){
		ArrayList<EntityStats> entities    = new ArrayList<EntityStats>();
		for (int i : DimensionManager.getIDs()){
			entities.addAll(EntityManager.getEntitiesInDim(i));
		}
		return entities;
	}
	
	/* Returns all the entities in the given dimension (without timing data) */
	public static ArrayList<EntityStats> getEntitiesInDim(int dim){
		ArrayList<EntityStats> entities    = new ArrayList<EntityStats>();
		
		World world = DimensionManager.getWorld(dim);
		if (world == null) return entities;
		
		ArrayList copyList = new ArrayList(world.loadedEntityList);
		
		for (int i = 0; i < copyList.size(); i++){
			Entity ent = (Entity)copyList.get(i);
			//entities.add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
			entities.add(new EntityStats(ent.entityId, getEntityName(ent), ent.dimension, ent.posX, ent.posY, ent.posZ));
		}
		
		return entities;
	}		
	
	/* Returns a hashmap of all entities per chunk (not timing) */
	public static HashMap<CoordinatesChunk, ArrayList<EntityStats>> getAllEntitiesPerChunk(){
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = new HashMap<CoordinatesChunk, ArrayList<EntityStats>>();
		for (int i : DimensionManager.getIDs()){
			entities.putAll(EntityManager.getEntitiesPerChunkInDim(i));
		}
		return entities;		
	}
	
	/* Returns a hashmap of entities in the given dimension (not timing) */
	public static HashMap<CoordinatesChunk, ArrayList<EntityStats>> getEntitiesPerChunkInDim(int dim){
		HashMap<CoordinatesChunk, ArrayList<EntityStats>> entities = new HashMap<CoordinatesChunk, ArrayList<EntityStats>>();
		World world = DimensionManager.getWorld(dim);
		if (world == null) return entities;
		
		ArrayList copyList = new ArrayList(world.loadedEntityList);
		
		for (int i = 0; i < copyList.size(); i++){
			Entity ent = (Entity)copyList.get(i);
			CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int)ent.posX, (int)ent.posY, (int)ent.posZ).asCoordinatesChunk();
			
			if (!entities.containsKey(chunk))
				entities.put(chunk, new ArrayList<EntityStats>());
			
			//entities.get(chunk).add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
			entities.get(chunk).add(new EntityStats(ent.entityId, getEntityName(ent), ent.dimension, ent.posX, ent.posY, ent.posZ));
		}
		
		return entities;
	}
	
	/* Returns an array of all entities in a given chunk */
	public static ArrayList<EntityStats> getEntitiesInChunk(CoordinatesChunk coord){
		ArrayList<EntityStats> entities = new  ArrayList<EntityStats>();
		
		World world = DimensionManager.getWorld(coord.dim);
		if (world == null) return entities;
		
		ArrayList copyList = new ArrayList(world.loadedEntityList);
		
		for (int i = 0; i < copyList.size(); i++){
			Entity ent = (Entity)copyList.get(i);
			CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int)ent.posX, (int)ent.posY, (int)ent.posZ).asCoordinatesChunk();
			if (chunk.equals(coord))
				//entities.add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
				entities.add(new EntityStats(ent.entityId, getEntityName(ent), ent.dimension, ent.posX, ent.posY, ent.posZ));
		}		
		
		return entities;
	}
	
	/* Returns a hashmap with the entity name and amount of it on the server */
	public static HashMap<String, Integer> getCumulativeEntities(boolean filtered){
		HashMap<String, Integer> entities = new HashMap<String, Integer>();
		
		for (int dim : DimensionManager.getIDs()){
			World world = DimensionManager.getWorld(dim);
			if (world == null) continue;
			
			ArrayList copyList = new ArrayList(world.loadedEntityList);
			
			for (int i = 0; i < copyList.size(); i++){
				Entity ent = (Entity)copyList.get(i);
				//String name = ent.getClass().getName();
				String name = getEntityName(ent, filtered);
				
				if (!entities.containsKey(name))
					entities.put(name, 0);
				
				entities.put(name, entities.get(name) + 1);
			}
		}		
		
		return entities;
	}
	
	public static CoordinatesBlock getTeleportTarget(CoordinatesBlock coord){
		World world = DimensionManager.getWorld(coord.dim);
		if (world == null) {return null;}
		
		int maxOffset       = 16;
		boolean targetFound = false;
		
		if (coord.y > 0){
			for (int xoffset = 0; xoffset <= maxOffset; xoffset++){
				for (int zoffset = 0; zoffset <= maxOffset; zoffset++){
					if ( world.isAirBlock(coord.x + xoffset, coord.y,     coord.z + zoffset) && 
					     world.isAirBlock(coord.x + xoffset, coord.y + 1, coord.z + zoffset) &&
					    !world.isAirBlock(coord.x + xoffset, coord.y - 1, coord.z + zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, coord.y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, coord.y,     coord.z + zoffset) && 
					    world.isAirBlock(coord.x - xoffset, coord.y + 1, coord.z + zoffset) &&
					   !world.isAirBlock(coord.x - xoffset, coord.y - 1, coord.z + zoffset))				
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, coord.y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x + xoffset, coord.y,     coord.z - zoffset) && 
						world.isAirBlock(coord.x + xoffset, coord.y + 1, coord.z - zoffset) &&
					   !world.isAirBlock(coord.x + xoffset, coord.y - 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, coord.y, coord.z - zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, coord.y,     coord.z - zoffset) && 
						world.isAirBlock(coord.x - xoffset, coord.y + 1, coord.z - zoffset) &&
					   !world.isAirBlock(coord.x - xoffset, coord.y - 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, coord.y, coord.z - zoffset);				
				}
			}
		} else {
			int y = 256;
			while (world.isAirBlock(coord.x, y, coord.z) ||
				   world.getBlockId(coord.x, y, coord.z) == Block.vine.blockID
				  )
				y--;
	
			for (int xoffset = 0; xoffset <= maxOffset; xoffset++){
				for (int zoffset = 0; zoffset <= maxOffset; zoffset++){
					if (world.isAirBlock(coord.x + xoffset, y, coord.z + zoffset) && world.isAirBlock(coord.x + xoffset, y + 1, coord.z + zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, y, coord.z + zoffset) && world.isAirBlock(coord.x - xoffset, y + 1, coord.z + zoffset))				
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, y, coord.z + zoffset);
					
					if (world.isAirBlock(coord.x + xoffset, y, coord.z - zoffset) && world.isAirBlock(coord.x + xoffset, y + 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x + xoffset, y, coord.z - zoffset);
					
					if (world.isAirBlock(coord.x - xoffset, y, coord.z - zoffset) && world.isAirBlock(coord.x - xoffset, y + 1, coord.z - zoffset))
						return new CoordinatesBlock(coord.dim, coord.x - xoffset, y, coord.z - zoffset);				
				}
			}	
		}
		
		return null;
	}
	
	public static boolean teleportPlayer(CoordinatesBlock coord, EntityPlayerMP player){
		//System.out.printf("%s %s\n", coord, getTeleportTarget(coord));
		CoordinatesBlock target = EntityManager.getTeleportTarget(coord);
		if (target == null) return false;
		if (player.worldObj.provider.dimensionId != coord.dim) 
			player.travelToDimension(coord.dim);
		
		player.setPositionAndUpdate(target.x + 0.5, target.y, target.z + 0.5);
		
		return true;
	}
	
	public static boolean teleportPlayer(int eid, int dim, EntityPlayerMP player){
		World world = DimensionManager.getWorld(dim);
		if (world == null) return false;
		
		Entity entity = world.getEntityByID(eid);
		if (entity == null) {
			PacketDispatcher.sendPacketToPlayer(
				new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find entity %d in world %d", eid, dim))), (Player)player);
			return false;
		}
		
		if (player.worldObj.provider.dimensionId != dim) 
			player.travelToDimension(dim);
		
		player.setPositionAndUpdate(entity.posX, entity.posY, entity.posZ);
		
		return true;
	}	
	
	public static String getEntityName(Entity ent){
		return getEntityName(ent, false);
	}
	
	public static String getEntityName(Entity ent, boolean filtered){
		if (ent instanceof EntityItem && filtered){
			return "Dropped Item";
		} else if (ent instanceof EntityItem && !filtered){
			try {
				return "[Stack] " + ((EntityItem)ent).getEntityItem().getDisplayName();
			} catch (Exception e) {
				return "<Unknown dropped item>";
			}
		}
			
		
		if (ent instanceof EntityPlayerMP)
			return "Player";
		
		String name = ent.getTranslatedEntityName();
		
		if (name.contains(".")){
			String[] namelst = ent.getClass().getName().split("\\.");
			return namelst[namelst.length - 1];
		}
		
		return name;
	}
	
}
