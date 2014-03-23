package mcp.mobius.opis.helpers;

import java.util.Iterator;

import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.packet.Packet41EntityEffect;
import net.minecraft.network.packet.Packet9Respawn;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.WorldServer;
import net.minecraftforge.common.DimensionManager;

public class Teleport {

	private static Teleport _instance = new Teleport();
	public  static Teleport instance(){return _instance;};
	
	// Originally in EntityPlayerMP
    public boolean movePlayerToDimension(EntityPlayerMP player, int dim)
    {
    	return this.transferPlayerToDimension(player.mcServer.getConfigurationManager(), player, dim);
    }	
	
    // Originally in ServerConfigurationManager
    public boolean transferPlayerToDimension(ServerConfigurationManager manager, EntityPlayerMP player, int targetID)
    {
        int sourceID = player.dimension;
        WorldServer sourceWorld = DimensionManager.getWorld(player.dimension);
        WorldServer targetWorld = DimensionManager.getWorld(targetID);
        
        if (sourceID == targetID) return true;
        if (sourceWorld == null || targetWorld == null) return false;

        player.dimension = targetID;        
        
        player.playerNetServerHandler.sendPacketToPlayer(new Packet9Respawn(player.dimension, (byte)player.worldObj.difficultySetting, targetWorld.getWorldInfo().getTerrainType(), targetWorld.getHeight(), player.theItemInWorldManager.getGameType()));
        sourceWorld.removePlayerEntityDangerously(player);
        player.isDead = false;
        this.transferEntityToWorld(player, sourceID, sourceWorld, targetWorld);
        this.preloadChunk(player, sourceWorld);
        
        player.playerNetServerHandler.setPlayerLocation(player.posX, player.posY, player.posZ, player.rotationYaw, player.rotationPitch);
        player.theItemInWorldManager.setWorld(targetWorld);
        
        manager.updateTimeAndWeatherForPlayer(player, targetWorld);
        manager.syncPlayerInventory(player);
        Iterator iterator = player.getActivePotionEffects().iterator();

        while (iterator.hasNext())
        {
            PotionEffect potioneffect = (PotionEffect)iterator.next();
            player.playerNetServerHandler.sendPacketToPlayer(new Packet41EntityEffect(player.entityId, potioneffect));
        }

        GameRegistry.onPlayerChangedDimension(player);
        return true;
    }    
   
    // Originally in ServerConfigurationManager    
    public void preloadChunk(EntityPlayerMP player, WorldServer sourceWorld)
    {
        WorldServer otherWorld = player.getServerForPlayer();

        if (sourceWorld != null)
        {
            sourceWorld.getPlayerManager().removePlayer(player);
        }

        otherWorld.getPlayerManager().addPlayer(player);
        otherWorld.theChunkProviderServer.loadChunk((int)player.posX >> 4, (int)player.posZ >> 4);
    }    

    public void transferEntityToWorld(Entity ent, int dim, WorldServer srcWorld, WorldServer trgWorld)
    {
        WorldProvider pOld = srcWorld.provider;
        WorldProvider pNew = trgWorld.provider;
        double moveFactor = pOld.getMovementFactor() / pNew.getMovementFactor();
        double d0 = ent.posX * moveFactor;
        double d1 = ent.posZ * moveFactor;
        double d3 = ent.posX;
        double d4 = ent.posY;
        double d5 = ent.posZ;
        float f = ent.rotationYaw;

        srcWorld.theProfiler.startSection("placing");
        d0 = (double)MathHelper.clamp_int((int)d0, -29999872, 29999872);
        d1 = (double)MathHelper.clamp_int((int)d1, -29999872, 29999872);

        if (ent.isEntityAlive())
        {
            trgWorld.spawnEntityInWorld(ent);
            ent.setLocationAndAngles(d0, ent.posY, d1, ent.rotationYaw, ent.rotationPitch);
            trgWorld.updateEntityWithOptionalForce(ent, false);
        }

        ent.setWorld(trgWorld);
    }    
    
    public boolean moveEntityToDimension(Entity target, int targetID)
    {
        if (!target.worldObj.isRemote && !target.isDead)
        {
            MinecraftServer server = MinecraftServer.getServer();
            int sourceID = target.dimension;
            WorldServer sourceWorld = DimensionManager.getWorld(sourceID);
            WorldServer targetWorld = DimensionManager.getWorld(targetID);

            if (sourceID == targetID) return true;
            if (sourceWorld == null || targetWorld == null) return false;                
            
            target.dimension = targetID;
            
            target.worldObj.removeEntity(target);
            target.isDead = false;
            this.transferEntityToWorld(target, sourceID, sourceWorld, targetWorld);
            Entity entity = EntityList.createEntityByName(EntityList.getEntityString(target), targetWorld);

            if (entity != null)
            {
                entity.copyDataFrom(target, true);
                targetWorld.spawnEntityInWorld(entity);
            }

            target.isDead = true;
            sourceWorld.resetUpdateEntityTick();
            targetWorld.resetUpdateEntityTick();
        }
        
        return true;
    }    
    
}
