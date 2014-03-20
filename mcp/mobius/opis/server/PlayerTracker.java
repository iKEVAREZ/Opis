package mcp.mobius.opis.server;

import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.overlay.OverlayStatus;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraftforge.common.Configuration;
import cpw.mods.fml.common.IPlayerTracker;
import cpw.mods.fml.common.network.Player;

public class PlayerTracker implements IPlayerTracker{
	private static PlayerTracker _instance  = new PlayerTracker();
	public  static PlayerTracker instance() {return _instance;} 
	private PlayerTracker(){}
	
	public HashSet<EntityPlayer> playersSwing = new HashSet<EntityPlayer>();		 //This is the list of players who have opened the UI
	public HashSet<EntityPlayer> playersOpis  = new HashSet<EntityPlayer>();		 //This is the list of players who have opened the UI or used the command line
	public HashMap<String, Boolean> filteredAmount = new HashMap<String, Boolean>(); //Should the entity amount be filtered or not
	public HashMap<Player, OverlayStatus> playerOverlayStatus = new HashMap<Player, OverlayStatus>();
	public HashMap<Player, Integer>       playerDimension     = new HashMap<Player, Integer>();
	private HashSet<String> playerPrivileged = new HashSet<String>();
	
	public void addPrivilegedPlayer(String name, boolean save){
		this.playerPrivileged.add(name);
		if (save){
			modOpis.instance.config.get(Configuration.CATEGORY_GENERAL, "privileged", new String[]{}).set(playerPrivileged.toArray(new String[]{}));
			modOpis.instance.config.save();
		}
	}
	
	public void addPrivilegedPlayer(String name){
		this.addPrivilegedPlayer(name, true);
	}
	
	public void rmPrivilegedPlayer(String name){
		this.playerPrivileged.remove(name);
		modOpis.instance.config.get(Configuration.CATEGORY_GENERAL, "privileged", new String[]{}).set(playerPrivileged.toArray(new String[]{}));
		modOpis.instance.config.save();		
	}
	
	public boolean isPlayerPrivileged(String name){
		return this.playerPrivileged.contains(name);
	}
	
	public void reloeadPriviligedPlayers(){
		String[] users   = modOpis.instance.config.get(Configuration.CATEGORY_GENERAL, "privileged", new String[]{}).getStringList();
		for (String s : users)
			PlayerTracker.instance().addPrivilegedPlayer(s,false);		
	}
	
	public boolean isTrueOP(Player player){
		return this.isTrueOP(((EntityPlayerMP)player).username);
	}
	
	public boolean isTrueOP(String name){
		return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(name) || MinecraftServer.getServer().isSinglePlayer();
	}		
	
	public boolean isOp(Player player){
		return this.isOp(((EntityPlayerMP)player).username);
	}	
	
	public boolean isOp(String name){
		return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(name) || MinecraftServer.getServer().isSinglePlayer() || PlayerTracker.instance().isPlayerPrivileged(name);
	}	
	
	@Override
	public void onPlayerLogin(EntityPlayer player) {
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		this.playerOverlayStatus.remove(player);
		this.playerDimension.remove(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}	
}
