package mcp.mobius.opis.events;

import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.enums.AccessLevel;
import mcp.mobius.opis.swing.SelectedTab;
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
	
	public HashSet<Player> playersSwing = new HashSet<Player>();		 //This is the list of players who have opened the UI
	//public HashSet<Player> playersOpis  = new HashSet<Player>();		 //This is the list of players who have opened the UI or used the command line
	public HashMap<String, Boolean> filteredAmount = new HashMap<String, Boolean>(); //Should the entity amount be filtered or not
	public HashMap<Player, OverlayStatus> playerOverlayStatus = new HashMap<Player, OverlayStatus>();
	public HashMap<Player, Integer>       playerDimension     = new HashMap<Player, Integer>();
	public HashMap<Player, SelectedTab>   playerTab           = new HashMap<Player, SelectedTab>();
	private HashSet<String> playerPrivileged = new HashSet<String>();
	
	public SelectedTab getPlayerSelectedTab(Player player){
		return this.playerTab.get(player);
	}
	
	public AccessLevel getPlayerAccessLevel(Player player){
		return this.getPlayerAccessLevel(((EntityPlayerMP)player).username);
	}
	
	public AccessLevel getPlayerAccessLevel(String name){
		if (MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(name) || MinecraftServer.getServer().isSinglePlayer())
			return AccessLevel.ADMIN;
		else if (playerPrivileged.contains(name))
			return AccessLevel.PRIVILEGED;
		else
			return AccessLevel.NONE;
	}	
	
	public void addPrivilegedPlayer(String name, boolean save){
		this.playerPrivileged.add(name);
		if (save){
			modOpis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[]{}, modOpis.commentPrivileged).set(playerPrivileged.toArray(new String[]{}));
			modOpis.instance.config.save();
		}
	}
	
	public void addPrivilegedPlayer(String name){
		this.addPrivilegedPlayer(name, true);
	}
	
	public void rmPrivilegedPlayer(String name){
		this.playerPrivileged.remove(name);
		modOpis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[]{}, modOpis.commentPrivileged).set(playerPrivileged.toArray(new String[]{}));
		modOpis.instance.config.save();		
	}
	
	public void reloeadPriviligedPlayers(){
		String[] users   = modOpis.instance.config.get("ACCESS_RIGHTS", "privileged", new String[]{}, modOpis.commentPrivileged).getStringList();
		for (String s : users)
			PlayerTracker.instance().addPrivilegedPlayer(s,false);		
	}
	
	public boolean isAdmin(Player player){
		return this.getPlayerAccessLevel(player).ordinal() >= AccessLevel.ADMIN.ordinal();
	}
	
	public boolean isAdmin(String name){
		return this.getPlayerAccessLevel(name).ordinal() >= AccessLevel.ADMIN.ordinal();		
	}		
	
	public boolean isPrivileged(Player player){
		return this.getPlayerAccessLevel(player).ordinal() >= AccessLevel.PRIVILEGED.ordinal();
	}	
	
	public boolean isPrivileged(String name){
		return this.getPlayerAccessLevel(name).ordinal() >= AccessLevel.PRIVILEGED.ordinal();
	}	
	
	//+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++//
	
	@Override
	public void onPlayerLogin(EntityPlayer player) {
	}

	@Override
	public void onPlayerLogout(EntityPlayer player) {
		this.playerOverlayStatus.remove(player);
		this.playerDimension.remove(player);
		//this.playersOpis.remove(player);
		this.playersSwing.remove(player);
	}

	@Override
	public void onPlayerChangedDimension(EntityPlayer player) {
	}

	@Override
	public void onPlayerRespawn(EntityPlayer player) {
	}	
}
