package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.data.holders.ISerializable;

public class DataThread implements ISerializable{

	public CachedString name;
	public CachedString clzz;
	
	public DataThread fill(Thread thread){
		this.name = new CachedString(thread.getName());
		this.clzz = new CachedString(thread.getClass().getSimpleName());
		return this;
	}
	
	public void  writeToStream(DataOutputStream stream) throws IOException{
		this.name.writeToStream(stream);
		this.clzz.writeToStream(stream);
		
	}
	
	public static  DataThread readFromStream(DataInputStream stream) throws IOException {
		DataThread retVal = new DataThread();
		retVal.name = CachedString.readFromStream(stream);
		retVal.clzz = CachedString.readFromStream(stream);
		return retVal;
	}	
	
}
