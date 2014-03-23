package mcp.mobius.opis.data.managers;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;
import mcp.mobius.opis.data.holders.basetypes.AmountHolder;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsPlayer;
import mcp.mobius.opis.helpers.Teleport;

public class EntityManager {

	public static HashMap<Integer, StatsEntity> stats = new HashMap<Integer, StatsEntity>();
	
	/* Add an entity to the stat array, with timing data */
	public static void addEntity(Entity ent, long timing){
		int entID = ent.entityId;
		String entName;
		entName = getEntityName(ent); //ent.getClass().getName();

		if (!(stats.containsKey(entID)))
			stats.put(entID, new StatsEntity(entID, entName, ent.dimension, ent.posX, ent.posY, ent.posZ));
		stats.get(entID).addMeasure(timing);
	}	

	/* Returns the x slowest entities in all dimensions (with timing data) */
	public static ArrayList<StatsEntity> getTopEntities(int quantity){
		ArrayList<StatsEntity> sortedEntities = new ArrayList(EntityManager.stats.values());
		ArrayList<StatsEntity> topEntities    = new ArrayList<StatsEntity>();
		Collections.sort(sortedEntities);
		
		int i = 0;
		while (topEntities.size() < Math.min(quantity, sortedEntities.size()) && (i < sortedEntities.size()) ){
		//for (int i = 0; i < Math.min(quantity, sortedEntities.size()); i++){
			StatsEntity testats = sortedEntities.get(i);
			
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
	public static ArrayList<StatsEntity> getAllEntities(){
		ArrayList<StatsEntity> entities    = new ArrayList<StatsEntity>();
		for (int i : DimensionManager.getIDs()){
			entities.addAll(EntityManager.getEntitiesInDim(i));
		}
		return entities;
	}
	
	/* Returns all the entities in the given dimension (without timing data) */
	public static ArrayList<StatsEntity> getEntitiesInDim(int dim){
		ArrayList<StatsEntity> entities    = new ArrayList<StatsEntity>();
		
		World world = DimensionManager.getWorld(dim);
		if (world == null) return entities;
		
		ArrayList copyList = new ArrayList(world.loadedEntityList);
		
		for (int i = 0; i < copyList.size(); i++){
			Entity ent = (Entity)copyList.get(i);
			//entities.add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
			entities.add(new StatsEntity(ent.entityId, getEntityName(ent), ent.dimension, ent.posX, ent.posY, ent.posZ));
		}
		
		return entities;
	}		
	
	/* Returns a hashmap of all entities per chunk (not timing) */
	public static HashMap<CoordinatesChunk, ArrayList<StatsEntity>> getAllEntitiesPerChunk(){
		HashMap<CoordinatesChunk, ArrayList<StatsEntity>> entities = new HashMap<CoordinatesChunk, ArrayList<StatsEntity>>();
		for (int i : DimensionManager.getIDs()){
			entities.putAll(EntityManager.getEntitiesPerChunkInDim(i));
		}
		return entities;		
	}
	
	/* Returns a hashmap of entities in the given dimension (not timing) */
	public static HashMap<CoordinatesChunk, ArrayList<StatsEntity>> getEntitiesPerChunkInDim(int dim){
		HashMap<CoordinatesChunk, ArrayList<StatsEntity>> entities = new HashMap<CoordinatesChunk, ArrayList<StatsEntity>>();
		World world = DimensionManager.getWorld(dim);
		if (world == null) return entities;
		
		ArrayList copyList = new ArrayList(world.loadedEntityList);
		
		for (int i = 0; i < copyList.size(); i++){
			Entity ent = (Entity)copyList.get(i);
			CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int)ent.posX, (int)ent.posY, (int)ent.posZ).asCoordinatesChunk();
			
			if (!entities.containsKey(chunk))
				entities.put(chunk, new ArrayList<StatsEntity>());
			
			//entities.get(chunk).add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
			entities.get(chunk).add(new StatsEntity(ent.entityId, getEntityName(ent), ent.dimension, ent.posX, ent.posY, ent.posZ));
		}
		
		return entities;
	}
	
	/* Returns an array of all entities in a given chunk */
	public static ArrayList<StatsEntity> getEntitiesInChunk(CoordinatesChunk coord){
		ArrayList<StatsEntity> entities = new  ArrayList<StatsEntity>();
		
		World world = DimensionManager.getWorld(coord.dim);
		if (world == null) return entities;
		
		ArrayList copyList = new ArrayList(world.loadedEntityList);
		
		for (int i = 0; i < copyList.size(); i++){
			Entity ent = (Entity)copyList.get(i);
			CoordinatesChunk chunk = new CoordinatesBlock(ent.dimension, (int)ent.posX, (int)ent.posY, (int)ent.posZ).asCoordinatesChunk();
			if (chunk.equals(coord))
				//entities.add(new EntityStats(ent.entityId, ent.getClass().getName(), ent.dimension, ent.posX, ent.posY, ent.posZ));
				entities.add(new StatsEntity(ent.entityId, getEntityName(ent), ent.dimension, ent.posX, ent.posY, ent.posZ));
		}		
		
		return entities;
	}
	
	/* Returns a hashmap with the entity name and amount of it on the server */
	public static ArrayList<AmountHolder> getCumulativeEntities(boolean filtered){
		ArrayList<AmountHolder>  cumData  = new ArrayList<AmountHolder>();
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
		
		for (String key : entities.keySet())
			cumData.add(new AmountHolder(key, entities.get(key)));
		
		return cumData;
	}
	
	public static boolean teleportPlayer(CoordinatesBlock coord, EntityPlayerMP player){
		//System.out.printf("%s %s\n", coord, getTeleportTarget(coord));
		CoordinatesBlock target = Teleport.instance().getTeleportTarget(coord);
		if (target == null) return false;
		
		target = Teleport.instance().fixNetherTP(target);

		if (target == null) return false;
		
		if (Teleport.instance().movePlayerToDimension(player, coord.dim))
			player.setPositionAndUpdate(target.x + 0.5, target.y, target.z + 0.5);
		else
			return false;
		
		return true;
	}

	public static boolean teleportEntity(Entity src, Entity trg, Player msgtrg){
		if ((src == null) && (msgtrg != null)) {
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find source entity %s", src))), (Player)msgtrg);
			return false;
		}		

		if ((trg == null) && (msgtrg != null)) {
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find target entity %s", trg))), (Player)msgtrg);
			return false;
		}				
		
		if (src instanceof EntityPlayerMP){
			if (Teleport.instance().movePlayerToDimension((EntityPlayerMP)src, trg.worldObj.provider.dimensionId))
				src.setLocationAndAngles(trg.posX, trg.posY, trg.posZ, src.rotationYaw, src.rotationPitch);
			else
				return false;
		}
		else{
			if (Teleport.instance().moveEntityToDimension(src, trg.worldObj.provider.dimensionId))			
				src.setLocationAndAngles(trg.posX, trg.posY, trg.posZ, src.rotationYaw, src.rotationPitch);
			else
				return false;
		}
	
		return true;		
		
	}

	public static Entity getEntity(int eid, int dim){
		World world = DimensionManager.getWorld(dim);
		if (world == null) return null;
		
		Entity entity = world.getEntityByID(eid);
		return entity;
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
	
	public static double getTotalUpdateTime(){
		ArrayList<StatsEntity> entities = new ArrayList(stats.values());
		double updateTime = 0D;
		for (StatsEntity data : entities){
			updateTime += data.getGeometricMean();
		}
		return updateTime;
	}	

	public static int killAll(String entName){
		int nkilled = 0;
		
		if (entName.contains("Player")){
			return -1; //Error msg for when trying to kill a player
		}
			
		
		for (int dim : DimensionManager.getIDs()){
			World world = DimensionManager.getWorld(dim);
			if (world == null) continue;
			
			ArrayList copyList = new ArrayList(world.loadedEntityList);
			
			for (Object o : copyList){
				Entity ent  = (Entity)o;
				String name = EntityManager.getEntityName(ent).toLowerCase(); 
				
				if (name.equals(entName.toLowerCase())){
					ent.setDead();
					nkilled += 1;
				}
			}
		}

		System.out.printf("Killed %d %s\n", nkilled, entName);		
		
		return nkilled;		
	}
	
	public static ArrayList<StatsPlayer> getAllPlayers(){
		List players = MinecraftServer.getServerConfigurationManager(MinecraftServer.getServer()).playerEntityList;
		ArrayList<StatsPlayer> outList = new ArrayList<StatsPlayer>();
		
		for (Object p : players)
			outList.add(new StatsPlayer((EntityPlayer) p));
		
		return outList;
	}
	
}
