package mcp.mobius.opis.commands.server;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.mobiuscore.profiler.DummyProfiler;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.server.OpisServerTickHandler;
import mcp.mobius.opis.server.PlayerTracker;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;
import mcp.mobius.mobiuscore.profiler.ProfilerRegistrar;

public class CommandStop extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_stop";
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
		modOpis.profilerRun = false;
		ProfilerRegistrar.turnOff();

		if (icommandsender instanceof EntityPlayer)
			PlayerTracker.instance().playersOpis.add((EntityPlayer)icommandsender);
		
		for (EntityPlayer player : PlayerTracker.instance().playersOpis)
			PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("\u00A7oOpis stopped."))), (Player)player);
		
		if (!(icommandsender instanceof EntityPlayer))
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("\u00A7oOpis stopped.")));
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
		return PlayerTracker.instance().isOp(((EntityPlayerMP)sender).username);
    }

	@Override
	public String getDescription() {
		return "Ends a run before completion.";
	}	
	
}
