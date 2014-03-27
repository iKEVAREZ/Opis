package mcp.mobius.opis.api;

import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;

public interface IMessageHandler {
	public void handleMessage(Message msg, NetDataRaw rawdata);
}
