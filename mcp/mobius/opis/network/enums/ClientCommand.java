package mcp.mobius.opis.network.enums;

public enum ClientCommand {
	INVALID,
	TEST_CMD,
	START_PROFILING,
	STOP_PROFILING,
	RESET_PROFILING,
	SHOW_RENDER_TICK,
	SHOW_SWING,
	CLEAR_SELECTION;
	/*
	public static byte TEST_CMD = 0x00;
	public static byte START_PROFILING = 0x01;
	public static byte STOP_PROFILING  = 0x02;
	public static byte RESET_PROFILING = 0x03;
	
	public static byte SHOW_RENDER_TICK = 0x04;
	
	public static byte SHOW_SWING = 0x05;
	*/
}
