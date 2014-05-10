package mcp.mobius.opis.commands.server;

import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.events.OpisServerTickHandler;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.OpisPacketHandler_OLD;
import mcp.mobius.opis.network.PacketManager;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatComponentText;

public class CommandReset extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_reset";
	}

	@Override
	public String getCommandNameOpis() {
		return this.getCommandName();
	}	
	
	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		MetaManager.reset();
		
		icommandsender.addChatMessage(new ChatComponentText(String.format("\u00A7oInternal data reseted.")));
		
		//PacketDispatcher.sendPacketToAllPlayers(NetDataCommand.create(Message.CLIENT_CLEAR_SELECTION));
		if (icommandsender instanceof EntityPlayerMP)
			PacketManager.validateAndSend(new NetDataCommand(Message.CLIENT_CLEAR_SELECTION), (EntityPlayerMP)icommandsender);
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender instanceof DedicatedServer) return true;
		//if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
		return PlayerTracker.INSTANCE.isPrivileged(((EntityPlayerMP)sender).getDisplayName());
    }

	@Override
	public String getDescription() {
		return "Cleans up all profiling data and remove client block overlay.";
	}	

}
