package mcp.mobius.opis.commands.server;

import java.util.ArrayList;
import java.util.HashMap;

import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.CoordinatesChunk;
import mcp.mobius.opis.data.holders.EntityStats;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.network.server.Packet_DataScreenTimingEntities;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;

public class CommandTimingEntities extends CommandBase implements IOpisCommand {

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
		ArrayList<EntityStats> ents = new ArrayList<EntityStats>(); 
		if (astring.length == 0){
			ents = EntityManager.getTopEntities(20);
		}
		else{
			try{
				ents = EntityManager.getTopEntities(Integer.valueOf(astring[0]));
			} catch (Exception e) {return;}
		}
		
		((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(Packet_DataScreenTimingEntities.create(ents));
		
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
		return "Returns the 20 longest entities to update.";
	}

}
