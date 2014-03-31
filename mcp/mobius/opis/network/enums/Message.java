package mcp.mobius.opis.network.enums;

import static mcp.mobius.opis.network.enums.AccessLevel.*;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.events.PlayerTracker;

public enum Message {
	
	LIST_CHUNK_TILEENTS,
	LIST_CHUNK_ENTITIES,
	LIST_CHUNK_LOADED,
	LIST_CHUNK_LOADED_CLEAR,
	LIST_CHUNK_TICKETS,
	LIST_TIMING_TILEENTS,
	LIST_TIMING_ENTITIES,
	LIST_TIMING_HANDLERS,
	LIST_TIMING_CHUNK,
	LIST_AMOUNT_ENTITIES,
	LIST_PLAYERS,
	
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
	
	VALUE_CHUNK_FORCED,
	VALUE_CHUNK_LOADED,
	
	STATUS_START,
	STATUS_STOP,
	STATUS_RUN_UPDATE,
	STATUS_RUNNING,
	STATUS_CURRENT_TIME,
	STATUS_TIME_LAST_RUN,
	STATUS_ACCESS_LEVEL,
	
	COMMAND_TELEPORT_BLOCK(PRIVILEGED),
	COMMAND_TELEPORT_CHUNK(PRIVILEGED),
	COMMAND_TELEPORT_TO_ENTITY(PRIVILEGED),
	COMMAND_TELEPORT_PULL_ENTITY(PRIVILEGED),	
	COMMAND_START(PRIVILEGED),
	COMMAND_KILLALL(PRIVILEGED),
	COMMAND_FILTERING_TRUE,
	COMMAND_FILTERING_FALSE,
	COMMAND_UNREGISTER,
	COMMAND_UNREGISTER_SWING,
	COMMAND_OPEN_SWING,
	
	OVERLAY_CHUNK_ENTITIES,
	OVERLAY_CHUNK_TIMING,
	
	CLIENT_START_PROFILING,
	CLIENT_STOP_PROFILING,
	CLIENT_RESET_PROFILING,
	CLIENT_SHOW_RENDER_TICK,
	CLIENT_SHOW_SWING,
	CLIENT_CLEAR_SELECTION,	
	CLIENT_HIGHLIGHT_BLOCK;
	
	
	private AccessLevel accessLevel = AccessLevel.NONE;
	
	private Message(){
		accessLevel = AccessLevel.NONE;
	}

	private Message(AccessLevel level){
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
	
	public static void setTablesMinimumLevel(AccessLevel level){
		Message.LIST_CHUNK_TILEENTS.setAccessLevel(level);
		Message.LIST_CHUNK_ENTITIES.setAccessLevel(level);
		Message.LIST_TIMING_TILEENTS.setAccessLevel(level);
		Message.LIST_TIMING_ENTITIES.setAccessLevel(level);
		Message.LIST_TIMING_HANDLERS.setAccessLevel(level);
		Message.LIST_TIMING_CHUNK.setAccessLevel(level);
		Message.LIST_AMOUNT_ENTITIES.setAccessLevel(level);
		Message.LIST_PLAYERS.setAccessLevel(level);
	}
	
	public static void setOverlaysMinimumLevel(AccessLevel level){
		Message.OVERLAY_CHUNK_ENTITIES.setAccessLevel(level);
		Message.OVERLAY_CHUNK_TIMING.setAccessLevel(level);
		Message.LIST_CHUNK_LOADED.setAccessLevel(level);
		Message.LIST_CHUNK_TICKETS.setAccessLevel(level);		
	}	
	
	public static void setOpisMinimumLevel(AccessLevel level){
		Message.COMMAND_OPEN_SWING.setAccessLevel(level);
	}
}
