package mcp.mobius.opis.commands;

import mcp.mobius.opis.network.server.Packet_TPS;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandTPS extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_tps";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_TPS.create() );		
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

	@Override
	public String getDescription() {
		return "Unused";
	}
	
}
