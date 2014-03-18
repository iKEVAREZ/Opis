package mcp.mobius.opis.data.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.SingleIntervalHandler;
import mcp.mobius.opis.data.holders.TickHandlerStats;

public class TickHandlerManager {

	public static HashMap<String, TickHandlerStats> startStats = new HashMap<String, TickHandlerStats>();
	public static HashMap<String, TickHandlerStats> endStats   = new HashMap<String, TickHandlerStats>();	
	public static Class SingleIntervalHandlerReflect;
	public static Field wrapped;
	
	static {
		try{
			SingleIntervalHandlerReflect = Class.forName("cpw.mods.fml.common.SingleIntervalHandler");
			wrapped                = SingleIntervalHandlerReflect.getDeclaredField("wrapped");
			wrapped.setAccessible(true);
			
		} catch (Exception e) {
			
			throw new RuntimeException(e);
			
		}
	}
	
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
			totalStats.get(key).dataPoints = startStats.get(key).getDataPoints() + endStats.get(key).getDataPoints();
		}
		
		ArrayList<TickHandlerStats> sortedStats   = new ArrayList(totalStats.values());
		Collections.sort(sortedStats);
		
		return sortedStats;
	}
	
	public static String getHandlerName(IScheduledTickHandler handler){
		String name = handler.getLabel();
		if (name == null || name.equals(""))
			if (handler instanceof SingleIntervalHandler){
				try{
					Object ticker = wrapped.get(handler);
					name = ticker.getClass().getName();
				} catch (Exception e) {
					throw new RuntimeException(e);
				}
				
				
				//name = ((SingleIntervalHandler)handler).wrapped.getClass().getName();
				name = "<Unnamed SingleIntervalHandler>"; // This part should be done via reflection to get the handler name./opis
			}
			else
				name = handler.getClass().getName();
		
		return name;
	}	
}
