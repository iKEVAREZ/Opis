package mcp.mobius.opis.commands;

import cpw.mods.fml.common.network.PacketDispatcher;
import cpw.mods.fml.common.network.Player;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.MemoryConnection;
import net.minecraft.network.packet.Packet3Chat;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.dedicated.DedicatedServer;
import net.minecraft.util.ChatMessageComponent;

public class CommandHelp extends CommandBase implements IOpisCommand {

	@Override
	public String getCommandName() {
		return "opis_help";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		IOpisCommand[] commands = {
				new CommandStart(),
				new CommandStop(),
				new CommandReset(),				
				new CommandFrequency(),
				new CommandTicks(),
				
				new CommandChunkList(),
				new CommandTimingTileEntities(),
				new CommandMeanModTime(),

				new CommandTimingEntities(),
				new CommandAmountEntities(),				
				
				new CommandDataDump(),

				new CommandKill(),
				new CommandKillAll(),
				
				new CommandHandler()
		};

		if (icommandsender instanceof EntityPlayer)		
			for (IOpisCommand cmd : commands)
				PacketDispatcher.sendPacketToPlayer(new Packet3Chat(ChatMessageComponent.createFromText(String.format("/%s : %s", cmd.getCommandName(), cmd.getDescription()))), (Player)icommandsender);		
		
		
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
		return true;		
    }

	@Override
	public String getDescription() {
		return "This message.";
	}

}
