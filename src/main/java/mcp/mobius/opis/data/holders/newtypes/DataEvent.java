package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.Table.Cell;
import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import mcp.mobius.opis.data.holders.ISerializable;

public class DataEvent implements ISerializable, Comparable {
	public CachedString     event;
	public CachedString     handler;
	public CachedString     package_;
	public long       nCalls;
	public DataTiming update;
	
	public DataEvent fill(Cell<Class, String, DescriptiveStatistics> cell){
		/*
		String handlerName = cell.getColumnKey().getSimpleName();
		try {
			String[] splitHandler = handlerName.split("_");
			handlerName  = splitHandler[2] + "." + splitHandler[3];
		} catch (Exception e){}
		*/		
		String[] nameRaw = cell.getColumnKey().split("\\|");
		
		String handlerName = nameRaw[1];
		try {
			String[] splitHandler = handlerName.split("_");
			handlerName  = splitHandler[2] + "." + splitHandler[3];
		} catch (Exception e){}		
		
		this.package_= new CachedString(nameRaw[0]);
		this.handler = new CachedString(handlerName);
		this.event   = new CachedString(cell.getRowKey().getName().replace("net.minecraftforge.event.", ""));
		this.nCalls  = cell.getValue().getN();
		
		this.update = new DataTiming(cell.getValue().getGeometricMean());
		return this;
	}
	
	@Override
	public void writeToStream(ByteArrayDataOutput stream){
		this.event.writeToStream(stream);
		this.package_.writeToStream(stream);
		this.handler.writeToStream(stream);
		this.update.writeToStream(stream);
		stream.writeLong(this.nCalls);
	}

	public static DataEvent readFromStream(ByteArrayDataInput stream){
		DataEvent retVal = new DataEvent();
		retVal.event   = CachedString.readFromStream(stream);
		retVal.package_= CachedString.readFromStream(stream);
		retVal.handler = CachedString.readFromStream(stream);
		retVal.update  = DataTiming.readFromStream(stream);
		retVal.nCalls  = stream.readLong();
		return retVal;
	}

	@Override
	public int compareTo(Object o) {
		return this.update.compareTo(((DataHandler)o).update);
	}
}
