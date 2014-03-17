package mcp.mobius.opis.network.json;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.tools.NBTUtil;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagString;

import com.google.gson.Gson;

public class CommandPayload {
	private OpisCommand command;
	private Object[] params;
	
	public CommandPayload(OpisCommand command, Object ... params){
		this.command = command;
		this.params  = params;
	}
	
	public CommandPayload(OpisCommand command){
		this.command = command;
		this.params  = new Object[0];
	}	
	
	public OpisCommand getCommand(){
		return this.command;
	}

	public Object[] getParams(){
		return this.params;
	}
	
	public NBTTagCompound toNBT(){
		Gson          gson = new Gson();
		NBTTagCompound tag = new NBTTagCompound();
		tag.setString("payload", gson.toJson(this));
		return tag;
	}
	
	public void writeToStream(DataOutputStream ostream){
		try {
			NBTUtil.writeNBTTagCompound(this.toNBT(), ostream);
		} catch (Exception e) {
			modOpis.log.warning(String.format("Error while writing command packet to stream for sending. COMMAND : %s", this.command));
			e.printStackTrace();
		}
	}
	
	public static CommandPayload readFromStream(DataInputStream istream){
		try {		
			Gson          gson = new Gson();
			NBTTagCompound tag = NBTUtil.readNBTTagCompound(istream);
			String     jsonStr = tag.getString("payload");
			CommandPayload  commandPacket = gson.fromJson(jsonStr, CommandPayload.class);
			return commandPacket;
			
		} catch (Exception e) {
			modOpis.log.warning("Error while reading command packet from stream.");
			e.printStackTrace();
		}

		return new CommandPayload(OpisCommand.INVALID);
	}
}
