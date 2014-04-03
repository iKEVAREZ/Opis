package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class TimingData implements Comparable, ISerializable{
	public Double timing;

	public TimingData(){ this.timing = 0.0D; }
	public TimingData(double timing){ this.timing = timing; }
	
	@Override
	public int compareTo(Object o) {
		return timing.compareTo(((TimingData)o).timing);
	}
	
	public TimingDataMillisecond asMillisecond(){
		return new TimingDataMillisecond(this.timing);
	}
	
	public String toString(){
		return String.format("%.3f µs", this.timing / 1000.0);
	}
	
	public void  writeToStream(DataOutputStream stream) throws IOException{
		stream.writeDouble(this.timing);
	}
	
	public static  TimingData readFromStream(DataInputStream stream) throws IOException {
		return new TimingData(stream.readDouble());
	}
}
