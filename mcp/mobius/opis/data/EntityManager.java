package mcp.mobius.opis.data;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
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
		entName = ent.getClass().getName();

		if (!(stats.containsKey(entID)))
			stats.put(entID, new EntityStats(entID, entName, ent.dimension, ent.posX, ent.posY, ent.posZ));
		stats.get(entID).addMeasure(timing);
	}	

	/* Returns the x slowest entities in all dimensions (with timing data) */
	public static ArrayList<EntityStats> getTopEntities(int quantity){
		ArrayList<EntityStats> sortedEntities = new ArrayList(EntityManager.stats.values());
		ArrayList<EntityStats> topEntities    = new ArrayList<EntityStats>();
		Collections.sort(sortedEntities);
		
		
		for (int i = 0; i < Math.min(quantity, sortedEntities.size()); i++){
			EntityStats testats = sortedEntities.get(i);
			topEntities.add(testats);
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
		
		for (int i = 0; i < world.loadedEntityList.size(); i++){
			Entity ent = (Entity)world.loadedEntityList.get(i);
			entities.add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
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
		
		for (int i = 0; i < world.loadedEntityList.size(); i++){
			Entity ent = (Entity)world.loadedEntityList.get(i);
			CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int)ent.posX, (int)ent.posY, (int)ent.posZ).asCoordinatesChunk();
			
			if (!entities.containsKey(chunk))
				entities.put(chunk, new ArrayList<EntityStats>());
			
			entities.get(chunk).add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
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
	
}
