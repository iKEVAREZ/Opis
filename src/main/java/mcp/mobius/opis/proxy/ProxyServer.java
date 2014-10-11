package mcp.mobius.opis.proxy;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.PacketBase;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.rcon.server.RConServer;

public class ProxyServer implements IMessageHandler{
	
	public void init(){
		if (modOpis.rconactive){
			Thread rconserver = new Thread(RConServer.instance);
			rconserver.setName("Opis Server");
			rconserver.start();
		}
	}

	@Override
	public boolean handleMessage(Message msg, PacketBase rawdata) {
		return false;
	}
}
