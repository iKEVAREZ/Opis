package mcp.mobius.opis.data.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.SingleIntervalHandler;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.holders.stats.StatsTickHandler;

public class TickHandlerManager {

	public static HashMap<String, StatsTickHandler> startStats = new HashMap<String, StatsTickHandler>();
	public static HashMap<String, StatsTickHandler> endStats   = new HashMap<String, StatsTickHandler>();	
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
			startStats.put(name, new StatsTickHandler(name));
		startStats.get(name).addMeasure(timing);		
	}	
	
	public static void addHandlerEnd(IScheduledTickHandler handler, long timing){
		String name = getHandlerName(handler);			
		
		if (!(endStats.containsKey(name)))
			endStats.put(name, new StatsTickHandler(name));
		endStats.get(name).addMeasure(timing);
	}	
	
	public static ArrayList<StatsTickHandler> getCumulatedStats(){
		HashMap<String, StatsTickHandler> totalStats = new HashMap<String, StatsTickHandler>();
		
		for (String key : startStats.keySet()){
			totalStats.put(key, new StatsTickHandler(key));
			totalStats.get(key).setGeometricMean(startStats.get(key).getGeometricMean() + endStats.get(key).getGeometricMean());
			totalStats.get(key).dataPoints = startStats.get(key).getDataPoints() + endStats.get(key).getDataPoints();
		}
		
		ArrayList<StatsTickHandler> sortedStats   = new ArrayList(totalStats.values());
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
	
	public static DataTiming getTotalUpdateTime(){
		ArrayList<StatsTickHandler> tickCumul = getCumulatedStats();
		double updateTime = 0D;
		for (StatsTickHandler data : tickCumul){
			updateTime += data.getGeometricMean();
		}
		return new DataTiming(updateTime);
	}	
}
