package mcp.mobius.opis.data.client;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.SingleIntervalHandler;
import mcp.mobius.opis.data.holders.TickHandlerStats;

public class TickHandlerClientManager {

	public static HashMap<String, TickHandlerStats> startStats = new HashMap<String, TickHandlerStats>();
	public static HashMap<String, TickHandlerStats> endStats   = new HashMap<String, TickHandlerStats>();	
	
	public static void addHandlerStart(IScheduledTickHandler handler, long timing){
		String name = getHandlerName(handler);			
		
		if (!(startStats.containsKey(name)))
			startStats.put(name, new TickHandlerStats(name));
		startStats.get(name).addMeasure(timing);		
	}	
	
	public static void addHandlerEnd(IScheduledTickHandler handler, long timing){
		String name = getHandlerName(handler);
		
		if (!(endStats.containsKey(name)))
			endStats.put(name, new TickHandlerStats(name));
		endStats.get(name).addMeasure(timing);
	}	
	
	public static ArrayList<TickHandlerStats> getCumulatedStats(){
		HashMap<String, TickHandlerStats> totalStats = new HashMap<String, TickHandlerStats>();
		
		for (String key : startStats.keySet()){
			totalStats.put(key, new TickHandlerStats(key));
			totalStats.get(key).setGeometricMean(startStats.get(key).getGeometricMean() + endStats.get(key).getGeometricMean());
		}
		
		ArrayList<TickHandlerStats> sortedStats   = new ArrayList(totalStats.values());
		Collections.sort(sortedStats);
		
		return sortedStats;
	}	
	
	public static String getHandlerName(IScheduledTickHandler handler){
		String name = handler.getLabel();
		if (name == null || name.equals(""))
			if (handler instanceof SingleIntervalHandler)
				name = ((SingleIntervalHandler)handler).wrapped.getClass().getName();
			else
				name = handler.getClass().getName();
		
		return name;
	}
	
}
