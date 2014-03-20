package mcp.mobius.opis.data.holders.stats;

import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesChunk;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public abstract class StatAbstract implements Comparable, ISerializable{
	public    DescriptiveStatistics dstat = new DescriptiveStatistics();
	public    Long   dataPoints  = 0L;
	protected Double geomMean        = null;
	protected Double dataSum         = null;
	protected CoordinatesBlock coord = null;
	protected CoordinatesChunk chunk = null;
	protected String name;
	
	public void addMeasure(long timing){
		dstat.addValue((double)timing/1000.0);
		dataPoints += 1;
	}		
	
	public void addMeasure(double timing){
		dstat.addValue(timing);
		dataPoints += 1;
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
	
	public double getDataSum(){
		if (dataSum != null)
			return dataSum;
		else
			return dstat.getSum();
	}	
	
	public void setDataSum(double value){
		this.dataSum = value;
	}	
	
	public long getDataPoints(){
		return this.dataPoints;
	}
	
	public void setDataPoints(long ndata){
		this.dataPoints = ndata;
	}
	
	@Override
	public int compareTo(Object o) {
		double value = ((StatAbstract)o).getGeometricMean() - this.getGeometricMean();
		if (value > 0)
			return 1;
		if (value < 0)
			return -1;
		return 0;
	}	
	
	public String getName(){ return this.name; };
	
	public CoordinatesBlock getCoordinates(){return this.coord;}
	public CoordinatesChunk getChunk(){return new CoordinatesChunk(this.getCoordinates());}	
	
	public abstract void  writeToStream(DataOutputStream stream) throws IOException;

}
