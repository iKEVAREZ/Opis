package mcp.mobius.opis.data.holders;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.opis.modOpis;

public class EntityStats {

	public  DescriptiveStatistics dstat = new DescriptiveStatistics(modOpis.profilerMaxTicks);	// Stored in microseconds !
	private String name;
	private int    entId;
	private Double geomMean = null;
	private double cachedMedian = -1.0;	

	public EntityStats(int entId, String name){
		this.entId = entId;
		this.name  = name;
	}
	
	public void addMeasure(long timing){
		dstat.addValue((double)timing/1000.0);
	}	

	public double getGeometricMean(){
		if (geomMean != null)
			return geomMean;
		else
			return dstat.getGeometricMean();
	}	
	
	public String toString(){
		return String.format("[%d] %50s %.3f \u00B5s", this.entId, this.name, this.getGeometricMean());
	}	
	
}
