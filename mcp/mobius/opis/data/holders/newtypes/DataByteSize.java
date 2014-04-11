package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class DataByteSize implements Comparable, ISerializable {
	public Long size;	// Size is stored in byte

	public DataByteSize(){ this.size = 0L; }
	public DataByteSize(long size){ this.size = size; }
	
	@Override
	public int compareTo(Object o) {
		return ((DataByteSize)o).size.compareTo(this.size);	// Reverse order ! Put higher values FIRST
	}
	
	public String toString(){
		
		if (size >= 1024 && size < (1024*1024)){
			return String.format("%.3f KiB", this.size / 1024.0);
		}

		if (size >= (1024*1024)){
			return String.format("%.3f GiB", this.size / 1024.0 / 1024.0);
		}		

		return String.format("%4d   B", this.size);		
	}
	
	public void  writeToStream(DataOutputStream stream) throws IOException{
		stream.writeLong(this.size);
	}
	
	public static  DataByteSize readFromStream(DataInputStream stream) throws IOException {
		return new DataByteSize(stream.readLong());
	}
}
