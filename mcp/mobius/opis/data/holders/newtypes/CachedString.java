package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.managers.StringCache;

public class CachedString implements Comparable, ISerializable{

	String str;
	int    index;
	
	public CachedString(int index) {
		this.index = index;
		this.str   = StringCache.INSTANCE.getString(index);
	}
	
	public CachedString(String str){
		this.index = StringCache.INSTANCE.getIndex(str);
		this.str   = str;
	}
	
	@Override
	public int compareTo(Object o) {
		return ((CachedString)o).str.compareTo(this.str);	// Reverse order ! Put higher values FIRST
	}
	
	public String toString(){
		return str;		
	}
	
	public void  writeToStream(DataOutputStream stream) throws IOException{
		stream.writeInt(this.index);
	}
	
	public static  CachedString readFromStream(DataInputStream stream) throws IOException {
		return new CachedString(stream.readInt());
	}	
	
}
