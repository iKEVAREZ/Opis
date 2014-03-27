package mcp.mobius.opis.proxy;

import java.util.ArrayList;
import java.util.HashMap;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.api.IMessageHandler;
import mcp.mobius.opis.data.holders.stats.StatsChunk;
import mcp.mobius.opis.data.holders.stats.StatsEntity;
import mcp.mobius.opis.data.holders.stats.StatsMod;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;
import mcp.mobius.opis.data.holders.stats.StatsTileEntity;
import mcp.mobius.opis.gui.overlay.OverlayStatus;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;

public class ProxyServer implements IMessageHandler{
	
	public void init(){}

	@Override
	public boolean handleMessage(Message msg, NetDataRaw rawdata) {
		return false;
	}
}
