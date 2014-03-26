package mcp.mobius.opis.api;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

import mcp.mobius.opis.network.enums.Message;

public class MessageHandlerRegistrar {
	private static MessageHandlerRegistrar _instance = new MessageHandlerRegistrar();
	private MessageHandlerRegistrar(){};
	public  MessageHandlerRegistrar instance(){return this._instance;}
	
	private HashMap<Message, HashSet<IMessageHandler>> msgHandlers = new HashMap<Message, HashSet<IMessageHandler>>();
	
	public void registerHandler(Message msg, IMessageHandler handler){
		if (!msgHandlers.containsKey(msg))
			msgHandlers.put(msg, new HashSet<IMessageHandler>());
		
		msgHandlers.get(msg).add(handler);
	}
	
	public void routeMessage(Message msg){
		if (msgHandlers.containsKey(msg))
			for (IMessageHandler handler : msgHandlers.get(msg))
				handler.handle(msg);
	}
}
