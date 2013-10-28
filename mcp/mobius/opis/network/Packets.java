package mcp.mobius.opis.network;

public class Packets {
	public static byte LOADED_CHUNKS     = 0x01;	// S => C
	public static byte REQ_CHUNKS_IN_DIM = 0x02;	// C => S
	public static byte REQ_CHUNKS_ALL    = 0x03;	// C => S //Not used right now
	
	public static byte UNREGISTER_USER   = 0x04;	// C => S

}
