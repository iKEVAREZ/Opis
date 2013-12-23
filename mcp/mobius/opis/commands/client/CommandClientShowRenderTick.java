package mcp.mobius.opis.commands.client;

import mcp.mobius.opis.network.ClientCommand;
import mcp.mobius.opis.network.server.Packet_ClientCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandClientShowRenderTick extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_ctick";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_ClientCommand.create(ClientCommand.SHOW_RENDER_TICK));
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 0;
    }	
	
	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		return true;
    }

}
