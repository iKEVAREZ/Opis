package mcp.mobius.opis.proxy;

import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.overlay.OverlayStatus;

public class ProxyServer {
	public HashMap<Player, OverlayStatus> playerOverlayStatus = new HashMap<Player, OverlayStatus>();
	public HashMap<Player, Integer>       playerDimension     = new HashMap<Player, Integer>();
	
	public void init(){}
}
