package mcp.mobius.opis.data;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TileEntityStatsData implements ISerializable {

	private DescriptiveStatistics stats = new DescriptiveStatistics(250);	
	private CoordinatesBlock coord;
	private String classname;
	private Double geomMean = null;
	
	
	public TileEntityStatsData(CoordinatesBlock coord, String teclass){
		this.coord     = coord;
		this.classname = teclass;
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
		return this.classname;
	}

	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.coord.writeToStream(stream);
		Packet.writeString(this.classname, stream);
		stream.writeDouble(this.getGeometricMean());
	}

	public static  TileEntityStatsData readFromStream(DataInputStream stream) throws IOException {
		CoordinatesBlock coord   = CoordinatesBlock.readFromStream(stream);
		String classname         = Packet.readString(stream, 255);
		TileEntityStatsData stat = new TileEntityStatsData(coord, classname);
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}
}
