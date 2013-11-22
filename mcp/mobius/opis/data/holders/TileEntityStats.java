package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.modOpis;
import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TileEntityStats implements ISerializable, Comparable {

	public  DescriptiveStatistics dstat = new DescriptiveStatistics(modOpis.profilerMaxTicks);	// Stored in microseconds !
	private CoordinatesBlock coord;
	private String name;
	private int    blockID;
	private short  blockMeta;
	private Double geomMean = null;
	private double cachedMedian;
	
	
	public TileEntityStats(CoordinatesBlock coord, int blockID, short blockMeta){
		this.coord = coord;
		//this.name  = teclass;
		this.blockID   = blockID;
		this.blockMeta = blockMeta;
	}
	
	public void addMeasure(long timing){
		dstat.addValue(timing/1000.0);
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
	
	public CoordinatesBlock getCoordinates(){
		return this.coord;
	}
	
	public CoordinatesChunk getChunk(){
		return new CoordinatesChunk(this.coord);
	}
	
	//public String getType(){
	//	return this.name;
	//}
	
	//public void setType(String name){
	//	this.name = name;
	//}
	
	public int getID(){
		return this.blockID;
	}

	public short getMeta(){
		return this.blockMeta;
	}
	
	public void setBlock(int ID, short meta){
		this.blockID   = ID;
		this.blockMeta = meta;
	}
	
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
	
	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.coord.writeToStream(stream);
		//Packet.writeString(this.name, stream);
		stream.writeInt(this.blockID);
		stream.writeShort(this.blockMeta);
		stream.writeDouble(this.getGeometricMean());
	}

	public static  TileEntityStats readFromStream(DataInputStream stream) throws IOException {
		CoordinatesBlock coord   = CoordinatesBlock.readFromStream(stream);
		int blockID     = stream.readInt();
		short blockMeta = stream.readShort();
		//String classname         = Packet.readString(stream, 255);
		TileEntityStats stat = new TileEntityStats(coord, blockID, blockMeta);
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}

	@Override
	public int compareTo(Object arg0) {
		double value = ((TileEntityStats)arg0).getGeometricMean() - this.getGeometricMean();
		if (value > 0)
			return 1;
		if (value < 0)
			return -1;
		return 0;
	}
	
	public String toString(){
		return String.format("[%d:%d] %s %s", this.blockID, this.blockMeta, this.coord, this.getGeometricMean());
	}
}
