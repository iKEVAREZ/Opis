package mcp.mobius.opis.data;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TileEntityStatsData {

	private DescriptiveStatistics stats = new DescriptiveStatistics(250);	
	
	public void addMeasure(long timing){
		stats.addValue(timing/1000.0);
	}
	
	public double getGeometricMean(){
		return stats.getGeometricMean();
	}
}
