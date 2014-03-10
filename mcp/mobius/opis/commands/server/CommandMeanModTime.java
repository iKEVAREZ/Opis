package mcp.mobius.opis.commands.server;

import java.util.ArrayList;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.ModStats;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.network.server.Packet_ModMeanTime;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandMeanModTime extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_mods";
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
		ArrayList<ModStats> modStats = TileEntityManager.getModStats();
		
		if (icommandsender instanceof EntityPlayer)		
			((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_ModMeanTime.create(modStats));
		else{
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("[Name] NTEs MeanTime"));
			for (ModStats stat : modStats){
				icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(
						String.format("[ %s ] %3d %.2f", stat.getModID(), stat.ntes, stat.dstat.getMean())));
			}
		}		
		
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
		if ((sender instanceof EntityPlayerMP) && ((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;
		if (!(sender instanceof DedicatedServer) && !(sender instanceof EntityPlayerMP)) return true;
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "List of mods update time, in respect to TEs.";
	}

}
