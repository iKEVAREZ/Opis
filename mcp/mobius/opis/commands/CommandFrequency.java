package mcp.mobius.opis.commands;

import mcp.mobius.opis.modOpis;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class CommandFrequency extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_delay";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length < 1) return;
		try{
			modOpis.profilerDelay = Integer.valueOf(astring[0]);
			notifyAdmins(icommandsender, "Opis delay set to %s ticks.", new Object[] {astring[0]});
		} catch (Exception e){}
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }	
	
}
