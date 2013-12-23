package mcp.mobius.opis.data;

import java.util.ArrayList;
import java.util.Collections;
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
	
	
}
