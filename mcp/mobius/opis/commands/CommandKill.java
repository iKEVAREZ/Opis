package mcp.mobius.opis.commands;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
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

public class CommandKill extends CommandBase {

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
			PacketDispatcher.sendPacketToPlayer(			
			new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find dim %d in world %d", dim))), 
			               (Player)icommandsender);
			return;
		}
		
		Entity entity = world.getEntityByID(eid);
		if (entity == null) {
			PacketDispatcher.sendPacketToPlayer(			
			new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oCannot find entity %d in dim %d", eid, dim))), 
			               (Player)icommandsender);
			return;
		}
		
		entity.setDead();
		PacketDispatcher.sendPacketToPlayer(			
		new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oKilled entity %d in dim %d", eid, dim))), 
		               (Player)icommandsender);
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
		if (sender instanceof DedicatedServer) return false;
		if (((EntityPlayerMP)sender).playerNetServerHandler.netManager instanceof MemoryConnection) return true;		
        return MinecraftServer.getServer().getConfigurationManager().isPlayerOpped(((EntityPlayerMP)sender).username);
    }

}
