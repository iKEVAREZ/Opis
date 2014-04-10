package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import com.google.common.collect.Table.Cell;

import net.minecraft.network.packet.Packet;
import mcp.mobius.opis.data.holders.ISerializable;

public class DataEvent implements ISerializable, Comparable {
	public String     event;
	public String     handler;
	public DataTiming update;
	
	public DataEvent fill(Cell<Class, Class, DescriptiveStatistics> cell){
		String handlerName = cell.getColumnKey().getSimpleName();
		try {
			String[] splitHandler = handlerName.split("_");
			handlerName  = splitHandler[2] + "." + splitHandler[3];
		} catch (Exception e){}		
		
		this.handler = handlerName;
		this.event   = cell.getRowKey().getName().replace("net.minecraftforge.event.", "");
		
		this.update = new DataTiming(cell.getValue().getGeometricMean());
		return this;
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.event,   stream);
		Packet.writeString(this.handler, stream);
		this.update.writeToStream(stream);
	}

	public static DataEvent readFromStream(DataInputStream stream) throws IOException {
		DataEvent retVal = new DataEvent();
		retVal.event   = Packet.readString(stream, 255);
		retVal.handler = Packet.readString(stream, 255);
		retVal.update  = DataTiming.readFromStream(stream);
		return retVal;
	}

	@Override
	public int compareTo(Object o) {
		return this.update.compareTo(((DataHandler)o).update);
	}
}
