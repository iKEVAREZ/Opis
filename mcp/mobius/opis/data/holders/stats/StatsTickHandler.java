package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;
import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatsTickHandler extends StatAbstract implements ISerializable{
	
	public StatsTickHandler(String name){
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
	
	public static  StatsTickHandler readFromStream(DataInputStream stream) throws IOException {
		StatsTickHandler stats = new StatsTickHandler(Packet.readString(stream, 255));
		stats.setGeometricMean(stream.readDouble());
		return stats;
	}	
	
}
