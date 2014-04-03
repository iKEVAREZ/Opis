package mcp.mobius.opis.data.holders;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.HashMap;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;
import mcp.mobius.opis.data.profilers.ProfilerDimBlockTick;

public class BlockTickData implements ISerializable{
	public HashMap<Integer, TimingData> perdim = new HashMap<Integer, TimingData>();
	public TimingData total;
	
	public BlockTickData fill(){
		this.total = new TimingData();
		HashMap<Integer, DescriptiveStatistics> data = ((ProfilerDimBlockTick)ProfilerSection.DIMENSION_BLOCKTICK.getProfiler()).data;
		
		for (Integer dim : data.keySet()){
			this.perdim.put(dim, new TimingData(data.get(dim).getGeometricMean()));
			this.total.timing += data.get(dim).getGeometricMean();
		}
		
		return this;
	}

	@Override
	public void writeToStream(DataOutputStream stream) throws IOException {
		stream.writeShort(this.perdim.size());
		for (Integer key : this.perdim.keySet()){
			stream.writeInt(key);
			this.perdim.get(key).writeToStream(stream);
		}
		this.total.writeToStream(stream);
	}
	
	public static BlockTickData readFromStream(DataInputStream stream) throws IOException {
		BlockTickData retVal = new BlockTickData();
		int nkeys = stream.readShort();
		for (int i = 0; i < nkeys; i++)
			retVal.perdim.put(stream.readInt(), TimingData.readFromStream(stream));
		retVal.total = TimingData.readFromStream(stream);
		return retVal;
	}
}
