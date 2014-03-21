package mcp.mobius.opis.data.holders.stats;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.holders.basetypes.CoordinatesBlock;
import net.minecraft.network.packet.Packet;

import org.apache.commons.math3.stat.descriptive.DescriptiveStatistics;

public class StatsTileEntity extends StatAbstract implements ISerializable {

	private int    blockID;
	private short  blockMeta;
	private double cachedMedian = -1.0;
	
	
	public StatsTileEntity(CoordinatesBlock coord, int blockID, short blockMeta){
		this.coord = coord;
		this.chunk = coord.asCoordinatesChunk();
		//this.name    = teclass;
		this.blockID   = blockID;
		this.blockMeta = blockMeta;
	}
	
	public int getID(){
		return this.blockID;
	}

	public short getMeta(){
		return this.blockMeta;
	}
	
	public void setBlock(int ID, short meta){
		this.blockID   = ID;
		this.blockMeta = meta;
	}
	
	public double getMedian(){
		if (this.cachedMedian < 0.0){
			int ndata = (int)this.dstat.getN();
			if (ndata == 1)
				return this.dstat.getElement(0);
			
			if (ndata % 2 == 1)
				return this.dstat.getSortedValues()[ndata/2];
			
			if (ndata % 2 == 0)
				return (this.dstat.getSortedValues()[ndata/2 - 1] + this.dstat.getSortedValues()[ndata/2]) / 2;
		}	
		return this.cachedMedian;
	}	
	
	@Override
	public   void writeToStream(DataOutputStream stream) throws IOException{
		this.coord.writeToStream(stream);
		//Packet.writeString(this.name, stream);
		stream.writeInt(this.blockID);
		stream.writeShort(this.blockMeta);
		stream.writeDouble(this.getGeometricMean());
	}

	public static  StatsTileEntity readFromStream(DataInputStream stream) throws IOException {
		CoordinatesBlock coord   = CoordinatesBlock.readFromStream(stream);
		int blockID     = stream.readInt();
		short blockMeta = stream.readShort();
		//String classname         = Packet.readString(stream, 255);
		StatsTileEntity stat = new StatsTileEntity(coord, blockID, blockMeta);
		stat.setGeometricMean(stream.readDouble());
		return stat;
	}
	
	/*
	public String toString(){
		return String.format("[%d:%d] %s %s", this.blockID, this.blockMeta, this.coord, this.getGeometricMean());
	}
	*/
	public String toString(){
		return String.format("%.3f µs", this.getGeometricMean());
	}
}
