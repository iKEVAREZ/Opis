package mcp.mobius.opis.data.holders.newtypes;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteArrayDataOutput;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.server.FMLServerHandler;
import mcp.mobius.opis.data.holders.ISerializable;
import mcp.mobius.opis.data.managers.StringCache;
import mcp.mobius.opis.helpers.Helpers;


public class CachedString implements Comparable, ISerializable{

	String str;
	int    index;
	
	public CachedString(int index) {
		this.index = index;
		this.str   = StringCache.INSTANCE.getString(index);
	}
	
	public CachedString(String str){

		//if(FMLCommonHandler.instance().getSide() == Side.SERVER){
		if(Helpers.getEffectiveSide() == Side.SERVER){
			this.index = StringCache.INSTANCE.getIndex(str);
			this.str   = str;
		} else {
			this.index = -1;
			this.str   = str;
		}
	}
	

	
	@Override
	public int compareTo(Object o) { 
		return ((CachedString)o).str.compareTo(this.str);	// Reverse order ! Put higher values FIRST
	}
	
	public String toString(){
		//return String.format("[%d] %s",this.index, this.str);
		return str;
	}
	
	public void  writeToStream(ByteArrayDataOutput stream){
		stream.writeInt(this.index);
	}
	
	public static  CachedString readFromStream(ByteArrayDataInput stream){
		return new CachedString(stream.readInt());
	}	
	
}
