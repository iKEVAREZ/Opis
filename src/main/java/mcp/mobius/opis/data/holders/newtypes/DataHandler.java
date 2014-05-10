package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.profilers.ProfilerHandler;

public class DataHandler implements ISerializable, Comparable {
	public CachedString name;
	public DataTiming   update;
	
	/*
	public DataHandler fillServer(IScheduledTickHandler ticker){
		double timingPre  = ((ProfilerHandler)(ProfilerSection.HANDLER_TICKSTART.getProfiler())).dataServer.get(ticker).getGeometricMean();
		double timingPost = ((ProfilerHandler)(ProfilerSection.HANDLER_TICKSTOP.getProfiler())).dataServer.get(ticker).getGeometricMean();		
		
		this.update = new DataTiming(timingPre + timingPost);
		this.name   = new CachedString(TickHandlerManager.getHandlerName(ticker));
		
		return this;
	}

	public DataHandler fillRender(IScheduledTickHandler ticker){
		double timingPre  = ((ProfilerHandler)(ProfilerSection.HANDLER_TICKSTART.getProfiler())).dataRender.get(ticker).getGeometricMean();
		double timingPost = ((ProfilerHandler)(ProfilerSection.HANDLER_TICKSTOP.getProfiler())).dataRender.get(ticker).getGeometricMean();		
		
		this.update = new DataTiming(timingPre + timingPost);
		this.name   = new CachedString(TickHandlerManager.getHandlerName(ticker));
		
		return this;
	}	
	*/
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.name.writeToStream(stream);
		this.update.writeToStream(stream);
	}

	public static DataHandler readFromStream(ByteArrayDataInput stream){
		DataHandler retVal = new DataHandler();
		retVal.name   = CachedString.readFromStream(stream);
		retVal.update = DataTiming.readFromStream(stream);
		return retVal;
	}

	@Override
	public int compareTo(Object o) {
		return this.update.compareTo(((DataHandler)o).update);
	}		
}
