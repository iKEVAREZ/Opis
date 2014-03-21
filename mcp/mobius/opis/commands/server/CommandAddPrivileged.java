package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.server.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandAddPrivileged  extends CommandBase  implements IOpisCommand{

	@Override
	public String getCommandName() {
		return "opis_addpriv";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		PlayerTracker.instance().addPrivilegedPlayer(astring[0]);
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("Player %s added to Opis user list", astring[0])));
	}
	
	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender  instanceof DedicatedServer) return true;
		if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
		return PlayerTracker.instance().isAdmin(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Add a user to the privileged list of users.";
	}

	@Override
	public String getCommandNameOpis() {
		return "opis_addpriv";
	}
}
