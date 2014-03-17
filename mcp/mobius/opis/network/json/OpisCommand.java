package mcp.mobius.opis.network.json;

import java.util.ArrayList;

import mcp.mobius.opis.data.holders.CoordinatesBlock;
import mcp.mobius.opis.data.holders.CoordinatesChunk;

public enum OpisCommand {
	INVALID,
	GET_CHUNKS(OpisTypes.DIM, OpisTypes.CHUNKS),
	GET_LOADED_CHUNKS_IN_DIM(OpisTypes.DIM),
	GET_LOADED_CHUNKS_TICKETS,
	GET_CHUNKS_MEAN_TIME(OpisTypes.DIM),
	GET_TES_IN_CHUNK(OpisTypes.CHUNK),
	GET_DATA(OpisTypes.CHUNK, OpisTypes.DATANAME),
	TELEPORT(OpisTypes.BLOCKCOORD),
	TELEPORT_TO_ENTITY(OpisTypes.ENTITYID, OpisTypes.DIM),
	UNREGISTER_USER;
	
	private OpisTypes[]  types;
	
	private OpisCommand(OpisTypes ... types){
		this.types = types;
	}
	
	public OpisTypes[] getParams(){
		return this.types;
	}
	
	public int getParamIndex(String name){
		for (int i = 0; i < types.length; i++){
			if (types[i].getName().equals(name))
				return i;
		}
		
		return -1;
	}
}
