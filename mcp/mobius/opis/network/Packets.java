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
	
	public static byte TPS                 = 0x23;
	
	public static byte REQ_TELEPORT        = 0x30;  // C => S	
}
