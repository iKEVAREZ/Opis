package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import net.minecraft.network.packet.Packet;

public class ModStats implements ISerializable, Comparable{
	private String modID;
	public  double meanTime;
	public  double meanGeom;
	public  double variance;
	public  double minTime;
	public  double maxTime;
	public  double median;
	public  double total;
	public  int    ntes;

	private double cachedMedian = -1.0;
	
	DescriptiveStatistics dstat = new DescriptiveStatistics();
	
	public ModStats(String modID){
		this.modID = modID;
		
		if (this.modID == null)
			this.modID = "<Unknown>";
	}
	
	public void addStat(TileEntityStats tes){
		this.dstat.addValue(tes.getGeometricMean());
	}
	
	//public double getMeanTimePerEntity(){
	//	return this.dstat.getMean();
	//}

	public String getModID(){
		return this.modID;
	}
	
	@Override
	public int compareTo(Object o) {
		//return this.dstat.getMean() > ((ModStats)o).dstat.getMean() ? -1 : 1;
		//return this.meanTime > ((ModStats)o).meanTime ? -1 : 1;
		if (this.cachedMedian < 0.0)
			this.cachedMedian = this.dstat.getSortedValues()[(int)this.dstat.getN()/2];

		if (((ModStats)o).cachedMedian < 0.0)
			((ModStats)o).cachedMedian = ((ModStats)o).dstat.getSortedValues()[(int)((ModStats)o).dstat.getN()/2];
			
		return this.cachedMedian > ((ModStats)o).cachedMedian ? -1 : 1;		
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		Packet.writeString(this.modID, stream);
		
		stream.writeDouble(dstat.getMean());
		stream.writeDouble(dstat.getGeometricMean());
		stream.writeDouble(dstat.getVariance());
		stream.writeDouble(dstat.getMin());
		stream.writeDouble(dstat.getMax());
		stream.writeDouble(dstat.getSum());
		stream.writeDouble(dstat.getSortedValues()[(int)dstat.getN()/2]);
		stream.writeInt((int)dstat.getN());
	}
	
	public static  ModStats readFromStream(DataInputStream stream) throws IOException {
		String modID = Packet.readString(stream, 255);
		ModStats modStats = new ModStats(modID);
	
		modStats.meanTime = stream.readDouble();
		modStats.meanGeom = stream.readDouble();
		modStats.variance = stream.readDouble();
		modStats.minTime  = stream.readDouble();
		modStats.maxTime  = stream.readDouble();
		modStats.total    = stream.readDouble();
		modStats.median   = stream.readDouble();
		modStats.ntes     = stream.readInt();
		
		modStats.cachedMedian = modStats.median;
		return modStats;
	}
	
	public String toString(){
		return String.format("[ %s ] %d %.2f %.2f", this.modID, this.ntes, this.meanTime, this.dstat.getMean());
	}
	
}
