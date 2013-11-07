package mcp.mobius.opis.commands;

import mcp.mobius.opis.modOpis;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;

public class CommandStop extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_stop";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		modOpis.profilerRun = false;
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender par1ICommandSender)
    {
        return true;
    }	
	
}
