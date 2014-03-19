package mcp.mobius.opis.network;

public class Packets {
	
	public static byte LOADED_CHUNKS       = 0x01;	// S => C
	public static byte REQ_CHUNKS_IN_DIM   = 0x02;	// C => S
	public static byte REQ_CHUNKS          = 0x03;	// C => S //Not used right now
	public static byte REQ_TICKETS         = 0x04;	// C => S
	public static byte TICKETS             = 0x05;	// S => C
	public static byte CHUNKS              = 0x06;	// S => C
	
	public static byte MEANTIME            = 0x10;	// S => C
	public static byte REQ_MEANTIME_IN_DIM = 0x11;	// C => S

	public static byte REQ_TES_IN_CHUNK    = 0x20;  // C => S

	public static byte TPS                    = 0x23;
	public static byte MODMEANTIME            = 0x24;  // S => C	
	
	public static byte REQ_TELEPORT           = 0x30;  // C => S
	public static byte REQ_TELEPORT_EID       = 0x31;  // C => S	
	
	public static byte CLIENT_CMD             = 0x32;
	
	public static byte DATA_OVERLAY_CHUNK_ENTITIES = 0x40;
	public static byte DATA_LIST_GENERAL         = 0x45;
	
	public static byte REQ_DATA      = 0x50; // C => S
}
