package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TickHandlerStats implements ISerializable, Comparable{

	public  DescriptiveStatistics dstat = new DescriptiveStatistics();	// Stored in microseconds !
	private String name;
	private Double geomMean = null;	
	public  int npoints = 0;
	
	
	public TickHandlerStats(String name){
		this.name  = name;
	}
	
	public void addMeasure(long timing){
		dstat.addValue((double)timing/1000.0);
		npoints += 1;
	}	
	
	public double getGeometricMean(){
		if (geomMean != null)
			return geomMean;
		else
			return dstat.getGeometricMean();
	}
	
	public void setGeometricMean(double value){
		this.geomMean = value;
	}	
	
	public String getName(){
		return this.name;
	}

	public String toString(){
		return String.format("[%50s] %.3f micros", this.name, this.getGeometricMean());
	}

	@Override
	public int compareTo(Object o) {
		double value = ((TickHandlerStats)o).getGeometricMean() - this.getGeometricMean();
		if (value > 0)
			return 1;
		if (value < 0)
			return -1;
		return 0;
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
