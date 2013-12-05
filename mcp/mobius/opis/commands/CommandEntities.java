package mcp.mobius.opis.commands;

import mcp.mobius.opis.data.EntityManager;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class CommandEntities extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_ent";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		double totalTime = 0;
		System.out.printf("=====\n");
		for (Integer index : EntityManager.stats.keySet()){
			System.out.printf("%s\n", EntityManager.stats.get(index));
			totalTime += EntityManager.stats.get(index).getGeometricMean();
		}
		System.out.printf("Total entity update time : %.3f \u00B5s\n", totalTime);
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

}
