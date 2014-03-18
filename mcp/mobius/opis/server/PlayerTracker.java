package mcp.mobius.opis.server;

import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.overlay.OverlayStatus;
import net.minecraft.entity.player.EntityPlayer;
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
