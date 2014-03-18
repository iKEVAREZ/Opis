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
	
	// Final Target
	ENTITIES("entities"),
	TILETENTS("tileents"),
	HANDLERS("handlers"),
	TRUE("true"),
	FALSE("false");
	
	private String text;
	
	private DataReq(String text){
		this.text = text;
	}
}
