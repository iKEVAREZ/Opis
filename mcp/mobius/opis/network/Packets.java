package mcp.mobius.opis.network;

public class Packets {
	public static byte UNREGISTER_USER   = 0x00;	// C => S
	
	public static byte LOADED_CHUNKS       = 0x01;	// S => C
	public static byte REQ_CHUNKS_IN_DIM   = 0x02;	// C => S
	public static byte REQ_CHUNKS_ALL      = 0x03;	// C => S //Not used right now
	
	public static byte MEANTIME            = 0x10;	// S => C
	public static byte REQ_MEANTIME_IN_DIM = 0x11;	// C => S
	public static byte REQ_MEANTIME_ALL    = 0x12;	// C => S //Not used right now

	public static byte REQ_TES_IN_CHUNK    = 0x20;  // C => S


}
