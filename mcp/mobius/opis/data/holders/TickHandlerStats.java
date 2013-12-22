package mcp.mobius.opis.data.holders;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TickHandlerStats {

	public  DescriptiveStatistics dstat = new DescriptiveStatistics();	// Stored in microseconds !
	private String name;
	private Double geomMean = null;	
	
	
	public TickHandlerStats(String name){
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
	
	public void setGeometricMean(double value){
		this.geomMean = value;
	}	
	
	public String getName(){
		return this.name;
	}

	public String toString(){
		return String.format("[%50s] %.3f micros", this.name, this.getGeometricMean());
	}	
	
}
