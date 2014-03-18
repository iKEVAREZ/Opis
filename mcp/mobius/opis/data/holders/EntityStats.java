package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.opis.modOpis;

public class EntityStats extends StatAbstract implements ISerializable {

	private int entId;
	private int dim;
	private double cachedMedian = -1.0;	
	private double x, y, z;
	
	public EntityStats(int entId, String name, int dim, double x, double y, double z){
		this.entId = entId;
		this.name  = name;
		this.dim   = dim;
		this.x     = x;
		this.y     = y;
		this.z     = z;
	}
	

	
	public int getID(){ return this.entId; }
	
	public double getMedian(){
		if (this.cachedMedian < 0.0){
			int ndata = (int)this.dstat.getN();
			if (ndata == 1)
				return this.dstat.getElement(0);
			
			if (ndata % 2 == 1)
				return this.dstat.getSortedValues()[ndata/2];
			
			if (ndata % 2 == 0)
				return (this.dstat.getSortedValues()[ndata/2 - 1] + this.dstat.getSortedValues()[ndata/2]) / 2;
		}	
		return this.cachedMedian;
	}	

	public String toString(){
		return String.format("[%d] %50s %.3f \u00B5s", this.entId, this.name, this.getGeometricMean());
	}

	@Override
	public CoordinatesBlock getCoordinates(){
		return new CoordinatesBlock(this.dim, this.x, this.y, this.z);
	}
	
	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		stream.writeInt(this.entId);
		Packet.writeString(this.name, stream);
		stream.writeInt(this.dim);
		stream.writeDouble(this.x);
		stream.writeDouble(this.y);
		stream.writeDouble(this.z);
		stream.writeDouble(this.getGeometricMean());
		stream.writeLong(this.getDataPoints());
	}

	public static  EntityStats readFromStream(DataInputStream stream) throws IOException {
		EntityStats stats = new EntityStats(stream.readInt(), Packet.readString(stream, 255), stream.readInt(),
											stream.readDouble(), stream.readDouble(), stream.readDouble());
		stats.setGeometricMean(stream.readDouble());
		stats.setDataPoints(stream.readLong());
		return stats;
	}		
	
}
