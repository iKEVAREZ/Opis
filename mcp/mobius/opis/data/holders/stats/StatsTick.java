package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatsTick extends StatAbstract {

	public StatsTick(){
		this.dstat = new DescriptiveStatistics(20);
	}
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		//stream.writeDouble(this.getGeometricMean());		
		stream.writeDouble(this.dstat.getGeometricMean());
	}
	
	public static  StatsTick readFromStream(DataInputStream stream) throws IOException {
		StatsTick stat = new StatsTick();
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}	
}
