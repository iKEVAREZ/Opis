package mcp.mobius.opis.network.enums;

import static mcp.mobius.opis.network.enums.AccessLevel.*;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.server.PlayerTracker;

public enum DataReq {
	
	LIST_CHUNK_TILEENTS,
	LIST_CHUNK_ENTITIES,
	LIST_CHUNK_LOADED,
	LIST_CHUNK_TICKETS,
	LIST_TIMING_TILEENTS,
	LIST_TIMING_ENTITIES,
	LIST_TIMING_HANDLERS,
	LIST_TIMING_CHUNK,
	LIST_AMOUNT_ENTITIES,
	
	VALUE_TIMING_TILEENTS,
	VALUE_TIMING_ENTITIES,
	VALUE_TIMING_HANDLERS,
	VALUE_TIMING_WORLDTICK,
	VALUE_TIMING_ENTUPDATE,
	VALUE_TIMING_TICK,
	
	VALUE_AMOUNT_TILEENTS,
	VALUE_AMOUNT_ENTITIES,
	VALUE_AMOUNT_HANDLERS,
	VALUE_AMOUNT_UPLOAD,
	VALUE_AMOUNT_DOWNLOAD,
	
	COMMAND_TELEPORT_BLOCK(PRIVILEGED),
	COMMAND_TELEPORT_TO_ENTITY(PRIVILEGED),
	COMMAND_TELEPORT_PULL_ENTITY(PRIVILEGED),	
	COMMAND_START(PRIVILEGED),
	COMMAND_KILLALL(PRIVILEGED),
	COMMAND_FILTERING_TRUE,
	COMMAND_FILTERING_FALSE,
	COMMAND_UNREGISTER,
	COMMAND_UNREGISTER_SWING,
	
	OVERLAY_CHUNK_ENTITIES,
	OVERLAY_CHUNK_TIMING;
	
	private AccessLevel accessLevel = AccessLevel.NONE;
	
	private DataReq(){
		accessLevel = AccessLevel.NONE;
	}

	private DataReq(AccessLevel level){
		accessLevel = level;
	}	
	
	public AccessLevel getAccessLevel(){
		return this.accessLevel;
	}

	public void setAccessLevel(AccessLevel level){
		this.accessLevel = level;
	}	
	
	public boolean canPlayerUseCommand(Player player){
		return PlayerTracker.instance().getPlayerAccessLevel(player).ordinal() >= this.accessLevel.ordinal();
	}
	
	public boolean canPlayerUseCommand(String name){
		return PlayerTracker.instance().getPlayerAccessLevel(name).ordinal() >= this.accessLevel.ordinal();
	}	
}
