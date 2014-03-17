package mcp.mobius.opis.network.json;

import java.util.ArrayList;

import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;

public enum OpisTypes {

	INVALID   (null,         null),
	DIM       ("dim",        Integer.class), 
	CHUNKS    ("chunks",     ArrayList.class),
	CHUNK     ("chunk",      CoordinatesChunk.class),
	DATANAME  ("dataname",   String.class),
	BLOCKCOORD("blockcoord", CoordinatesBlock.class),
	ENTITYID  ("eid",        Integer.class);
	
	private String name;
	private Class  type;
	
	private OpisTypes(String name, Class type){
		this.name = name;
		this.type = type;
	}

	public Class getType(){
		return this.type;
	}
	
	public String getName(){
		return this.name;
	}
}
