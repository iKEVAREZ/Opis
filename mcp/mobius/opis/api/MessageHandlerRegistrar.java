package mcp.mobius.opis.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataRaw;

public class MessageHandlerRegistrar {
	private static MessageHandlerRegistrar _instance = new MessageHandlerRegistrar();
	private MessageHandlerRegistrar(){};
	public static  MessageHandlerRegistrar instance(){return _instance;}
	
	private HashMap<Message, HashSet<IMessageHandler>> msgHandlers = new HashMap<Message, HashSet<IMessageHandler>>();
	
	public void registerHandler(Message msg, IMessageHandler handler){
		if (!msgHandlers.containsKey(msg))
			msgHandlers.put(msg, new HashSet<IMessageHandler>());
		
		msgHandlers.get(msg).add(handler);
	}
	
	public void routeMessage(Message msg, NetDataRaw rawdata){
		if (msgHandlers.containsKey(msg))
			for (IMessageHandler handler : msgHandlers.get(msg))
				handler.handleMessage(msg, rawdata);
		else
			modOpis.log.warning(String.format("Unhandled msg : %s", msg));
	}
}
