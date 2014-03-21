package mcp.mobius.opis.network;

public class Packets {
	
	public static byte LOADED_CHUNKS = 0x01;	// S => C
	public static byte REQ_CHUNKS    = 0x02;	// C => S //Not used right now
	public static byte TICKETS       = 0x03;	// S => C
	public static byte CHUNKS        = 0x04;	// S => C
	public static byte TPS           = 0x06;
	public static byte MODMEANTIME   = 0x07;  // S => C	
	public static byte CLIENT_CMD    = 0x08;
	public static byte DATA_OVERLAY_CHUNK_ENTITIES = 0x09;
	public static byte DATA_LIST_GENERAL           = 0x10;
	public static byte DATA_VALUE_GENERAL          = 0x11;
	public static byte REQ_DATA                    = 0x12; // C => S
}
