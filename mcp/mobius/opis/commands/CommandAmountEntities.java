package mcp.mobius.opis.commands;

import java.util.HashMap;

import mcp.mobius.opis.data.EntityManager;
import mcp.mobius.opis.network.server.Packet_DataScreenAmountEntities;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class CommandAmountEntities extends CommandBase implements IOpisCommand{

	@Override
	public String getCommandName() {
		return "opis_nent";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		HashMap<String, Integer> ents = EntityManager.getCumulativeEntities();
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_DataScreenAmountEntities.create(ents));
		
	}

	@Override
    public int getRequiredPermissionLevel()
    {
        return 3;
    }	

	@Override
    public boolean canCommandSenderUseCommand(ICommandSender sender)
    {
		if (sender instanceof DedicatedServer) return false;
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;		
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Opens a summary of the number of entities on the server, by type.";
	}

}
