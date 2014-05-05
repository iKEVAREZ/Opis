package mcp.mobius.opis.commands.client;

import cpw.mods.fml.common.network.Player;
import mcp.mobius.opis.network.OpisPacketHandler_OLD;
import mcp.mobius.opis.network.enums.Message;
import mcp.mobius.opis.network.packets.server.NetDataCommand_OLD;
import net.minecraft.command.CommandBase;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommandClientStart extends CommandBase {

	@Override
	public String getCommandName() {
		return "opis_cstart";
	}

	@Override
	public String getCommandUsage(ICommandSender icommandsender) {
		return "";
	}

	@Override
	public void processCommand(ICommandSender icommandsender, String[] astring) {
		if (icommandsender instanceof Player)
			OpisPacketHandler_OLD.validateAndSend(NetDataCommand_OLD.create(Message.CLIENT_START_PROFILING), (Player)icommandsender);		
		//((EntityPlayerMP)icommandsender).playerNetServerHandler.sendPacketToPlayer(NetDataCommand.create(Message.CLIENT_START_PROFILING));
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

}
