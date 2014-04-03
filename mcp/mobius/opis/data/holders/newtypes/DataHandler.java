package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;
import mcp.mobius.mobiuscore.profiler.ProfilerSection;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.profilers.ProfilerHandlerServer;
import cpw.mods.fml.common.IScheduledTickHandler;

public class DataHandler implements ISerializable, Comparable {
	public String     name;
	public DataTiming update;
	
	public DataHandler fill(IScheduledTickHandler ticker){
		double timingPre  = ((ProfilerHandlerServer)(ProfilerSection.HANDLER_TICKSTART.getProfiler())).data.get(ticker).getGeometricMean();
		double timingPost = ((ProfilerHandlerServer)(ProfilerSection.HANDLER_TICKSTOP.getProfiler())).data.get(ticker).getGeometricMean();		
		
		this.update = new DataTiming(timingPre + timingPost);
		this.name   = TickHandlerManager.getHandlerName(ticker);
		
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.name, stream);
		this.update.writeToStream(stream);
	}

	public static DataHandler readFromStream(DataInputStream stream) throws IOException {
		DataHandler retVal = new DataHandler();
		retVal.name   = Packet.readString(stream, 255);
		retVal.update = DataTiming.readFromStream(stream);
		return retVal;
	}

	@Override
	public int compareTo(Object o) {
		return this.update.compareTo(((DataHandler)o).update);
	}		
}
