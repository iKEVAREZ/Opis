package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class TickStats extends StatAbstract {

	public DescriptiveStatistics dstat = new DescriptiveStatistics(20);
	
	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeDouble(this.getGeometricMean());		
	}
	
	public static  TickStats readFromStream(DataInputStream stream) throws IOException {
		TickStats stat = new TickStats();
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}	
}
