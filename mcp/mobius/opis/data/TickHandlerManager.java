package mcp.mobius.opis.data;

import java.util.HashMap;

import cpw.mods.fml.common.IScheduledTickHandler;
import mcp.mobius.opis.data.holders.TickHandlerStats;

public class TickHandlerManager {

	public static HashMap<String, TickHandlerStats> startStats = new HashMap<String, TickHandlerStats>();
	public static HashMap<String, TickHandlerStats> endStats   = new HashMap<String, TickHandlerStats>();	
	
	public static void addHandlerStart(IScheduledTickHandler handler, long timing){
		if (!(startStats.containsKey(handler.getLabel())))
			startStats.put(handler.getLabel(), new TickHandlerStats(handler.getLabel()));
		startStats.get(handler.getLabel()).addMeasure(timing);
	}	
	
	public static void addHandlerEnd(IScheduledTickHandler handler, long timing){
		if (!(endStats.containsKey(handler.getLabel())))
			endStats.put(handler.getLabel(), new TickHandlerStats(handler.getLabel()));
		endStats.get(handler.getLabel()).addMeasure(timing);
	}	
	
}
