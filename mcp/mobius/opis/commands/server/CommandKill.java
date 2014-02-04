package mcp.mobius.opis.commands.server;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.commands.IOpisCommand;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import net.minecraft.world.World;
import net.minecraftforge.common.DimensionManager;

public class CommandKill extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_kill";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (astring.length != 2) return;
		int dim = Integer.valueOf(astring[0]);
		int eid = Integer.valueOf(astring[1]);
		
		World world = DimensionManager.getWorld(dim);
		if (world == null){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find dim %d in world %d", dim)));
			return;
		}
		
		Entity entity = world.getEntityByID(eid);
		if (entity == null) {
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find entity %d in dim %d", eid, dim))); 
			return;
		}
		
		entity.setDead();
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("\u00A7oKilled entity %d in dim %d", eid, dim)));
		return;		
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
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;		
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Kills the given entity id in the given dimension.";
	}

}
