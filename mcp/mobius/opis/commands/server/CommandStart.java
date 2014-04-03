package mcp.mobius.opis.commands.server;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.modOpis;
import mcp.mobius.opis.commands.IOpisCommand;
import mcp.mobius.opis.data.holders.basetypes.SerialInt;
import mcp.mobius.opis.data.managers.EntityManager;
import mcp.mobius.opis.data.managers.MetaManager;
import mcp.mobius.opis.data.managers.TickHandlerManager;
import mcp.mobius.opis.data.managers.TileEntityManager;
import mcp.mobius.opis.data.server.HandlerProfiler;
import mcp.mobius.opis.events.OpisServerTickHandler;
import mcp.mobius.opis.events.PlayerTracker;
import mcp.mobius.opis.network.OpisPacketHandler;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataValue;
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
import mcp.mobius.mobiuscore.profiler_v2.ProfilerSection;

public class CommandStart extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_start";
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
		if (icommandsender instanceof Player){
			icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText("DEPRECATED ! Please run /opis instead."));
			return;
		}				
		
		MetaManager.reset();	
		modOpis.profilerRun = true;
		ProfilerRegistrar.turnOn();
		ProfilerSection.activateAll();

		OpisPacketHandler.sendPacketToAllSwing(NetDataValue.create(Message.STATUS_START, new SerialInt(modOpis.profilerMaxTicks)));		
		icommandsender.sendChatToPlayer(ChatMessageComponent.createFromText(String.format("\u00A7oOpis started with a tick delay %s.", modOpis.profilerDelay)));

		
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

	@Override
	public String getDescription() {
		return "Starts a run.";
	}	
	
}
