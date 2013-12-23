package mcp.mobius.opis.network;

public class Packets {
	public static byte UNREGISTER_USER   = 0x00;	// C => S
	
	public static byte LOADED_CHUNKS       = 0x01;	// S => C
	public static byte REQ_CHUNKS_IN_DIM   = 0x02;	// C => S
	public static byte REQ_CHUNKS          = 0x03;	// C => S //Not used right now
	public static byte REQ_TICKETS         = 0x04;	// C => S
	public static byte TICKETS             = 0x05;	// S => C
	public static byte CHUNKS              = 0x06;	// S => C
	public static byte CHUNKS_TOPLIST      = 0x07;	// S => C		
	
	public static byte MEANTIME            = 0x10;	// S => C
	public static byte REQ_MEANTIME_IN_DIM = 0x11;	// C => S
	public static byte REQ_MEANTIME_ALL    = 0x12;	// C => S //Not used right now

	public static byte REQ_TES_IN_CHUNK    = 0x20;  // C => S
	public static byte TILEENTITIES_CHUNKLIST = 0x21;  // S => C
	public static byte TILEENTITIES_TOPLIST   = 0x22;  // S => C	
	
	public static byte TPS                    = 0x23;
	public static byte MODMEANTIME            = 0x24;  // S => C	
	
	public static byte REQ_TELEPORT           = 0x30;  // C => S
	public static byte REQ_TELEPORT_EID       = 0x31;  // C => S	
	
	/* Entities packets */
	public static byte DATA_OVERLAY_CHUNK_ENTITIES = 0x40;
	public static byte DATA_LIST_CHUNK_ENTITIES    = 0x41;
	public static byte DATA_SCREEN_TIMING_ENTITIES = 0x42;
	public static byte DATA_SCREEN_AMOUNT_ENTITIES = 0x43;
	public static byte DATA_SCREEN_TIMING_HANDLERS = 0x44;		
	
	
	public static byte REQ_DATA      = 0x50; // C => S
	public static byte CLR_SELECTION = 0x51; // S => C
}
