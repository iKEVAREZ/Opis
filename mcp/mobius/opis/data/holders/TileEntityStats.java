package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TileEntityStats implements ISerializable, Comparable {

	private DescriptiveStatistics stats = new DescriptiveStatistics(100);	
	private CoordinatesBlock coord;
	private String name;
	private Double geomMean = null;
	
	
	public TileEntityStats(CoordinatesBlock coord, String teclass){
		this.coord = coord;
		this.name  = teclass;
	}
	
	public void addMeasure(long timing){
		stats.addValue(timing/1000.0);
	}
	
	public double getGeometricMean(){
		if (geomMean != null)
			return geomMean;
		else
			return stats.getGeometricMean();
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
	
	public String getType(){
		return this.name;
	}
	
	public void setType(String name){
		this.name = name;
	}
	

	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.coord.writeToStream(stream);
		Packet.writeString(this.name, stream);
		stream.writeDouble(this.getGeometricMean());
	}

	public static  TileEntityStats readFromStream(DataInputStream stream) throws IOException {
		CoordinatesBlock coord   = CoordinatesBlock.readFromStream(stream);
		String classname         = Packet.readString(stream, 255);
		TileEntityStats stat = new TileEntityStats(coord, classname);
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
}
