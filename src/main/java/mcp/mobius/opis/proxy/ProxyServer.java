package mcp.mobius.opis.proxy;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.rcon.nexus.NexusClient;
import mcp.mobius.opis.network.rcon.server.RConServer;

public class ProxyServer implements IMessageHandler{
	
	public void init(){
	}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		return false;
	}
}
