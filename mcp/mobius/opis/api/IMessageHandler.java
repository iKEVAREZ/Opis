package mcp.mobius.opis.api;

import mcp.mobius.opis.network.enums.Message;

public interface IMessageHandler {
	public void handle(Message msg);
}
