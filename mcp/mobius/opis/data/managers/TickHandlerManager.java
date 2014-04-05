package mcp.mobius.opis.data.managers;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import cpw.mods.fml.common.IScheduledTickHandler;
import cpw.mods.fml.common.SingleIntervalHandler;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.data.holders.newtypes.DataHandler;
import mcp.mobius.opis.data.holders.newtypes.DataTiming;
import mcp.mobius.opis.data.profilers.ProfilerHandlerServer;

public class TickHandlerManager {

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
	
	public static ArrayList<DataHandler> getCumulatedStats(){
		ArrayList<DataHandler> retVal = new ArrayList<DataHandler>();
		for (IScheduledTickHandler ticker : ((ProfilerHandlerServer)(ProfilerSection.HANDLER_TICKSTART.getProfiler())).data.keySet())
			retVal.add(new DataHandler().fill(ticker));
		
		Collections.sort(retVal);
		
		return retVal;
	}
	
	public static String getHandlerName(IScheduledTickHandler handler){
		String name = handler.getLabel();
		if (name == null || name.equals("")){
			if (handler instanceof SingleIntervalHandler){
				try{
					Object ticker = wrapped.get(handler);
					name = ticker.getClass().getName();
				} catch (Exception e) {
					name = "<Unnamed SingleIntervalHandler>";
				}
			}
		}else{
			name = handler.getClass().getName();
		}
		
		if (name == null || name.equals("")){
			name = "<Unnamed SingleIntervalHandler>";
		}
		
		return name;
	}
	
	public static DataTiming getTotalUpdateTime(){
		double updateTime = 0D;
		for (IScheduledTickHandler ticker : ((ProfilerHandlerServer)(ProfilerSection.HANDLER_TICKSTART.getProfiler())).data.keySet())
			updateTime += new DataHandler().fill(ticker).update.timing;
		
		return new DataTiming(updateTime);
	}	
}
