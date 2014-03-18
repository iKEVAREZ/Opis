package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TickHandlerStats extends StatAbstract implements ISerializable{
	
	public TickHandlerStats(String name){
		this.name  = name;
	}

	public String toString(){
		return String.format("[%50s] %.3f micros", this.name, this.getGeometricMean());
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.name, stream);
		stream.writeDouble(this.getGeometricMean());
	}	
	
	public static  TickHandlerStats readFromStream(DataInputStream stream) throws IOException {
		TickHandlerStats stats = new TickHandlerStats(Packet.readString(stream, 255));
		stats.setGeometricMean(stream.readDouble());
		return stats;
	}	
	
}
