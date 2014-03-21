package mcp.mobius.opis.network.enums;

public enum DataReq {
	
	INVALID("invalid"),
	NONE("none"),
	
	// General structure of the request
	OVERLAY("overlay"),
	LIST("list"),
	COMMAND("cmd"),
	VALUE("value"),
	
	// First subtype
	CHUNK("chunk"),
	TIMING("timing"),
	AMOUNT("amount"),
	
	FILTERING("filtering"),
	UNREGISTER("unregister"),
	START("start"),
	TELEPORT("teleport"),
	KILLALL("killall"),
	
	// Final Target
	BLOCK("block"),
	ENTITIES("entities"),
	TILETENTS("tileents"),
	HANDLERS("handlers"),
	TRUE("true"),
	FALSE("false"),
	LOADED("loaded"),
	TICKETS("tickets"),
	TICK("tick"),
	WORLDTICK("worldtick"),
	ENTUPDATE("entupdate"),
	UPLOAD("outdata"),
	DOWNLOAD("indata");
	
	private String text;
	
	private DataReq(String text){
		this.text = text;
	}
}
