package mcp.mobius.opis.proxy;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.data.holders.ChunkStats;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.holders.ModStats;
import mcp.mobius.opis.data.holders.TickHandlerStats;
import mcp.mobius.opis.data.holders.TileEntityStats;
import mcp.mobius.opis.overlay.OverlayStatus;

public class ProxyServer {
	public HashMap<Player, OverlayStatus> playerOverlayStatus = new HashMap<Player, OverlayStatus>();
	public HashMap<Player, Integer>       playerDimension     = new HashMap<Player, Integer>();
	
	public void init(){}
	public void displayTileEntityList(ArrayList<TileEntityStats> stats){}
	public void displayEntityList(ArrayList<EntityStats> stats){}
	public void displayHandlerList(ArrayList<TickHandlerStats> stats){}	
	public void displayChunkList(ArrayList<ChunkStats> stats){}
	public void displayModList(ArrayList<ModStats> stats){}
	public void displayEntityAmount(HashMap<String, Integer> stats){}
}
