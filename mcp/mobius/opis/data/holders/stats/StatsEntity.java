package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;

public class StatsEntity extends StatAbstract implements ISerializable {

	private int entId;
	private double cachedMedian = -1.0;	
	
	public StatsEntity(int entId, String name, int dim, double x, double y, double z){
		this.entId = entId;
		this.name  = name;
		this.coord = new CoordinatesBlock(dim, x, y, z);
		this.chunk = this.coord.asCoordinatesChunk();
	}

	public StatsEntity(int entId, String name, CoordinatesBlock coord){
		this.entId = entId;
		this.name  = name;
		this.coord = coord;
		this.chunk = this.coord.asCoordinatesChunk();
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
		return String.format("%.3f µs", this.getGeometricMean());
	}

	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.coord.writeToStream(stream);
		stream.writeInt(this.entId);
		Packet.writeString(this.name, stream);
		stream.writeDouble(this.getGeometricMean());
		stream.writeLong(this.getDataPoints());
	}

	public static  StatsEntity readFromStream(DataInputStream stream) throws IOException {
		CoordinatesBlock coord = CoordinatesBlock.readFromStream(stream);
		StatsEntity stats = new StatsEntity(stream.readInt(), Packet.readString(stream, 255), coord);
		stats.setGeometricMean(stream.readDouble());
		stats.setDataPoints(stream.readLong());
		return stats;
	}		
	
}
